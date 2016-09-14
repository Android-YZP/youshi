package com.mkch.youshi.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.mkch.youshi.R;
import com.mkch.youshi.bean.ArcBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by SunnyJiang on 2016/8/3.
 */
public class DayCircleView extends View {
    private int STROKE_WIDTH;//画笔宽度
    private int TEXT_STROKE_WIDTH;//画笔宽度
    private int mCircleRadius;//半径是300
    private Paint mPaint;//画笔
    private Paint mPaint2;//画笔

    private int width;//默认宽度
    private int height;//默认高度

    private List<ArcBean> mArcBeans;
    private HashMap<Integer,Integer> mHourAngle;

    public DayCircleView(Context context,List<ArcBean> pArcBeans) {
        super(context);
        this.mArcBeans = pArcBeans;

        //填充每个小时的起始角度
        fullHourAngle();
        //新建画笔
        mPaint = new Paint();
        mPaint2 = new Paint();

    }

    public DayCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        height = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);

        mCircleRadius = width/4;
        STROKE_WIDTH = width/30;//=20
        TEXT_STROKE_WIDTH = width/40;//15
//        Log.d("jlj","------------------------------------"+width+","+height+",radius="+mCircleRadius);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //依次旋转画布，画出每个刻度和对应数字
        setPaintStyle(getResources().getColor(R.color.clock_kedu),3);
        for (int i = 1; i <= 60; i++) {
            canvas.save();//保存当前画布
            canvas.rotate(360/60*i,width/2,height/2);

            //算出长刻度和短刻度
            int _kedu_length_long=mCircleRadius/6;//200/6
            int _kedu_length_short=mCircleRadius/15;//200/15
            if (i%5==0){
                //左起：起始位置x坐标，起始位置y坐标，终止位置x坐标，终止位置y坐标，画笔(一个Paint对象)
                canvas.drawLine(width / 2, height / 2 - mCircleRadius, width / 2, height / 2 - mCircleRadius + _kedu_length_long, mPaint);
            }else{
                canvas.drawLine(width / 2, height / 2 - mCircleRadius, width / 2, height / 2 - mCircleRadius + _kedu_length_short, mPaint);
            }
            canvas.restore();
        }
        //画出大圆
        setPaintStyle(getResources().getColor(R.color.clock_kedu), STROKE_WIDTH);
        canvas.drawCircle(width / 2, height / 2, mCircleRadius+TEXT_STROKE_WIDTH+STROKE_WIDTH, mPaint);//PM
        canvas.drawCircle(width / 2, height / 2, mCircleRadius+TEXT_STROKE_WIDTH+2*STROKE_WIDTH+5, mPaint);//AM

        //根据传入的时间段集合，画出所有的时间段弧线
        drawAllArcs(canvas);


        //左起：文本内容，起始位置x坐标，起始位置y坐标，画笔
//        setPaintStyle(getResources().getColor(R.color.clock_kedu),10);
        mPaint2.setColor(getResources().getColor(R.color.clock_kedu));//设置颜色
//        mPaint2.setStrokeWidth(10);//设置线宽
        mPaint2.setStrokeWidth(mCircleRadius/60);//设置线宽
        mPaint2.setTextAlign(Paint.Align.CENTER);
        mPaint2.setTextSize(TEXT_STROKE_WIDTH);
