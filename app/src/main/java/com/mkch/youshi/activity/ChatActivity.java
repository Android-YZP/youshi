package com.mkch.youshi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.adapter.ChartListAdapter;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.ChatBean;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.model.MessageBox;
import com.mkch.youshi.receiver.ChatReceiver;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.TimesUtils;
import com.mkch.youshi.util.XmppHelper;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.util.XmppStringUtils;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_chat)
public class ChatActivity extends BaseActivity {

    @ViewInject(R.id.iv_chat_topbar_back)
    private ImageView mIvBack;//返回按钮

    @ViewInject(R.id.tv_chat_topbar_title)
    private TextView mTvTitle;//标题

    @ViewInject(R.id.iv_chat_topbar_ps)
    private ImageView mIvPsInfo;//查看用户

    @ViewInject(R.id.iv_chat_go_voice)
    private ImageView mIvGoVoice;//去使用语音

    @ViewInject(R.id.line_keyboard_comptonts_use)
    private LinearLayout mLineKeybordBlock;//键盘输入框和表情选择

    @ViewInject(R.id.btn_chat_voice_use)
    private Button mBtnUseVoice;

    @ViewInject(R.id.iv_chat_go_keyboard)
    private ImageView mIvGoKeyboard;//去使用文字

    @ViewInject(R.id.et_chat_input)
    private EditText mEtChatInput;//聊天框

    @ViewInject(R.id.iv_chat_go_expression)
    private ImageView mIvGoExpression;//表情

    @ViewInject(R.id.iv_chat_go_more_action)
    private ImageView mIvGoMoreAction;//更多操作

    @ViewInject(R.id.btn_chat_send)
    private Button mBtnChatSend;//发送

    @ViewInject(R.id.line_chat_more_action_block)
    private LinearLayout mLineMoreAction;//更多操作-面板弹出

    @ViewInject(R.id.gv_chat_more_action)
    private GridView mGvMoreAction;//更多操作-网格视图


    /*
    聊天
     */
    @ViewInject(R.id.rv_chart_list)
    private RecyclerView mRvList;
    private String m_jid;

    //xmpp
    private XMPPTCPConnection connection;
    private Chat mChat;
    private ChatManager chatmanager;

    private DbManager dbManager;

    //数据
    private List<ChatBean> m_chart_list;
    private ChartListAdapter m_adapter;

    private static ProgressDialog mProgressDialog = null;//加载
    private ChatReceiver mChatReceiver;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            int _what = msg.what;
            switch (_what){
                case 0:
                    //出现错误
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    Toast.makeText(ChatActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    break;
                case ChatReceiver.RECEIVE_CHAT_MSG:
                    int _chat_id = (int) msg.obj;//最新的一条消息-写在广播接收处
                    if (_chat_id!=0){
                        Log.d("jlj","ChatActivity-----------------------------_chat_id"+_chat_id);
                        //查询该消息内容并刷新到UI
                        try {
                            ChatBean _chat_bean = dbManager.findById(ChatBean.class,_chat_id);
                            //更新UI界面，获取最新的用户列表
                            updateUIfromReceiver(_chat_bean);
                        } catch (DbException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                    }


                    break;
                case CommonConstants.SEND_MSG_SUCCESS:
                    m_adapter.notifyDataSetChanged();
                    mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面
                    break;
                default:
                    break;
            }

            super.handleMessage(msg);

        }
    };
    private int mMessageBoxId;
    private Friend mFriend;
    private MessageBox m_messageBox;


