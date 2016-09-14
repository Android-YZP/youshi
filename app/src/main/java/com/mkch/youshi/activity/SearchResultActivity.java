package com.mkch.youshi.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.refreshview.XRefreshView;
import com.andview.refreshview.utils.LogUtils;
import com.mkch.youshi.R;
import com.mkch.youshi.bean.SearchResult;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.util.CommonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class SearchResultActivity extends Activity {

    private ImageView mIvBack;
    private EditText mEtSearch;
    private ListView mListView;
    private ArrayList<SearchResult> searchResult = new ArrayList<>();
    private XRefreshView refreshView;
    public static long lastRefreshTime;
    private static ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initView();
        initData();
        setListener();
    }

    private void initView() {
        mIvBack = (ImageView) findViewById(R.id.iv_search_result_back);
        mEtSearch = (EditText) findViewById(R.id.et_search_result);
        refreshView = (XRefreshView) findViewById(R.id.custom_view);
        mListView = (ListView) findViewById(R.id.list_search_result);
    }

    private void initData() {
        // 设置是否可以下拉刷新
        refreshView.setPullRefreshEnable(true);
        // 设置是否可以上拉加载
        refreshView.setPullLoadEnable(true);
        // 设置上次刷新的时间
        refreshView.restoreLastRefreshTime(lastRefreshTime);
        // 设置时候可以自动刷新
        refreshView.setAutoRefresh(false);
    }

    private static class MyHandler extends Handler {
        private final WeakReference<Activity> mActivity;

        public MyHandler(SearchResultActivity activity) {
            mActivity = new WeakReference<Activity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            int flag = msg.what;
            switch (flag) {
                case CommonConstants.FLAG_GET_SEARCH_RESULT_SHOW:
                    //加载查询用户列表
                    ((SearchResultActivity) mActivity.get()).showListVerfy();
                    break;
                default:
                    break;
            }
        }
    }

    private MyHandler myHandler = new MyHandler(SearchResultActivity.this);

    /**
     * 获取联系人列表
     */
    private void showListVerfy() {
        SearchResultListAdapter mAdapter = new SearchResultListAdapter();
        mListView.setAdapter(mAdapter);
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchResultActivity.this.finish();
            }
        });

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String search = mEtSearch.getText().toString();
                    if (search == null || search.equals("")) {
                        Toast.makeText(SearchResultActivity.this, "您未填写优时号或手机号", Toast.LENGTH_SHORT).show();
                        mEtSearch.setText("");
                        mEtSearch.setFocusable(true);
                        return false;
                    }
                    getSearchResultFromNet();
                    return true;
                }
                return false;
            }
        });
//        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent _intent = null;
//                switch (position) {
//                    case 0:
//                        _intent = new Intent(SearchResultActivity.this, PhoneContactsActivity.class);
//                        startActivity(_intent);
//                        break;
//                    case 1:
//                        break;
//                    default:
//                        break;
//                }
//            }
//        });

        refreshView.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {

            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshView.stopRefresh();
                        lastRefreshTime = refreshView.getLastRefreshTime();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        refreshView.stopLoadMore();
                    }
                }, 2000);
            }

            @Override
            public void onRelease(float direction) {
                super.onRelease(direction);
                if (direction > 0) {
                    Toast.makeText(SearchResultActivity.this, "下拉", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SearchResultActivity.this, "上拉", Toast.LENGTH_SHORT).show();
                }
            }
        });
        refreshView.setOnAbsListViewScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                LogUtils.i("onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                LogUtils.i("onScroll");
            }
        });
    }

    /**
     * 通过忧时账号\手机号查询用户
     */
    private void getSearchResultFromNet() {
        //弹出加载进度条
        mProgressDialog = ProgressDialog.show(SearchResultActivity.this, "请稍等", "正在获取中...", true, true);
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.SearchUser);
        //包装请求参数
        String search = mEtSearch.getText().toString();
        String code = CommonUtil.getUserInfo(SearchResultActivity.this).getLoginCode();
        String _req_json = "{\"Key\":\"" + search + "\",\"PageSize\":\"" + 10 + "\",\"PageIndex\":\"" + 0 + "\"}";
        Log.d("zzzzzzzzzzzzzzzzzz", "----result:" + _req_json);
        requestParams.addBodyParameter("", _req_json);//用户名
        requestParams.addHeader("sVerifyCode", code);//头信息
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    try {
                        Log.d("zzzzzzzzzzzzzzzzzz", "----result:" + result);
                        JSONObject _json_result = new JSONObject(result);
                        Boolean _success = (Boolean) _json_result.get("Success");
                        if (_success) {
                            //加载查询结果
                            JSONArray datas = _json_result.getJSONArray("Datas");
                            Log.d("zzzzzzzzzzzzzzzzzz", "----result:" + datas.toString());
                            for (int i = 0; i < datas.length(); i++) {
                                SearchResult item = new SearchResult();
                                JSONObject jobj = datas.getJSONObject(i);
                                String phone = jobj.getString("MobileNumber");
                                item.setMobileNumber(phone);
                                String openFireUserName = jobj.getString("OpenFireUserName");
                                item.setOpenFireUserName(openFireUserName);
                                item.setName("小明" + i);
                                searchResult.add(item);
                            }
                            myHandler.sendEmptyMessage(CommonConstants.FLAG_GET_SEARCH_RESULT_SHOW);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    public class SearchResultListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return searchResult.size() != 0 ? searchResult.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return searchResult.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_list_search_result, null);
            }
            ImageView ivSearchHeadItem = (ImageView) convertView.findViewById(R.id.iv_search_result_head);
            ivSearchHeadItem.setImageResource(R.drawable.maillist);
            TextView tvSearchNameItem = (TextView) convertView.findViewById(R.id.tv_search_result_name);
            tvSearchNameItem.setText(searchResult.get(position).getName());
            return convertView;
        }
    }
}
