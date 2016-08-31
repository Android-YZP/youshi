package com.mkch.youshi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.mkch.youshi.R;

import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.x;

public class TestNetActivity extends AppCompatActivity {

    private TextView mTvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_net);
        mTvTest = (TextView) findViewById(R.id.tv_test);

        RequestParams params = new RequestParams("http://www.baidu.com");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onCancelled(Callback.CancelledException arg0) {
                LogUtil.d("取消");
            }

            @Override
            public void onError(Throwable arg0, boolean arg1) {
                LogUtil.d("错误");
            }

            @Override
            public void onFinished() {
                LogUtil.d("完成");
            }

            @Override
            public void onSuccess(String arg0) {
                // 成功下载，显示到txtv上面
                mTvTest.setText(arg0);
            }
        });
    }


}
