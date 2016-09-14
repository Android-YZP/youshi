package com.mkch.youshi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.ArcBean;

import java.util.List;

/**
 * Created by SunnyJiang on 2016/8/8.
 */
public class DayLineView extends View{

    public static final int HOUR_TEXT_SIZE = 30;
    public static final int HOUR_CHOOSE_HEIGHT = 30;
    public static final int STROKE_WIDTH=40;
    private Paint mPaint;
    private Paint mPaint2;

    private int width;//默认宽度
    private int height;//默认高度
    private float _kedu_long;//长刻度的长度
    private List<ArcBean> mArcBeans;
    public DayLineView(Context context,List<ArcBean> pArcBeans) {
        super(context);
        this.mArcBeans = pArcBeans;
        //新建画笔
        mPaint = new Paint();

        mPaint2 = new Paint();

        mPaint2.setColor(getResources().getColor(R.color.clock_kedu));//设置颜色
//        mPaint2.setStrokeWidth(10);//设置线宽
        mPaint2.setStrokeWidth(10);//设置线宽
        mPaint2.setTextAlign(Paint.Align.CENTER);
        mPaint2.setTextSize(HOUR_TEXT_SIZE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

//        Log.d("jlj","DayLineView-onMeasure------------------------------------"+width+","+height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        Log.d("jlj","DayLineView-onDraw------------------------------------");
        setPaintStyle(getResources().getColor(R.color.clock_kedu),3);
        //小时的高度
        float _hour_height = HOUR_TEXT_SIZE;
        //左起：起始位置x坐标，起始位置y坐标，终止位置x坐标，终止位置y坐标，画笔(一个Paint对象)
        canvas.drawLine(0f,height-_hour_height, 1440f, height-_hour_height, mPaint);//24小時*6格=144格，每格子做10

        float _kedu_short = 10f;
        _kedu_long = 20f;

        //画刻度
        for (int i=0;i<=144;i++){
            float _kedu_x = i*10;
            if (i%6==0){
                //画长的刻度
                canvas.drawLine(_kedu_x,height-_hour_height,_kedu_x,height-_hour_height-_kedu_long, mPaint);
                canvas.drawText(i/6+"",_kedu_x,height,mPaint2);
            }else{
                //画短的刻度
                canvas.drawLine(_kedu_x,height-_hour_height,_kedu_x, height-_hour_height-_kedu_short, mPaint);
            }
        }

        //画各个时间段
        drawTimeLine(canvas);

    }

    /**
     * 画各个阶段的时间线
     * @param canvas
     */
    private void drawTimeLine(Canvas canvas) {
        for (int i=0;i<mArcBeans.size();i++) {
            ArcBean _arc_bean = mArcBeans.get(i);

            //根据开始时间和结束时间算出起始角度
            String _start_time = _arc_bean.getStarttime();
            String _end_time = _arc_bean.getEndtime();

            int _overlap_line = _arc_bean.getOverlap_line();
            int _color = _arc_bean.getNotetype();//查出颜色
            switch (_color) {
                case 1:
                    //画弧线1:left/top/right/bottom
                    setPaintStyle(getResources().getColor(R.color.cycicle_color_1), STROKE_WIDTH);
                    break;
                case 2:
                    setPaintStyle(getResources().getColor(R.color.cycicle_color_2), STROKE_WIDTH);
                    break;
                case 3:
                    setPaintStyle(getResources().getColor(R.color.cycicle_color_3), STROKE_WIDTH);
                    break;
                case 4:
                    setPaintStyle(getResources().getColor(R.color.cycicle_color_4), STROKE_WIDTH);
                    break;
                case 5:
                    setPaintStyle(getResources().getColor(R.color.cycicle_color_5), STROKE_WIDTH);
                    break;
                case 6:
                    setPaintStyle(getResources().getColor(R.color.cycicle_color_6), STROKE_WIDTH);
                    break;
                default:
                    break;
            }

            //根据开始时间和结束时间，计算出x轴开始坐标和x轴结束坐标
            float _x_location = calcXLocationFromTime(_start_time);
            float _y_location = calcXLocationFromTime(_end_time);

            canvas.drawLine(_x_location,height-HOUR_TEXT_SIZE-STROKE_WIDTH*_overlap_line-_kedu_long, _y_location, height-HOUR_TEXT_SIZE-STROKE_WIDTH*_overlap_line-_kedu_long, mPaint);

            //小线条
//            setPaintStyle(Color.RED, 5);
//            canvas.drawLine(_x_location,height-HOUR_TEXT_SIZE-STROKE_WIDTH/2-_kedu_long, _y_location, height-HOUR_TEXT_SIZE-STROKE_WIDTH/2-_kedu_long, mPaint);
        }
    }

    /**
     * 根据开始时间和结束时间，计算出x轴开始坐标和x轴结束坐标
     */
    private float calcXLocationFromTime(String start_time) {
        int _hour = Integer.parseInt(start_time.substring(0,start_time.indexOf(":")));
        int _minute =  Integer.parseInt(start_time.substring(start_time.indexOf(":")+1));
        //1小时是6格，1格是10
        float _start_hour = _hour*6*10;
        //1格是10，代表10分钟，1分钟就是1，计算出x距离
        float _distance_x_minute = _minute*(10/10);
        return _start_hour+_distance_x_minute;
    }

    /**
     * 设置画笔的颜色、粗细、是否抗锯齿、绘制风格
     * @param pColor
     * @param pStrokeWidth
     */
    private void setPaintStyle(int pColor,int pStrokeWidth) {
        mPaint.setColor(pColor);//设置颜色
        mPaint.setStrokeWidth(pStrokeWidth);//设置线宽
        mPaint.setAntiAlias(true);//设置是否抗锯齿
        mPaint.setStyle(Paint.Style.STROKE);//设置绘制风格
    }
}
