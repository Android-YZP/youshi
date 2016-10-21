package com.mkch.youshi.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.mkch.youshi.R;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.fragment.ManyPeopleCaledarFragment;
import com.mkch.youshi.model.Schedule;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.UIUtils;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

import static com.mkch.youshi.activity.AddPersonalEventActivity.mProgressDialog;

/**
 * Created by Smith on 2016/10/18.
 */

public class BaseDetailActivity extends AppCompatActivity implements OnGetGeoCoderResultListener {
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    BaiduMap mBaiduMap = null;
    MapView mMapView = null;
    Button mBtDeleteSch;
    int mSid;
    int mId;
    TextView mTvCancle;
    TextView mTvComp;
    TextView mTvTitle;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mSid = intent.getIntExtra("Sid", -1);
        mId = intent.getIntExtra("id", -1);
    }

    public void initTopBar() {
        mTvCancle = (TextView) findViewById(R.id.tv_add_event_cancel);
        mTvComp = (TextView) findViewById(R.id.tv_add_event_complete);
        mTvTitle = (TextView) findViewById(R.id.tv_add_event_title);
        mTvComp.setText("编辑");
        mTvTitle.setText("详情信息");
    }


    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mSearch.destroy();
        super.onDestroy();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(UIUtils.getContext(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
        Toast.makeText(UIUtils.getContext(), strInfo, Toast.LENGTH_LONG).show();

        //设置默认的缩放比例
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(17).build()));
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(UIUtils.getContext(), "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        mBaiduMap.clear();
        mBaiduMap.addOverlay(new MarkerOptions().position(result.getLocation())
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marka)));
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLng(result
                .getLocation()));
        //设置缩放比例
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(55).build()));
        Toast.makeText(UIUtils.getContext(), result.getAddress(),
                Toast.LENGTH_LONG).show();

    }

    /**
     * 封装请求参数
     *
     * @return
     */
    private String createJson(String Sid) {
        JSONObject _json_args = null;
        String _DatastoString = "";
        try {
            _json_args = new JSONObject();
            _json_args.put("Sid", Sid);
            _DatastoString = _json_args.toString();
            UIUtils.LogUtils(_DatastoString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return _DatastoString;
    }

    /**
     * 拒绝或者接收
     */
    public void deleteSch2Net(String Sid) {
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.DeleteSchedule);
        //包装请求参数
        final String code = CommonUtil.getUserInfo(UIUtils.getContext()).getLoginCode();
        String _req_json = createJson(Sid);
        requestParams.addBodyParameter("", _req_json);//用户名
        requestParams.addHeader("sVerifyCode", code);//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                JSONObject _json_result = null;
                if (result != null) {
                    UIUtils.LogUtils(result + code);
                    try {
                        _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            DbManager mDbManager = DBHelper.getDbManager();
                            //在网络上删除数据成功之后,把本地的也删除,然后结束本页面显示
                            WhereBuilder whereBuilder = WhereBuilder.b();
                            whereBuilder.and("id", "=", mId + "");
//                          whereBuilder.and("id","=",mId).or("id","=","1").expr(" and mobile > '2015-12-29 00:00:01' ");
                            int delete = mDbManager.delete(Schedule.class, whereBuilder);
                            finish();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (DbException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //使用handler通知UI提示用户错误信息
                if (ex instanceof ConnectException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_ERROR, myHandler);
                } else if (ex instanceof ConnectTimeoutException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_CONNECT_TIMEOUT, myHandler);
                } else if (ex instanceof SocketTimeoutException) {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_SERVER_TIMEOUT, myHandler);
                } else {
                    CommonUtil.sendErrorMessage(CommonConstants.MSG_DATA_EXCEPTION, myHandler);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    /**
     * 对话框
     */
    public void showAlertDialog() {
        // 1, 定义普通对话框的构建者
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseDetailActivity.this);
        // 2, 构建对话框
//        builder.setIcon(R.drawable.img_23);// 设置图标
        builder.setTitle("提示");// 设置标题
        builder.setMessage("是否确定删除这个日程?");// 设置内容
        // 设置取消按钮
        builder.setNegativeButton("取消", null);
        // 设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                //上传数据
                deleteSch2Net(mSid + "");
            }
        });
        // 3, 显示对话框
        builder.show();
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(BaseDetailActivity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            int flag = msg.what;
            switch (flag) {
                case 0:
                    String errorMsg = (String) msg.getData().getSerializable("ErrorMsg");
                    UIUtils.showTip(errorMsg);
                    break;
                default:
                    break;
            }
        }
    }

    private BaseDetailActivity.MyHandler myHandler = new BaseDetailActivity.MyHandler(BaseDetailActivity.this);

}
