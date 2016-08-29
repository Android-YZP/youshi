package com.mkch.youshi.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.adapter.ContactAdapter;
import com.mkch.youshi.bean.Contact;
import com.mkch.youshi.view.HanziToPinyin;
import com.mkch.youshi.view.SideBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.kymjs.kjframe.KJActivity;
import org.kymjs.kjframe.KJHttp;
import org.kymjs.kjframe.http.HttpCallBack;
import org.kymjs.kjframe.http.HttpConfig;
import org.kymjs.kjframe.ui.BindView;
import org.kymjs.kjframe.utils.KJLoger;

import java.util.ArrayList;

/**
 * 用户联系人列表
 *
 * @author kymjs (http://www.kymjs.com/) on 7/24/15.
 */
public class TestMsgActivity extends KJActivity implements SideBar
        .OnTouchingLetterChangedListener, TextWatcher {

    @BindView(id = R.id.list_contacts)
    private ListView mListView;
    private TextView mFooterView;

    private KJHttp kjh = null;
    private ArrayList<Contact> datas = new ArrayList<>();
    private ContactAdapter mAdapter;

    @Override
    public void setRootView() {
        setContentView(R.layout.fragment_contacts);
    }

    @Override
    public void initData() {
        super.initData();
        HttpConfig config = new HttpConfig();
        HttpConfig.sCookie = "oscid=V" +
                "%2BbmxZFR8UfmpvrBHAcVRKALrd72iPWknXaWDa5Is3veK7WsxTSWY80kRXB1LoH%2F4VJ" +
                "%2F9s7K3Kd9CwCC1CAV%2BnJ7T3ka0blF8vZojozhUdOYkq6D6Laldg%3D%3D; Domain=.oschina" +
                ".net; Path=/; ";
        config.cacheTime = 0;
        kjh = new KJHttp();
    }

    @Override
    public void initWidget() {
        super.initWidget();
        SideBar mSideBar = (SideBar) findViewById(R.id.sidebar_contacts);
        TextView mDialog = (TextView) findViewById(R.id.dialog_contacts);
        EditText mSearchInput = (EditText) findViewById(R.id.search_input_contacts);

        mSideBar.setTextView(mDialog);
        mSideBar.setOnTouchingLetterChangedListener(this);
        mSearchInput.addTextChangedListener(this);

        // 给listView设置adapter
        mFooterView = (TextView) View.inflate(aty, R.layout.item_list_contact_count, null);
        mListView.addFooterView(mFooterView);

        doHttp();
    }

    private void doHttp() {
        kjh.get("http://zb.oschina.net/action/zbApi/contacts_list?uid=863548", new HttpCallBack() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                parser(t);
            }
        });
    }

    private void parser(String json) {
        try {
            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.optJSONObject(i);
                Contact data = new Contact();
                data.setName(object.optString("name"));
                Log.d("_user_name", object.optString("name"));
                data.setUrl(object.optString("portrait"));
                Log.d("_user_url", object.optString("portrait"));
                data.setId(object.optInt("id"));
                Log.d("_user_id", String.valueOf(object.optInt("id")));
                data.setPinyin(HanziToPinyin.getPinYin(data.getName()));
                Log.d("_user_pinyin", HanziToPinyin.getPinYin(data.getName()));
                datas.add(data);
            }
            mFooterView.setText(datas.size() + "位联系人");
            mAdapter = new ContactAdapter(mListView, datas);
            mListView.setAdapter(mAdapter);
        } catch (JSONException e) {
            KJLoger.debug("解析异常" + e.getMessage());
        }
    }

    /**
     *当点击拼音首字母改变时
     * @param s
     */
    @Override
    public void onTouchingLetterChanged(String s) {
        int position = 0;
        // 该字母首次出现的位置
        if (mAdapter != null) {
            position = mAdapter.getPositionForSection(s.charAt(0));
        }
        if (position != -1) {
            mListView.setSelection(position);
        } else if (s.contains("#")) {
            mListView.setSelection(0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        ArrayList<Contact> temp = new ArrayList<>(datas);
        for (Contact data : datas) {
            if (data.getName().contains(s) || data.getPinyin().contains(s)) {
            } else {
                temp.remove(data);
            }
        }
        if (mAdapter != null) {
            mAdapter.refresh(temp);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}
