package com.mkch.youshi.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.location.Poi;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.route.SuggestAddrInfo;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.mkch.youshi.R;
import com.mkch.youshi.util.UIUtils;

import java.util.List;

/**
 * Created by Smith on 2016/10/11.
 */

public class AddressAdapter extends BaseAdapter {
    List<Poi> list;
    List<PoiInfo> InfoList;
    List<SuggestionResult.SuggestionInfo> mSuggestList;
    boolean isSuggest;

    public AddressAdapter(List<Poi> list) {
        this.list = list;
    }

    public AddressAdapter(List<SuggestionResult.SuggestionInfo> SuggestList,boolean isSuggest) {
        this.mSuggestList = SuggestList;
        this.isSuggest = isSuggest;
    }


    @Override
    public int getCount() {
        if (isSuggest){
            return mSuggestList.size();
        }else {
            return list.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if (isSuggest){
            return mSuggestList.get(position);
        }else {

            return list.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View AddressView = UIUtils.inflate(R.layout.address_item);
        TextView tvAddress = (TextView) AddressView.findViewById(R.id.tv_address);

        if (isSuggest){
            tvAddress.setText(mSuggestList.get(position).city
                    +mSuggestList.get(position).district+mSuggestList.get(position).key);
        }else {
            tvAddress.setText(list.get(position).getName());
        }
        return AddressView;
    }
}
