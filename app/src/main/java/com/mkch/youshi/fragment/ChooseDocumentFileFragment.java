package com.mkch.youshi.fragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.mkch.youshi.R;
import com.mkch.youshi.activity.DropBoxFileActivity;
import com.mkch.youshi.adapter.YoupanFileAdapter;
import com.mkch.youshi.bean.CloudFileBean;
import com.mkch.youshi.config.CommonConstants;
import com.mkch.youshi.model.YoupanFile;
import com.mkch.youshi.util.CommonUtil;
import com.mkch.youshi.util.DBHelper;
import com.mkch.youshi.util.IntentUtil;
import com.mkch.youshi.util.StringUtils;
import com.mkch.youshi.util.UIUtils;
import com.mkch.youshi.util.XUtil;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChooseDocumentFileFragment extends Fragment {

    private ArrayList<YoupanFile> mYoupanFiles;
    private YoupanFileAdapter mAdapter;
    private SwipeRefreshLayout mSRLayout;
    private ListView mListView;
    private int mType;
    private int mPageIndex = 0;
    private ProgressDialog mProgressDialog;

    public ChooseDocumentFileFragment(int mType) {
        this.mType = mType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_document_file, container, false);
        findView(view);
        return view;
    }

    private void findView(View view) {
        mSRLayout = (SwipeRefreshLayout) view.findViewById(R.id.srlayout_file);
        mSRLayout.setColorSchemeColors(R.color.common_topbar_bg_color);
        mListView = (ListView) view.findViewById(R.id.list_document_file);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
        YoupanFileAdapter.mChooseFile.clear();
        YoupanFileAdapter.mChooseNum = 0;
        DropBoxFileActivity activity = (DropBoxFileActivity) getActivity();
        activity.getmTvChoofileNum().setText("已选择");
        initData();
    }

    private void initData() {
        //文档类型
        mYoupanFiles = DBHelper.findYoupanFile(mType);
        if (mYoupanFiles != null && getActivity() != null) {
            DropBoxFileActivity activity = (DropBoxFileActivity) getActivity();
            mAdapter = new YoupanFileAdapter(mYoupanFiles, activity.getmTvChoofileNum());//拿到activity
            mListView.setAdapter(mAdapter);
        }
    }

    private void setListener() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (YoupanFileAdapter.mChooseNum != 0) {//当有选择的时候,屏蔽listView的选择事件
                    return;
                }
                mYoupanFiles = DBHelper.findYoupanFile(mType);//更新数据
                YoupanFile youpanFile = mYoupanFiles.get(position);
                String local_address = youpanFile.getLocal_address();
                //判断本地文件是否存在
                if (new File(local_address).exists()) {//存在此文件直接打开
                    UIUtils.showTip(local_address);
                    Intent intent = null;
                    intent = IntentUtil.getRightIntent(youpanFile.getSuf(), local_address);
                    try {
                        startActivity(intent);
                    } catch (Exception e) {
                        UIUtils.showTip("暂不支持打开此类型文件");
                    }
                } else { //下载文件,并更新数据库的地址
                    buildAlertDialog_progress();//下载对话框
                    CommonUtil.makeDri();//创建文件夹,保存用户下载的文件
                    XUtil.downLoadFile(youpanFile, CommonConstants.YOU_PAN_PIC_PATH +
                            youpanFile.getName(), mProgressDialog);
                }
            }
        });

        //长点击收藏
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (YoupanFileAdapter.mChooseNum != 0) {//当有选择的时候,屏蔽listView的选择事件
                    return true;
                }
                showOptionDialog(position);
                return true;
            }
        });

        //文件的刷新操作
        mSRLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //得到这个文件类型的所有数据
                getFileFormNET();
            }
        });
    }

    //将一个文件复制到另外一个文件夹中
    private void copyFile(String file1, String file2) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file1);
            BufferedInputStream bufis = new BufferedInputStream(fis);
            FileOutputStream fos = new FileOutputStream(file2);
            BufferedOutputStream bufos = new BufferedOutputStream(fos);
            int len = 0;
            while ((len = bufis.read()) != -1) {
                bufos.write(len);
            }
            bufis.close();
            bufos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //收藏文件的选择对话框
    private void showOptionDialog(final int position) {
        final String[] items = {"收    藏"};
        AlertDialog.Builder listDialog =
                new AlertDialog.Builder(getActivity());
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    mYoupanFiles = DBHelper.findYoupanFile(mType);//更新数据
                    YoupanFile youpanFile = mYoupanFiles.get(position);
                    String local_address = youpanFile.getLocal_address();
                    //判断本地文件是否存在
                    if (new File(local_address).exists() && !new File(CommonConstants.COLLECT_PATH + youpanFile.getName()).exists()) {//存在此文件直接收藏
                        DBHelper.saveCollectFile(youpanFile, CommonConstants.COLLECT_PATH + youpanFile.getName());
                        //储存一个文件到指定文件夹
                        CommonUtil.makeCollectDri();
                        copyFile(local_address, CommonConstants.COLLECT_PATH + youpanFile.getName());
                        UIUtils.showTip("已收藏该文件");
                    } else if (!new File(local_address).exists() && !new File(CommonConstants.COLLECT_PATH + youpanFile.getName()).exists()) {
                        UIUtils.showTip("请先下载该文件");
                    } else if (new File(CommonConstants.COLLECT_PATH + youpanFile.getName()).exists()) {
                        UIUtils.showTip("该文件已经收藏");
                    }
                }
            }
        });
        listDialog.show();
    }

    /**
     * 从网络端获取文件
     */
    private void getFileFormNET() {
        //弹出加载进度条
//        mProgressDialog = ProgressDialog.show(getActivity(), "请稍等", "正在...", true, true);
        //使用xutils3访问网络并获取返回值
        RequestParams requestParams = new RequestParams(CommonConstants.GetCloudFile);
        //包装请求参数
        String _personEventJson = createGetFileJson();
        Log.d("YZP", "---------------------_personEventJson = " + _personEventJson);

        requestParams.addBodyParameter("", _personEventJson);//用户名
        String loginCode = CommonUtil.getUserInfo(UIUtils.getContext()).getLoginCode();
        if (loginCode != null)
            requestParams.addHeader("sVerifyCode", loginCode);//头信息

        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    UIUtils.LogUtils(result);
                    CloudFileBean cloudFileBean = AnalyticJson(result);
                    List<CloudFileBean.DatasBean> fileDatas = cloudFileBean.getDatas();
                    for (int i = 0; i < fileDatas.size(); i++) {
                        //拿到fileID去查找数据库里面的数据,没有则进行添加
                        ArrayList<YoupanFile> files = DBHelper.findFile(fileDatas.get(i).getFileID() + "");
                        if (files != null && files.size() != 0) {
                            UIUtils.LogUtils("跳过了这个");
                        } else {//没有此文件则添加此文件
                            UIUtils.LogUtils("添加了这个文件");
                            DBHelper.saveFile(fileDatas.get(i));
                        }
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("jlj", "-------onError = " + ex.getMessage());
                //使用handler通知UI提示用户错误信息
                if (ex instanceof ConnectException) {
                    UIUtils.showTip(CommonConstants.MSG_CONNECT_ERROR);
                } else if (ex instanceof ConnectTimeoutException) {
                    UIUtils.showTip(CommonConstants.MSG_CONNECT_TIMEOUT);
                } else if (ex instanceof SocketTimeoutException) {
                    UIUtils.showTip(CommonConstants.MSG_SERVER_TIMEOUT);
                } else {
                    UIUtils.showTip(CommonConstants.MSG_DATA_EXCEPTION);
                }
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Log.d("userLogin", "----onCancelled");
            }

            @Override
            public void onFinished() {
                Log.d("userLogin", "----onFinished");
                mSRLayout.setRefreshing(false);
                initData();
            }
        });
    }

    //创建上传的JSON
    private String createGetFileJson() {
        //封装请求参数
        JSONObject _json_args = null;
        String _DatastoString = "";
        try {
            _json_args = new JSONObject();
            _json_args.put("type", mType);
            _json_args.put("PageSize", Integer.MAX_VALUE);
            _json_args.put("PageIndex", mPageIndex);
            _DatastoString = _json_args.toString();
            UIUtils.LogUtils(_DatastoString);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return _DatastoString;

    }

    private CloudFileBean AnalyticJson(String json) {
        Gson gson = new Gson();
        CloudFileBean cloudFileBean = gson.fromJson(json, CloudFileBean.class);
        return cloudFileBean;
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("确认下载?");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     * 进度对话框
     */
    private void buildAlertDialog_progress() {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("正在下载...........");
        /**进度条样式 */
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "后台下载", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mProgressDialog.dismiss();
            }
        });
        /**模糊效果 */
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
    }
}
