package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.AddFriendsMethodsListAdapter;
import com.mkch.youshi.util.RosterHelper;
import com.mkch.youshi.util.XmppHelper;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jxmpp.util.XmppStringUtils;

public class AddFriendsActivity extends Activity {

    private final static int REQUEST_CODE = 1;
    private ImageView mIvBack;
    private TextView mTvTitle;
    private EditText mEtSearch;
    private ListView mListView;
    private XMPPTCPConnection connection; //connection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friends);
        initView();
        initData();
        setListener();
        ZXingLibrary.initDisplayOpinion(this);
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_common_topbar_back);
        mTvTitle = (TextView) findViewById(R.id.tv_common_topbar_title);
        mEtSearch = (EditText) findViewById(R.id.et_add_friends_search);
        mListView = (ListView) findViewById(R.id.list_add_friends);
    }

    private void initData() {
        mTvTitle.setText("添加好友");
        //获取连接
        connection = XmppHelper.getConnection();
        ListAdapter mAdapter = new AddFriendsMethodsListAdapter(AddFriendsActivity.this);
        mListView.setAdapter(mAdapter);
        //搜索框不弹出软键盘
        mEtSearch.setInputType(InputType.TYPE_NULL);
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddFriendsActivity.this.finish();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent _intent = null;
                switch (position) {
                    case 0:
                        _intent = new Intent(AddFriendsActivity.this, PhoneContactsActivity.class);
                        startActivity(_intent);
                        break;
                    case 1:
                        Intent intent = new Intent(AddFriendsActivity.this, CaptureActivity.class);
                        startActivityForResult(intent, REQUEST_CODE);
                        break;
                    default:
                        break;
                }
            }
        });
        mEtSearch.setOnClickListener(new AddFriendsOnClickListener());
    }

    /**
     * 自定义点击监听类
     *
     * @author JLJ
     */
    private class AddFriendsOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Intent _intent = null;
            switch (view.getId()) {
                case R.id.et_add_friends_search:
                    _intent = new Intent(AddFriendsActivity.this, SearchResultActivity.class);
                    startActivity(_intent);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (data != null) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                    //获取用户名和_OpenFireUsrName；并发起添加功能
                    //获取连接
                    connection = XmppHelper.getConnection();
                    final String _jid = XmppStringUtils.completeJidFrom(result, connection.getServiceName());//转jid
                    //发送请求添加好友
                    RosterHelper _roster_helper = RosterHelper.getInstance(connection);
                    String _nickname = XmppStringUtils.parseLocalpart(_jid);
                    _roster_helper.addEntry(_jid, _nickname, "Friends");
                    //立马删除好友
                    _roster_helper.removeEntry(_jid);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(AddFriendsActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
