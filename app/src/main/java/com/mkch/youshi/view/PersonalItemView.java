package com.mkch.youshi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mkch.youshi.R;
import com.mkch.youshi.util.UIUtils;

/**
 * Created by Smith on 2016/10/12.
 * 自定义个人日程条目控件
 */

public class PersonalItemView extends FrameLayout {
    public final int PERSONAL_EVENT = 0;
    public final int PERSONAL_AFFAIR = 1;
    public final int PERSONAL_HABIT = 2;
    private CheckBox mIsComplete;
    private TextView mTvTheme;
    private ProgressBar mPbProgress;
    private MyProgress mMbProgress;


    private TextView mTvTimeStop;
    private TextView mTvProgress;
    private TextView mTvTimeAndStopTime;

    public PersonalItemView(Context context) {
        super(context);
        initView();
    }

    public PersonalItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PersonalItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = UIUtils.inflate(R.layout.item_list_personal_calendar);
        mIsComplete = (CheckBox) view.findViewById(R.id.cb_complete);
        mTvTheme = (TextView) view.findViewById(R.id.tv_lv_theme);
        mPbProgress = (ProgressBar) view.findViewById(R.id.pb_progress);
        mMbProgress = (MyProgress) view.findViewById(R.id.mp_progress);
        mTvTimeStop = (TextView) view.findViewById(R.id.tv_time_stop);
        mTvProgress = (TextView) view.findViewById(R.id.tv_progress);
        mTvTimeAndStopTime = (TextView) view.findViewById(R.id.tv_time_and_stop_time);
        addView(view);
    }

    /**
     * 设置当前日程是哪种类型
     */
    public void setType(int type, int habitTimes) {
        if (type == PERSONAL_EVENT) {
            mTvTimeStop.setVisibility(View.VISIBLE);
            mPbProgress.setVisibility(View.INVISIBLE);
            mMbProgress.setVisibility(View.INVISIBLE);
            mTvProgress.setVisibility(View.INVISIBLE);
            mTvTimeAndStopTime.setVisibility(View.INVISIBLE);
        } else if (type == PERSONAL_AFFAIR) {
            mPbProgress.setVisibility(View.GONE);
            mTvTimeStop.setVisibility(View.GONE);
            mPbProgress.setVisibility(View.VISIBLE);
            mTvProgress.setVisibility(View.VISIBLE);
            mTvTimeAndStopTime.setVisibility(View.VISIBLE);
        } else if (type == PERSONAL_HABIT) {
            mTvTimeAndStopTime.setVisibility(View.VISIBLE);
            mPbProgress.setVisibility(View.GONE);
            mTvProgress.setVisibility(View.GONE);
            if (habitTimes == 7) {
                mMbProgress.setVisibility(View.GONE);
                mTvTimeStop.setVisibility(View.VISIBLE);
            } else {
                mMbProgress.setVisibility(View.VISIBLE);
                mTvTimeStop.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置标题
     *
     * @param theme 标题
     */
    public void setTheme(String theme) {
        mTvTheme.setText(theme);//设置主题
    }

    /**
     * 设置当前日程是否需要打钩
     */
    public void setCompleted(boolean isCompleted) {
        mIsComplete.setChecked(isCompleted);//设置是否完成
    }

    /**
     * 设置个人事件的截止日期
     *
     * @param endTime 截止日期
     */
    public void setPerEveProgress(String endTime) {
        //设置进度显示
        mTvTimeStop.setText(endTime + "截止");
    }

    /**
     * 个人事务的时间进度
     */
    public void setPerAffProgress(int progress) {
        mPbProgress.setSecondaryProgress(progress);
        mPbProgress.setProgress(progress - 3);
        mTvProgress.setText(progress + "%");
    }

    /**
     * 设置个人习惯的进度
     *
     * @param CompleteTimes   当前完成次数
     * @param totalHabitTimes 一周的总次数
     */
    public void setPerHabProgress(int CompleteTimes, int totalHabitTimes) {
        if (totalHabitTimes < 7) {
            mMbProgress.setNumber(totalHabitTimes);
            mMbProgress.setCompleteNumber(CompleteTimes);
        } else {
            mTvTimeStop.setText(CompleteTimes + "/" + totalHabitTimes);
        }
        mTvTimeAndStopTime.setText("一周" + totalHabitTimes + "次");
    }
    /**
     * 设置个人事务的结束时间和总时长
     */
    public void setPerAffStopTime(String StopTime, String totalTime) {
        mTvTimeAndStopTime.setText(StopTime+"截止"+"  时长"+totalTime);
    }


}



