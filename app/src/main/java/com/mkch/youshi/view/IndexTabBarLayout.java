package com.mkch.youshi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mkch.youshi.R;


/**
 * 自定义控件-首页底部tabbar切换fragment用
 */
public class IndexTabBarLayout extends LinearLayout {
	//tabbar的回调接口
	public interface IIndexTabBarCallBackListener{
		public void clickItem(int id);//按了某一项后
	}
	IIndexTabBarCallBackListener indexTabBarCallBackListener=null;
	public void setOnItemClickListener(IIndexTabBarCallBackListener indexTabBarCallBackListener){
		this.indexTabBarCallBackListener = indexTabBarCallBackListener;
	}
	
	RelativeLayout mTodayLayout;
	RelativeLayout mMessageLayout;
	RelativeLayout mContactLayout;
	RelativeLayout mPsCenterLayout;
	
	LayoutInflater inflater;
	
	public final static int FLAG_TODAY = 0;
    public final static int FLAG_MESSAGE = 1;
    public final static int FLAG_CONTACT = 2;
    public final static int FLAG_PSCENTER = 3;
	
	public IndexTabBarLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	private void initView() {
		inflater = LayoutInflater.from(getContext());
		View view = inflater.inflate(R.layout.layout_index_tabbar, this);
		findView(view);
		initData();
		setListener();
	}
	
	/**
	 * 找到所有layout控件
	 * @param view
	 */
	private void findView(View view) {
		mTodayLayout = (RelativeLayout)view.findViewById(R.id.index_today_item);
		mMessageLayout = (RelativeLayout)view.findViewById(R.id.index_message_item);
		mContactLayout = (RelativeLayout)view.findViewById(R.id.index_contact_item);
		mPsCenterLayout = (RelativeLayout)view.findViewById(R.id.index_pscenter_item);
		
	}

	/**
	 * 初始化底部每个item的数据
	 */
	private void initData() {
		//今日
		mTodayLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_today_pressed);
		TextView _tvHome = (TextView) mTodayLayout.findViewById(R.id.tv_index_tab_item_title);
		_tvHome.setText("今日");
		_tvHome.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_sel));
		//消息
		mMessageLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_message_normal);
		TextView _tvCates = (TextView) mMessageLayout.findViewById(R.id.tv_index_tab_item_title);
		_tvCates.setText("消息");
		_tvCates.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
		//联系人
		mContactLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_contact_normal);
		TextView _tvCitizens = (TextView) mContactLayout.findViewById(R.id.tv_index_tab_item_title);
		_tvCitizens.setText("联系人");
		_tvCitizens.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
		//我
		mPsCenterLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_me_normal);
		TextView _tvPsCenter = (TextView)mPsCenterLayout.findViewById(R.id.tv_index_tab_item_title);
		_tvPsCenter.setText("我");
		_tvPsCenter.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
	}
	
	/**
	 * 点击事件监听
	 */
	private void setListener() {
		mTodayLayout.setOnClickListener(new MyItemClickListener());
		mMessageLayout.setOnClickListener(new MyItemClickListener());
		mContactLayout.setOnClickListener(new MyItemClickListener());
		mPsCenterLayout.setOnClickListener(new MyItemClickListener());
	}
	
	private class MyItemClickListener implements OnClickListener{

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.index_today_item:
				//1-改变图片;改变文字的颜色;改变layout的背景
				changeTabBarItems(FLAG_TODAY);
				//2-实现页面的切换
				break;
			case R.id.index_message_item:
				changeTabBarItems(FLAG_MESSAGE);
				break;
			case R.id.index_contact_item:
				changeTabBarItems(FLAG_CONTACT);
				break;
			case R.id.index_pscenter_item:
				changeTabBarItems(FLAG_PSCENTER);
				break;
			}
			if(indexTabBarCallBackListener!=null){
				indexTabBarCallBackListener.clickItem(view.getId());
			}
		}

		
		
	}
	
	/**
	 * 点击某个tabitem，切换以下内容：改变图片;改变文字的颜色;改变layout的背景
	 * @param index 索引值
	 */
	public void changeTabBarItems(int index) {
		TextView _tvToday;
		TextView _tvMessage;
		TextView _tvContact;
		TextView _tvPsCenter;
		
		switch (index) {
		case FLAG_TODAY:
			//今日
			mTodayLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_today_pressed);
			_tvToday = (TextView) mTodayLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvToday.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_sel));
			//消息
			mMessageLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_message_normal);
			_tvMessage = (TextView) mMessageLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvMessage.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
			//联系人
			mContactLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_contact_normal);
			_tvContact = (TextView) mContactLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvContact.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
			//我
			mPsCenterLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_me_normal);
			_tvPsCenter = (TextView)mPsCenterLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvPsCenter.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
			break;
		case FLAG_MESSAGE:
			//今日
			mTodayLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_today_normal);
			_tvToday = (TextView) mTodayLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvToday.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
			
			//消息
			mMessageLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_message_pressed);
			_tvMessage = (TextView) mMessageLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvMessage.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_sel));
			
			//联系人
			mContactLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_contact_normal);
			_tvContact = (TextView) mContactLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvContact.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
			
			//我
			mPsCenterLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_me_normal);
			_tvPsCenter = (TextView)mPsCenterLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvPsCenter.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
			
			break;
		case FLAG_CONTACT:
			//今日
			mTodayLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_today_normal);
			_tvToday = (TextView) mTodayLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvToday.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
			
			//消息
			mMessageLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_message_normal);
			_tvMessage = (TextView) mMessageLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvMessage.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
			//联系人
			mContactLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_contact_pressed);
			_tvContact = (TextView) mContactLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvContact.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_sel));
			//我
			mPsCenterLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_me_normal);
			_tvPsCenter = (TextView)mPsCenterLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvPsCenter.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
			break;
		case FLAG_PSCENTER:
			//今日
			mTodayLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_today_normal);
			_tvToday = (TextView) mTodayLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvToday.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
			
			//消息
			mMessageLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_message_normal);
			_tvMessage = (TextView) mMessageLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvMessage.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
			//联系人
			mContactLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_contact_normal);
			_tvContact = (TextView) mContactLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvContact.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_unsel));
			//我
			mPsCenterLayout.findViewById(R.id.iv_index_tab_item_pic).setBackgroundResource(R.drawable.tab_me_pressed);
			_tvPsCenter = (TextView)mPsCenterLayout.findViewById(R.id.tv_index_tab_item_title);
			_tvPsCenter.setTextColor(getResources().getColor(R.color.main_tabbar_item_text_font_color_sel));
			break;
		}
	}
	
}
