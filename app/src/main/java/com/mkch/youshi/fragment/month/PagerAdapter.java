package com.mkch.youshi.fragment.month;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Created by Smith on 2016/8/12.
 */
public class PagerAdapter<V extends GridView> extends android.support.v4.view.PagerAdapter {
    private V[] views;

    /**
     * @param views
     */
    public PagerAdapter(V[] views) {
        super();
        this.views = views;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

//        if (((ViewPager) container).getChildCount() == views.length) {
//            ((ViewPager) container).removeView(views[position % views.length]);
//
//        }
        ((ViewPager) container).removeView(views[position % views.length]);
        ((ViewPager) container).addView(views[position % views.length], 0);
        return views[position % views.length];
    }


    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) container);
    }

    public V[] getAllItems() {
        return views;
    }
}
