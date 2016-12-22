package com.gh.tools.selectDate;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

import com.gh.tools.R;
import com.gh.tools.base.BaseActivity;

import java.util.Calendar;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author: gh
 * @description:
 * @date: 2016/10/26 09:56.
 */

public class SelectDateActivity extends BaseActivity {

    @Bind(R.id.id_tv)
    TextView id_tv;

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SelectDateActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.id_btn)
    public void onClick() {
        //初始化Calendar日历对象
        Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
        int year = mycalendar.get(Calendar.YEAR);
        int month = mycalendar.get(Calendar.MONTH);
        int day = mycalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dpd = new DatePickerDialog(this, datelistener, year, month, day);
        dpd.show();
    }

    private DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String timeText = String.format("%1$d-%2$d-%3$d",
                    year, monthOfYear + 1, dayOfMonth);
            id_tv.setText(timeText);
        }
    };
}
