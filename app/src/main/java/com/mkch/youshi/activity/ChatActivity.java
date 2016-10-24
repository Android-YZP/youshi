package com.mkch.youshi.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.ChartListAdapter;
import com.mkch.youshi.bean.User;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.ChatBean;
import com.mkch.youshi.model.Friend;
import com.mkch.youshi.model.MessageBox;
import com.mkch.youshi.receiver.ChatReceiver;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.TimesUtils;
import com.mkch.youshi.view.RecordButton;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMSoundElem;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;

import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    private RecordButton mBtnUseVoice;

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
    // 聊天
    @ViewInject(R.id.rv_chart_list)
    private RecyclerView mRvList;
    private DbManager dbManager;//数据库管理对象
    //数据
    private List<ChatBean> m_chart_list;
    private ChartListAdapter m_adapter;
    private String _openfirename;
    private static ProgressDialog mProgressDialog = null;//加载
    private ChatReceiver mChatReceiver;
    private User mUser;
    MediaPlayer mediaPlayer = new MediaPlayer();

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            int _what = msg.what;
            switch (_what) {
                case 0:
                    //出现错误
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    Toast.makeText(ChatActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    break;
                case ChatReceiver.RECEIVE_CHAT_MSG:
                    int _chat_id = (int) msg.obj;//最新的一条消息-写在广播接收处
                    Log.d("zzz----RECEIVE_CHAT_MSG", String.valueOf(_chat_id));
                    //查询该消息内容并刷新到UI
                    try {
                        ChatBean _chat_bean = dbManager.findById(ChatBean.class, _chat_id);
                        //更新UI界面，获取最新的用户列表
                        updateUIfromReceiver(_chat_bean);
                    } catch (DbException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case CommonConstants.SEND_MSG_SUCCESS:
                    Log.d("zzz----SEND_MSG_SUCCESS", "");
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
    private String friendId, selfId;

    /**
     * 更新UI界面，获取最新的聊天记录
     *
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
        dbManager = DBHelper.getDbManager();
        initData();
        setListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setListener() {
        mBtnUseVoice.setOnRecordFinishedListener(new RecordButton.OnRecordFinishedListener() {
            @Override
            public void onFinished(File audioFile, int duration) {
                TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, _openfirename);
                //构造一条消息
                TIMMessage msg = new TIMMessage();
                //添加语音
                TIMSoundElem elem = new TIMSoundElem();
                final String soundFile = audioFile.getAbsolutePath();
                final int soundDuration = duration / 1000;
                elem.setPath(soundFile); //填写语音文件路径
                elem.setDuration(duration);  //填写语音时长
                //将elem添加到消息
                if (msg.addElement(elem) != 0) {
                    Log.d("zzz-------", "addElement failed");
                    return;
                }
                //发送消息
                conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {//发送消息回调
                    @Override
                    public void onError(int code, String desc) {//发送消息失败
                        Log.d("zzz---sendMessage sound", code + "Error:" + desc);
                    }

                    @Override
                    public void onSuccess(TIMMessage msg) {//发送消息成功
                        Log.d("zzz---sendMessage sound", "sendMessage is success");
                        addSoundMessageBox(soundFile, soundDuration);
                    }
                });
            }
        });
        m_adapter.setOnItemClickListener(new ChartListAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) throws IOException {
                if (m_chart_list.get(position).getMsgModel() == 2) {
                    if (mediaPlayer.isPlaying()) {
                        Log.d("zzz---------mediaPlayer", "mediaPlayer is Playing");
                        mediaPlayer.stop();
                    } else {
                        try {
                            Log.d("zzz---------mediaPlayer", "mediaPlayer is stop");
                            File audio = new File(m_chart_list.get(position).getFileName());
                            FileInputStream fis = new FileInputStream(audio);
                            mediaPlayer.reset();
                            mediaPlayer.setDataSource(fis.getFD());
                            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                            mediaPlayer.prepare();
                            mediaPlayer.start();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        mEtChatInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //当获取焦点时，只要有其他面板在打开状态，隐藏这些面板，差表情面板未设计
                if (hasFocus) {
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
                String _input_text = s.toString();
                if (_input_text != null && !_input_text.equals("")) {
                    mIvGoMoreAction.setVisibility(View.GONE);
                    mBtnChatSend.setVisibility(View.VISIBLE);
                } else {
                    mIvGoMoreAction.setVisibility(View.VISIBLE);
                    mBtnChatSend.setVisibility(View.GONE);
                }
            }
        });
    }

    private void initData() {
        mUser = CommonUtil.getUserInfo(this);
        selfId = mUser.getOpenFireUserName();
        //获取会话
        Intent _intent = getIntent();
        if (_intent != null) {
            _openfirename = _intent.getStringExtra("_openfirename");
            if (_openfirename != null && !_openfirename.equals("")) {
                try {
                    mFriend = dbManager.selector(Friend.class)
                            .where("friendid", "=", _openfirename)
                            .and("status", "=", "1")
                            .findFirst();
                    //标题
                    if (mFriend.getNickname() == null || mFriend.getNickname().equals("")) {
                        mTvTitle.setText(mFriend.getPhone());
                    } else {
                        mTvTitle.setText(mFriend.getNickname());
                    }
                    friendId = mFriend.getFriendid() + "@" + "TIMConversationType.C2C";
                    queryChatsInfoFromDB(friendId, selfId);
                    //注册聊天监听的Receiver
                    mChatReceiver = new ChatReceiver(mHandler);
                    IntentFilter filter = new IntentFilter("yoshi.action.chatsbroadcast");
                    registerReceiver(mChatReceiver, filter);
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
        String[] _str_names = new String[]{"照片", "拍摄", "位置", "文件"};
        List<Map<String, Object>> _maps = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            HashMap<String, Object> _map1 = new HashMap<>();
            _map1.put("pic_res", _pic_reses[i]);
            _map1.put("str_name", _str_names[i]);
            _maps.add(_map1);
        }
        //适配器
        SimpleAdapter _adapter = new SimpleAdapter(this, _maps, R.layout.gv_item_chat_more_action, new String[]{"pic_res", "str_name"}, new int[]{R.id.iv_item_chat_more_action, R.id.tv_item_chat_more_action});
        //设置适配器
        mGvMoreAction.setAdapter(_adapter);
    }

    /**
     * 从数据库查询聊天信息
     *
     * @param
     */
    private void queryChatsInfoFromDB(final String friendId, final String selfId) {
        //聊天内容列表
        //布局管理器
        LinearLayoutManager _layout_manager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(_layout_manager);
        //初始化list聊天数据
        try {
            //查询
            dbManager = DBHelper.getDbManager();
            m_messageBox = dbManager.selector(MessageBox.class)
                    .where("friend_id", "=", friendId)
                    .and("self_id", "=", selfId)
                    .findFirst();
            //若消息盒子存在，则获取消息盒子的ID以及该消息盒子的所有消息
            if (m_messageBox != null) {
                mMessageBoxId = m_messageBox.getId();
                m_chart_list = m_messageBox.getChatBeans(dbManager);
            }
        } catch (DbException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (m_chart_list == null) {
            m_chart_list = new ArrayList<>();
        }
        //适配器
        m_adapter = new ChartListAdapter(m_chart_list, mFriend, mUser, this);
        mRvList.setAdapter(m_adapter);
        mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面
    }

    @Event({R.id.iv_chat_topbar_back, R.id.iv_chat_topbar_ps, R.id.iv_chat_go_voice, R.id.iv_chat_go_keyboard, R.id.iv_chat_go_expression, R.id.iv_chat_go_more_action
            , R.id.et_chat_input, R.id.btn_chat_send})
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
                if (_input_text_content != null && !_input_text_content.equals("")) {
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
                if (mLineMoreAction.getVisibility() == View.VISIBLE) {
                    mLineMoreAction.setVisibility(View.GONE);
                } else {
                    CommonUtil.hideInput(this, mEtChatInput);//隐藏输入法
                    mLineMoreAction.setVisibility(View.VISIBLE);
                }
                if (m_chart_list != null) {
                    mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面
                }
                break;
        }
    }

    /**
     * 发送信息给好友
     */
    private void sendInfoToFriend() {
        final String _msg = mEtChatInput.getText().toString();
        if (TextUtils.isEmpty(_openfirename) || TextUtils.isEmpty(_msg)) {
            Toast.makeText(getApplicationContext(), "接收方或内容不能为空", Toast.LENGTH_LONG).show();
            mEtChatInput.setText("");
            return;
        }
        //发出去后立马清空edittext
        mEtChatInput.setText("");
        //开启副线程-发送消息
        sendMsg(_msg);
    }

    private void sendMsg(final String _msg) {
        TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.C2C, _openfirename);
        TIMMessage msg = new TIMMessage();
        //添加文本内容
        TIMTextElem elem = new TIMTextElem();
        elem.setText(_msg);
        //将elem添加到消息
        if (msg.addElement(elem) != 0) {
            Log.d("zzz-------", "addElement failed");
            return;
        }
        conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(int i, String s) {
                Log.d("zzz----sendMessage text", i + "Error:" + s);
            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                Log.d("zzz----sendMessage text", "sendMessage is success");
                addTextMessageBox(_msg);
            }
        });
    }

    //文本发送成功
    private void addTextMessageBox(final String msg) {
        try {
            //localMessage
            ChatBean _local_message = new ChatBean(selfId, msg, ChatBean.MESSAGE_TYPE_OUT, TimesUtils.getNow());
            m_chart_list.add(_local_message);
            if (mMessageBoxId == 0) {
                //新增该消息盒子
                m_messageBox = new MessageBox(mFriend.getHead_pic(), mFriend.getNickname(), _local_message.getContent(), 0, TimesUtils.getNow(), 0, MessageBox.MB_TYPE_CHAT, friendId, selfId);
                dbManager.saveBindingId(m_messageBox);
                mMessageBoxId = m_messageBox.getId();
            } else {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //语音发送成功
    private void addSoundMessageBox(final String file, final int duration) {
        try {
            //localMessage
            ChatBean _local_message = new ChatBean(selfId, TimesUtils.getNow(), ChatBean.MESSAGE_TYPE_OUT, duration, file, "[语音]");
            m_chart_list.add(_local_message);
            if (mMessageBoxId == 0) {
                //新增该消息盒子
                m_messageBox = new MessageBox(mFriend.getHead_pic(), mFriend.getNickname(), _local_message.getContent(), 0, TimesUtils.getNow(), 0, MessageBox.MB_TYPE_CHAT, friendId, selfId);
                dbManager.saveBindingId(m_messageBox);
                mMessageBoxId = m_messageBox.getId();
            } else {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 切换键盘和语音
     */
    private void changeKeyboardAndVoice() {
        if (mLineKeybordBlock.getVisibility() == View.VISIBLE) {
            //点击了语音
            CommonUtil.hideInput(this, mEtChatInput);//隐藏输入法
            mIvGoVoice.setVisibility(View.GONE);
            mIvGoKeyboard.setVisibility(View.VISIBLE);
            mLineKeybordBlock.setVisibility(View.GONE);
            mBtnUseVoice.setVisibility(View.VISIBLE);
        } else {
            //点击了键盘
            mIvGoVoice.setVisibility(View.VISIBLE);
            mIvGoKeyboard.setVisibility(View.GONE);
            mLineKeybordBlock.setVisibility(View.VISIBLE);
            mBtnUseVoice.setVisibility(View.GONE);
        }
        mLineMoreAction.setVisibility(View.GONE);//隐藏面板-差表情面板
    }
}