//        Log.d("jlj","AM--------------------"+(height / 2 - mCircleRadius-TEXT_STROKE_WIDTH-2*STROKE_WIDTH-10));
        canvas.drawText("AM", width / 2,height / 2 - mCircleRadius-TEXT_STROKE_WIDTH-2*STROKE_WIDTH-5-TEXT_STROKE_WIDTH, mPaint2);
        canvas.drawText("PM", width / 2,height / 2 - mCircleRadius-TEXT_STROKE_WIDTH, mPaint2);

    }

    /**
     * 填充每个小时的起始角度
     */
    private void fullHourAngle() {
        mHourAngle = new HashMap<>();
        int _angle = 270;
        for (int i=0;i<24;i++){
            mHourAngle.put(i,_angle);
            _angle = _angle+30;
            if (_angle==360){
                _angle = 0;
            }
        }

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


    /**
     * 画出所有的时间段弧线
     * @param canvas
     */
    private void drawAllArcs(Canvas canvas) {
        for (int i=0;i<mArcBeans.size();i++){
            ArcBean _arc_bean = mArcBeans.get(i);


            RectF _rect_f = null;
            RectF _rect_f_2 = null;
            //根据开始时间和结束时间算出起始角度
            String _start_time = _arc_bean.getStarttime();
            String _end_time = _arc_bean.getEndtime();
            int _color = _arc_bean.getNotetype();
            switch (_color){
                case 1:
                    //画弧线1:left/top/right/bottom
                    setPaintStyle(getResources().getColor(R.color.cycicle_color_1),STROKE_WIDTH);
                    break;
                case 2:
                    setPaintStyle(getResources().getColor(R.color.cycicle_color_2),STROKE_WIDTH);
                    break;
                case 3:
                    setPaintStyle(getResources().getColor(R.color.cycicle_color_3),STROKE_WIDTH);
                    break;
                case 4:
                    setPaintStyle(getResources().getColor(R.color.cycicle_color_4),STROKE_WIDTH);
                    break;
                case 5:
                    setPaintStyle(getResources().getColor(R.color.cycicle_color_5),STROKE_WIDTH);
                    break;
                case 6:
                    setPaintStyle(getResources().getColor(R.color.cycicle_color_6),STROKE_WIDTH);
                    break;
                default:
                    break;
            }

            //计算当前时间是上午还是下午，若是上午，在外圆画；若是下午，在内圆画。
            int _start_hour = Integer.parseInt(_start_time.substring(0,_start_time.indexOf(":")));
            int _end_hour = Integer.parseInt(_end_time.substring(0,_end_time.indexOf(":")));
            if (_start_hour>=12){
                //内圈
                _rect_f = new RectF(width / 2 -(mCircleRadius+TEXT_STROKE_WIDTH+STROKE_WIDTH),height / 2 - (mCircleRadius+TEXT_STROKE_WIDTH+STROKE_WIDTH),width / 2 + (mCircleRadius+TEXT_STROKE_WIDTH+STROKE_WIDTH),height/2+(mCircleRadius+TEXT_STROKE_WIDTH+STROKE_WIDTH));
                float _startAngle = calcStartAngle(_start_time);//计算开始角度
                float _totalHour = clacTimeInstance(_start_time,_end_time);//计算两个时间段经过多少小时
                float _seepAngle = 30 * _totalHour;//计算通过多少度//13:00-14：00=1小时=60分钟=30度，那就是过了多少小时*30度float _seepAngle = 360*(_total_hour/12);

                canvas.drawArc(_rect_f,_startAngle+1,_seepAngle-2,false,mPaint);//画一段弧线

                setPaintStyle(Color.WHITE,STROKE_WIDTH);//白色刷笔
                canvas.drawArc(_rect_f,_startAngle,1,false,mPaint);//画一段白色的开始间隙
                canvas.drawArc(_rect_f,_startAngle+_seepAngle-1,1,false,mPaint);//画一段白色的结束间隙


            }else if(_end_hour<=12){
                //外圈
                _rect_f = new RectF(width / 2 - (mCircleRadius+TEXT_STROKE_WIDTH+2*STROKE_WIDTH+5),height / 2 - (mCircleRadius+TEXT_STROKE_WIDTH+2*STROKE_WIDTH+5),width / 2 + (mCircleRadius+TEXT_STROKE_WIDTH+2*STROKE_WIDTH+5),height/2+(mCircleRadius+TEXT_STROKE_WIDTH+2*STROKE_WIDTH+5));
                float _startAngle = calcStartAngle(_start_time);//计算开始角度
                float _totalHour = clacTimeInstance(_start_time,_end_time);//计算两个时间段经过多少小时
                float _seepAngle = 30 * _totalHour;//计算通过多少度//13:00-14：00=1小时=60分钟=30度，那就是过了多少小时*30度float _seepAngle = 360*(_total_hour/12);
                canvas.drawArc(_rect_f,_startAngle,_seepAngle,false,mPaint);//画一段弧线

                setPaintStyle(Color.WHITE,STROKE_WIDTH);//白色刷笔
                canvas.drawArc(_rect_f,_startAngle,1,false,mPaint);//画一段白色的开始间隙
                canvas.drawArc(_rect_f,_startAngle+_seepAngle-1,1,false,mPaint);//画一段白色的结束间隙
            }else if (_start_hour<12&&_end_hour>12){

                _rect_f_2 = new RectF(width / 2 -(mCircleRadius+TEXT_STROKE_WIDTH+STROKE_WIDTH),height / 2 - (mCircleRadius+TEXT_STROKE_WIDTH+STROKE_WIDTH),width / 2 + (mCircleRadius+TEXT_STROKE_WIDTH+STROKE_WIDTH),height/2+(mCircleRadius+TEXT_STROKE_WIDTH+STROKE_WIDTH));
                _rect_f = new RectF(width / 2 - (mCircleRadius+TEXT_STROKE_WIDTH+2*STROKE_WIDTH+5),height / 2 - (mCircleRadius+TEXT_STROKE_WIDTH+2*STROKE_WIDTH+5),width / 2 + (mCircleRadius+TEXT_STROKE_WIDTH+2*STROKE_WIDTH+5),height/2+(mCircleRadius+TEXT_STROKE_WIDTH+2*STROKE_WIDTH+5));

                float _startAngle1 = calcStartAngle(_start_time);//计算开始角度
                float _totalHour1 = clacTimeInstance(_start_time,"12:00");//计算两个时间段经过多少小时
                float _seepAngle1 = 30 * _totalHour1;//计算通过多少度//13:00-14：00=1小时=60分钟=30度，那就是过了多少小时*30度float _seepAngle = 360*(_total_hour/12);
                canvas.drawArc(_rect_f,_startAngle1,_seepAngle1,false,mPaint);

                float _startAngle2 = calcStartAngle("12:00");//计算开始角度
                float _totalHour2 = clacTimeInstance("12:00",_end_time);//计算两个时间段经过多少小时
                float _seepAngle2 = 30 * _totalHour2;//计算通过多少度//13:00-14：00=1小时=60分钟=30度，那就是过了多少小时*30度float _seepAngle = 360*(_total_hour/12);
                canvas.drawArc(_rect_f_2,_startAngle2,_seepAngle2,false,mPaint);

                setPaintStyle(Color.WHITE,STROKE_WIDTH);//白色刷笔
                canvas.drawArc(_rect_f,_startAngle1,1,false,mPaint);//画一段白色的开始间隙
                canvas.drawArc(_rect_f,_startAngle1+_seepAngle1-1,1,false,mPaint);//画一段白色的结束间隙

                canvas.drawArc(_rect_f_2,_startAngle2,1,false,mPaint);//画一段白色的开始间隙
                canvas.drawArc(_rect_f_2,_startAngle2+_seepAngle2-1,1,false,mPaint);//画一段白色的结束间隙
            }



        }
    }

    /**
     * 计算两个时间段经过多少小时
     * @param start_time
     * @param end_time
     * @return
     */
    private static float clacTimeInstance(String start_time, String end_time) {
        String _year = "2016-01-01";
        String _start_time=_year+" "+start_time;
        String _end_time=_year+" "+end_time;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = null;
        Date date = null;
        try {
            now = df.parse(_start_time);
            date = df.parse(_end_time);
        } catch (Exception e) {
            e.printStackTrace();
        }

        float between = (float)(date.getTime() - now.getTime())/(1000*3600);
        return between;
    }

    /**
     * 计算开始角度
     */
    private float calcStartAngle(String start_time) {
        //通过开始时间算出开始角度
        int _hour = Integer.parseInt(start_time.substring(0,start_time.indexOf(":")));
        int _minute = Integer.parseInt(start_time.substring(start_time.indexOf(":")+1));
//        float _seepAngle = 360*((_minute/60)/12);//通过分钟计算多出来的角度
        float _seepAngle =_minute/2;
        float _start_angle = mHourAngle.get(_hour)+_seepAngle;//时刻对应的角度+多出来的角度=开始角度
//        Log.d("jlj","_start_angle----------------="+_start_angle);
        return _start_angle;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("jlj","onTouchEvent---------------");
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            float _x = event.getX();
            float _y = event.getY();
//            Log.d("jlj","onTouchEvent---------------ACTION_DOWN="+_x+","+_y);
        }
        return super.onTouchEvent(event);
    }
}
