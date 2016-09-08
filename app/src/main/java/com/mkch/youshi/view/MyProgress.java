package com.mkch.youshi.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.mkch.youshi.R;


/**
 * Created by Smith on 2016/9/2.
 */
public class MyProgress extends LinearLayout {

    private View circle1;
    private View circle2;
    private View circle3;
    private View circle4;
    private View circle5;
    private View circle6;
    private View line_1;
    private View line_2;
    private View line_3;
    private View line_4;
    private View line_5;

    public MyProgress(Context context) {
        super(context);
        initView(context);
    }

    public MyProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }


    private void initView(Context context) {
        View v = View.inflate(context, R.layout.my_progress, null);
        circle1 = v.findViewById(R.id.circle1);
        circle2 = v.findViewById(R.id.circle2);
        circle3 = v.findViewById(R.id.circle3);
        circle4 = v.findViewById(R.id.circle4);
        circle5 = v.findViewById(R.id.circle5);
        circle6 = v.findViewById(R.id.circle6);
        line_1 = v.findViewById(R.id.line_1);
        line_2 = v.findViewById(R.id.line_2);
        line_3 = v.findViewById(R.id.line_3);
        line_4 = v.findViewById(R.id.line_4);
        line_5 = v.findViewById(R.id.line_5);
        addView(v);
    }

    /**
     * 设置一周有几次
     */
    public void setNumber(int number) {
        switch (number) {
            case 1:
                circle1.setVisibility(VISIBLE);
                break;
            case 2:
                circle1.setVisibility(VISIBLE);
                line_1.setVisibility(VISIBLE);
                circle2.setVisibility(VISIBLE);
                break;
            case 3:
                circle1.setVisibility(VISIBLE);
                line_1.setVisibility(VISIBLE);
                circle2.setVisibility(VISIBLE);
                line_2.setVisibility(VISIBLE);
                circle3.setVisibility(VISIBLE);
                break;
            case 4:
                circle1.setVisibility(VISIBLE);
                line_1.setVisibility(VISIBLE);
                circle2.setVisibility(VISIBLE);
                line_2.setVisibility(VISIBLE);
                circle3.setVisibility(VISIBLE);
                line_3.setVisibility(VISIBLE);
                circle4.setVisibility(VISIBLE);
                break;
            case 5:
                circle1.setVisibility(VISIBLE);
                line_1.setVisibility(VISIBLE);
                circle2.setVisibility(VISIBLE);
                line_2.setVisibility(VISIBLE);
                circle3.setVisibility(VISIBLE);
                line_3.setVisibility(VISIBLE);
                circle4.setVisibility(VISIBLE);
                line_4.setVisibility(VISIBLE);
                circle5.setVisibility(VISIBLE);
                break;
            case 6:
                circle1.setVisibility(VISIBLE);
                line_1.setVisibility(VISIBLE);
                circle2.setVisibility(VISIBLE);
                line_2.setVisibility(VISIBLE);
                circle3.setVisibility(VISIBLE);
                line_3.setVisibility(VISIBLE);
                circle4.setVisibility(VISIBLE);
                line_4.setVisibility(VISIBLE);
                circle5.setVisibility(VISIBLE);
                line_5.setVisibility(VISIBLE);
                circle6.setVisibility(VISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 设置已经完成了几次
     */
    public void setCompleteNumber(int completeNumber) {
        switch (completeNumber){
            case 1:
                circle1.setBackgroundResource(R.drawable.shape_circle_bg);
                break;
            case 2:
                circle1.setBackgroundResource(R.drawable.shape_circle_bg);
                circle2.setBackgroundResource(R.drawable.shape_circle_bg);
                break;
            case 3:
                circle1.setBackgroundResource(R.drawable.shape_circle_bg);
                circle2.setBackgroundResource(R.drawable.shape_circle_bg);
                circle3.setBackgroundResource(R.drawable.shape_circle_bg);
                break;
            case 4:
                circle1.setBackgroundResource(R.drawable.shape_circle_bg);
                circle2.setBackgroundResource(R.drawable.shape_circle_bg);
                circle3.setBackgroundResource(R.drawable.shape_circle_bg);
                circle4.setBackgroundResource(R.drawable.shape_circle_bg);
                break;
            case 5:
                circle1.setBackgroundResource(R.drawable.shape_circle_bg);
                circle2.setBackgroundResource(R.drawable.shape_circle_bg);
                circle3.setBackgroundResource(R.drawable.shape_circle_bg);
                circle4.setBackgroundResource(R.drawable.shape_circle_bg);
                circle5.setBackgroundResource(R.drawable.shape_circle_bg);
                break;
            case 6:
                circle1.setBackgroundResource(R.drawable.shape_circle_bg);
                circle2.setBackgroundResource(R.drawable.shape_circle_bg);
                circle3.setBackgroundResource(R.drawable.shape_circle_bg);
                circle4.setBackgroundResource(R.drawable.shape_circle_bg);
                circle5.setBackgroundResource(R.drawable.shape_circle_bg);
                circle6.setBackgroundResource(R.drawable.shape_circle_bg);
                break;

        }
    }


}