    /**
     * 更新UI界面，获取最新的聊天记录
     * @param _chat_bean
     */
    private void updateUIfromReceiver(ChatBean _chat_bean) {
        m_chart_list.add(_chat_bean);
        m_adapter.notifyDataSetChanged();
        mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connection = XmppHelper.getConnection();
        chatmanager = ChatManager.getInstanceFor(connection);
        dbManager = DBHelper.getDbManager();
        initData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChatReceiver!=null){
            unregisterReceiver(mChatReceiver);
        }

    }

    private void setListener() {
        mEtChatInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //当获取焦点时，只要有其他面板在打开状态，隐藏这些面板，差表情面板未设计
                if (hasFocus){
                    mLineMoreAction.setVisibility(View.GONE);
                }
            }
        });
        mEtChatInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("jlj","----------------------afterTextChanged："+s.toString());
                String _input_text = s.toString();
                if (_input_text!=null&&!_input_text.equals("")){
                    mIvGoMoreAction.setVisibility(View.GONE);
                    mBtnChatSend.setVisibility(View.VISIBLE);
                }else{
                    mIvGoMoreAction.setVisibility(View.VISIBLE);
                    mBtnChatSend.setVisibility(View.GONE);
                }
            }
        });

    }

    private void initData() {
        //标题
        mTvTitle.setText("单聊");
        //_openfirename获取聊天对象
        Intent _intent = getIntent();
        if (_intent!=null){
            String _openfirename = _intent.getStringExtra("_openfirename");
            if (_openfirename!=null&&!_openfirename.equals("")){

                try {
                    mFriend = dbManager.selector(Friend.class)
                            .where("friendid","=",_openfirename)
                            .and("status","=","1")
                            .findFirst();
                    if (mFriend!=null){
                        Log.d("jlj","ChatActivity mFriend--------------------" + mFriend.toString());

                    }else{
                        Log.d("jlj","ChatActivity mFriend--------------------数据异常");
                        return;
                    }
                    //查询出与该好友的消息记录，并更新UI
                    m_jid = XmppStringUtils.completeJidFrom(mFriend.getFriendid(),connection.getServiceName());
                    queryChatsInfoFromDB(m_jid);//从数据库查询聊天信息
                    //创建一个聊天
                    mChat = chatmanager.createChat(m_jid);
                    //注册聊天监听的Receiver
                    mChatReceiver = new ChatReceiver(mHandler);
                    IntentFilter filter = new IntentFilter("yoshi.action.chatsbroadcast");
                    registerReceiver(mChatReceiver,filter);

                } catch (DbException e) {
                    e.printStackTrace();
                }
            }


        }


        //gridview网格视图-更多动作
        //数据源
        int[] _pic_reses = new int[]{R.drawable.chat_photo,
        R.drawable.chat_shot,
        R.drawable.chat_location,
        R.drawable.chat_file};
        String[] _str_names = new String[]{"照片","拍摄","位置","文件"};
        List<Map<String,Object>> _maps = new ArrayList<>();
        for (int i=0;i<4;i++){
            HashMap<String, Object> _map1 = new HashMap<>();
            _map1.put("pic_res",_pic_reses[i]);
            _map1.put("str_name",_str_names[i]);
            _maps.add(_map1);
        }
        //适配器
        SimpleAdapter _adapter = new SimpleAdapter(this,_maps,R.layout.gv_item_chat_more_action,new String[]{"pic_res","str_name"},new int[]{R.id.iv_item_chat_more_action,R.id.tv_item_chat_more_action});
        //设置适配器
        mGvMoreAction.setAdapter(_adapter);


    }

    /**
     * 从数据库查询聊天信息
     * @param jid
     */
    private void queryChatsInfoFromDB(String jid) {

        //聊天内容列表
        //布局管理器
        LinearLayoutManager _layout_manager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(_layout_manager);
        //初始化list聊天数据
        //默认数据
//        m_chart_list = new ArrayList<>();
//        m_chart_list.add(new ChatBean("张三","你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊",ChatBean.MESSAGE_TYPE_IN,"2016-09-01 14:50"));
//        m_chart_list.add(new ChatBean("张三","在？",ChatBean.MESSAGE_TYPE_IN,"2016-09-02 15:10"));
//        m_chart_list.add(new ChatBean("张三","在吗",ChatBean.MESSAGE_TYPE_IN,"2016-09-03 14:23"));
//        m_chart_list.add(new ChatBean("jsjlj","在的",ChatBean.MESSAGE_TYPE_OUT,"2016-09-03 14:25"));
        //查询该JID的消息盒子的所有消息
        try {
            //查询
            dbManager = DBHelper.getDbManager();
            m_messageBox = dbManager.selector(MessageBox.class)
                    .where("jid", "=", jid)
                    .findFirst();
            //若消息盒子存在，则获取消息盒子的ID以及该消息盒子的所有消息
            if (m_messageBox !=null){
                mMessageBoxId = m_messageBox.getId();
                m_chart_list = m_messageBox.getChatBeans(dbManager);
            }


        } catch (DbException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        if (m_chart_list==null){
            m_chart_list = new ArrayList<>();
        }
        //适配器
        m_adapter = new ChartListAdapter(m_chart_list);
        mRvList.setAdapter(m_adapter);
        mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面


    }

    @Event({R.id.iv_chat_topbar_back, R.id.iv_chat_topbar_ps,R.id.iv_chat_go_voice,R.id.iv_chat_go_keyboard,R.id.iv_chat_go_expression,R.id.iv_chat_go_more_action
            ,R.id.et_chat_input,R.id.btn_chat_send})
    private void clickOneButton(View view) {
        switch (view.getId()) {
            case R.id.btn_chat_send://发送
                sendInfoToFriend();
                break;
        case R.id.iv_chat_topbar_back://返回按钮
            this.finish();
            break;
        case R.id.iv_chat_topbar_ps://查看用户
            break;
        case R.id.iv_chat_go_voice://切换到语音
            changeKeyboardAndVoice();

            //隐藏发送按钮和显示更多
            mIvGoMoreAction.setVisibility(View.VISIBLE);
            mBtnChatSend.setVisibility(View.GONE);
            break;
        case R.id.iv_chat_go_keyboard://切换到键盘
            changeKeyboardAndVoice();

            //判断是否有文字在输入框
            String _input_text_content = mEtChatInput.getText().toString();
            if (_input_text_content!=null&&!_input_text_content.equals("")){
                //隐藏更多和显示发送按钮
                mIvGoMoreAction.setVisibility(View.GONE);
                mBtnChatSend.setVisibility(View.VISIBLE);
            }
            break;
        case R.id.et_chat_input://点击输入框
            mLineMoreAction.setVisibility(View.GONE);//隐藏面板-差表情面板
            mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面
            break;
        case R.id.iv_chat_go_expression://弹出表情
            break;
        case R.id.iv_chat_go_more_action://切换弹出更多操作
            if (mLineMoreAction.getVisibility()==View.VISIBLE){
                mLineMoreAction.setVisibility(View.GONE);
            }else {
                mLineMoreAction.setVisibility(View.VISIBLE);
                CommonUtil.hideInput(this,mEtChatInput);//隐藏输入法
            }
            mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面
            break;
        }
    }

    /**
     * 发送信息给好友
     */
    private void sendInfoToFriend() {
            final String _msg = mEtChatInput.getText().toString();
            if (TextUtils.isEmpty(m_jid)||TextUtils.isEmpty(_msg)) {
                Toast.makeText(getApplicationContext(), "接收方或内容不能为空", Toast.LENGTH_LONG).show();
                mEtChatInput.setText("");
                return;
            }
            //发出去后立马清空edittext
            mEtChatInput.setText("");
            //开启副线程-发送消息
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        String _full_user = connection.getUser();
                        String _openfirename = XmppStringUtils.parseLocalpart(_full_user);
                        //localMessage
                        ChatBean _local_message = new ChatBean(_openfirename,_msg,ChatBean.MESSAGE_TYPE_OUT, TimesUtils.getNow());

                        //remoteMessage
                        ChatBean _remote_message = new ChatBean(_openfirename,_msg,ChatBean.MESSAGE_TYPE_IN, TimesUtils.getNow());

                        Gson _gson = new Gson();
                        String _gson_str = _gson.toJson(_remote_message);
                        Log.d("jlj","sendInfoToFriend--------------send msg = "+_gson_str);
                        mChat.sendMessage(_gson_str);

                        m_chart_list.add(_local_message);
                        if (mMessageBoxId==0){
                            //新增该消息盒子
                            MessageBox _msg_box = new MessageBox(mFriend.getHead_pic(),mFriend.getNickname(),_local_message.getContent(),0,TimesUtils.getNow(),0,MessageBox.MB_TYPE_CHAT,m_jid);
                            dbManager.saveBindingId(_msg_box);
                            mMessageBoxId = _msg_box.getId();
                        }else{
                            //更新消息盒子
                            m_messageBox.setBoxLogo(mFriend.getHead_pic());
                            m_messageBox.setTitle(mFriend.getNickname());
                            m_messageBox.setInfo(_local_message.getContent());
                            m_messageBox.setLasttime(TimesUtils.getNow());
                            dbManager.saveOrUpdate(m_messageBox);
                        }
                        _local_message.setMsgboxid(mMessageBoxId);//設置消息盒子id
                        dbManager.save(_local_message);//保存一条消息到数据库

                        mHandler.sendEmptyMessage(CommonConstants.SEND_MSG_SUCCESS);
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }).start();
    }

    /**
     * 切换键盘和语音
     */
    private void changeKeyboardAndVoice() {
        if (mLineKeybordBlock.getVisibility()== View.VISIBLE){
            //点击了语音
            CommonUtil.hideInput(this,mEtChatInput);//隐藏输入法

            mIvGoVoice.setVisibility(View.GONE);
            mIvGoKeyboard.setVisibility(View.VISIBLE);

            mLineKeybordBlock.setVisibility(View.GONE);
            mBtnUseVoice.setVisibility(View.VISIBLE);
        }else {
            //点击了键盘
            mIvGoVoice.setVisibility(View.VISIBLE);
            mIvGoKeyboard.setVisibility(View.GONE);

            mLineKeybordBlock.setVisibility(View.VISIBLE);
            mBtnUseVoice.setVisibility(View.GONE);
        }
        mLineMoreAction.setVisibility(View.GONE);//隐藏面板-差表情面板
    }


}
