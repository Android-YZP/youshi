package com.mkch.youshi.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.mkch.youshi.R;

public class SearchResultActivity extends Activity {

    private ImageView mIvBack;
    private ListView mListView;
    private EditText mEtSearch;

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
        mEtSearch = (EditText) findViewById(R.id.et_add_friends_search);
//        mListView = (ListView) findViewById(R.id.list_add_friends);
        mEtSearch.setFocusable(true);
    }

    private void initData() {
//        ListAdapter mAdapter = new AddFriendsMethodsListAdapter(SearchResultActivity.this);
//        mListView.setAdapter(mAdapter);
    }

    private void setListener() {
        mIvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchResultActivity.this.finish();
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
    }
}
