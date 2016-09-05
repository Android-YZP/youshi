package com.mkch.youshi.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.NumberPicker;

import com.mkch.youshi.R;

import java.util.Calendar;

public class ChooseRemindBeforeActivity extends AppCompatActivity {

    private NumberPicker _npYear;
    private NumberPicker _npMonth;
    private NumberPicker _npDay;
    private int mYear = 2016;
    private int mMonth = 9;
    private NumberPicker _npHour;
    private NumberPicker _npHin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_remind_before);


        _npYear = (NumberPicker) findViewById(R.id.np_year);
        _npMonth = (NumberPicker) findViewById(R.id.np_month);
        _npDay = (NumberPicker) findViewById(R.id.np_day);
        _npHour = (NumberPicker) findViewById(R.id.np_hour);
        _npHin = (NumberPicker) findViewById(R.id.np_min);

        _npHour.setMaxValue(23);
        _npHour.setMinValue(0);
        _npHour.setValue(17);

        _npHin.setMaxValue(59);
        _npHin.setMinValue(0);
        _npHin.setValue(16);

        _npYear.setMaxValue(2036);
        _npYear.setMinValue(2016);
        _npYear.setValue(2016);

        _npMonth.setMaxValue(12);
        _npMonth.setMinValue(1);
        _npMonth.setValue(9);

        _npDay.setMinValue(1);
        _npDay.setMaxValue(getDays(mYear, mMonth));
        _npDay.setValue(1);
        _npYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mYear = newVal;
                int maxMonth = getDays(mYear, mMonth);
                _npDay.setMaxValue(maxMonth);
            }
        });

        _npMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                mMonth = newVal;
                int maxMonth = getDays(mYear, mMonth);
                _npDay.setMaxValue(maxMonth);
            }
        });
    }

    public int getDays(int year , int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);//先指定年份
        calendar.set(Calendar.MONTH, month - 1);//再指定月份 Java月份从0开始算
        return calendar.getActualMaximum(Calendar.DATE);//获取指定年份中指定月份有几天
    }
}
