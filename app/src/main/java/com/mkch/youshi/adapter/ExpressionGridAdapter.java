package com.mkch.youshi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.mkch.youshi.R;

/**
 * 表情列表
 * Created by ZJ on 2016/10/27.
 */
public class ExpressionGridAdapter extends BaseAdapter {
    private int[] mExpressions = new int[]{};
    private LayoutInflater mInflater;
    private Context mContext;

    public ExpressionGridAdapter(Context context, int[] expressions) {
        mContext = context;
        mExpressions = expressions;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mExpressions.length != 0 ? mExpressions.length : 0;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_grid_expression, null);
        }
        ImageView ivItemExpression = (ImageView) convertView.findViewById(R.id.iv_item_grid_expression);
        ivItemExpression.setImageResource(mExpressions[position]);
        return convertView;
    }
}
