package com.mkch.youshi.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.mkch.youshi.R;

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
    private ImageView mIvGoVoice;//语音

    @ViewInject(R.id.iv_chat_go_expression)
    private ImageView mIvGoExpression;//表情

    @ViewInject(R.id.iv_chat_go_more_action)
    private ImageView mIvGoMoreAction;//更多操作

    @ViewInject(R.id.gv_chat_more_action)
    private GridView mGvMoreAction;//更多操作-网格视图

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
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


    }

    @Event({R.id.iv_chat_topbar_back, R.id.iv_chat_topbar_ps})
    private void clickOneButton(View view) {
        switch (view.getId()) {
        case R.id.iv_chat_topbar_back://返回按钮
            this.finish();
            break;
        case R.id.iv_chat_topbar_ps://查看用户
            break;
        case R.id.iv_chat_go_voice://切换到语音
            break;
        case R.id.iv_chat_go_expression://弹出表情
            break;
        case R.id.iv_chat_go_more_action://弹出更多操作
            break;
    }
}


}
