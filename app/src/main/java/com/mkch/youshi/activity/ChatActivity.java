package com.mkch.youshi.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.ChartListAdapter;
import com.mkch.youshi.bean.ChatBean;
import com.mkch.youshi.util.CommonUtil;

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
    //数据
    private List<ChatBean> m_chart_list;
    private ChartListAdapter m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        setListener();
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

    }

    private void initData() {
        //标题
        mTvTitle.setText("单聊");
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

        //聊天内容列表
        //布局管理器
        LinearLayoutManager _layout_manager = new LinearLayoutManager(this);
        mRvList.setLayoutManager(_layout_manager);
        //初始化list聊天数据
        m_chart_list = new ArrayList<>();
        m_chart_list.add(new ChatBean("张三","你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊你好啊",ChatBean.MESSAGE_TYPE_IN,"2016-09-01 14:50"));
        m_chart_list.add(new ChatBean("张三","在？",ChatBean.MESSAGE_TYPE_IN,"2016-09-02 15:10"));
        m_chart_list.add(new ChatBean("张三","在吗",ChatBean.MESSAGE_TYPE_IN,"2016-09-03 14:23"));
        m_chart_list.add(new ChatBean("jsjlj","在的",ChatBean.MESSAGE_TYPE_OUT,"2016-09-03 14:25"));
        //适配器
        m_adapter = new ChartListAdapter(m_chart_list);
        mRvList.setAdapter(m_adapter);
        mRvList.smoothScrollToPosition(m_chart_list.size());//滚动到最下面
    }

    @Event({R.id.iv_chat_topbar_back, R.id.iv_chat_topbar_ps,R.id.iv_chat_go_voice,R.id.iv_chat_go_keyboard,R.id.iv_chat_go_expression,R.id.iv_chat_go_more_action
            ,R.id.et_chat_input})
    private void clickOneButton(View view) {
        switch (view.getId()) {
        case R.id.iv_chat_topbar_back://返回按钮
            this.finish();
            break;
        case R.id.iv_chat_topbar_ps://查看用户
            break;
        case R.id.iv_chat_go_voice://切换到语音
            changeKeyboardAndVoice();
            break;
        case R.id.iv_chat_go_keyboard://切换到键盘
            changeKeyboardAndVoice();
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
