package com.gh.utils.views.selectTime.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gh.utils.R;
import com.gh.utils.tools.data.DateUtils;
import com.gh.utils.views.selectTime.view.wheelview.OnWheelChangedListener;
import com.gh.utils.views.selectTime.view.wheelview.OnWheelScrollListener;
import com.gh.utils.views.selectTime.view.wheelview.WheelView;
import com.gh.utils.views.selectTime.view.wheelview.adapter.NumericWheelAdapter;

import java.util.Calendar;

/**
 * @author: gh
 * @description:
 * @date: 2016/9/21 09:33.
 */
public class TimeSelectView extends LinearLayout {

    private Activity mActivity;

    /**
     * 向后年数
     */
    private int mAgoYear = 1;
    /**
     * 向前年数
     */
    private int mBeforYear = 1;
    /**
     * 控件最前年数
     */
    private int mFirstYear;

    /**
     * 是否联动标志位
     */
    private boolean mIsLink;

    /**
     * 控件时间
     */
    private int mYearWheel;
    private int mMonthWheel;
    private int mDayWheel;
    private int mHourWheel;
    private int mMinWheel;

    private TextView id_tv_time;
    private WheelView mYear;
    private WheelView mMonth;
    private WheelView mDay;
    private WheelView mHour;
    private WheelView mMin;

    public TimeSelectView(Context context) {
        this(context, null);
    }

    public TimeSelectView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeSelectView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TimeSelectView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.TimeSelectView_is_link) {
                mIsLink = a.getBoolean(attr, false);
            } else if (attr == R.styleable.TimeSelectView_year_befor) {
                mBeforYear = a.getInt(attr, 1);
            } else if (attr == R.styleable.TimeSelectView_year_ago) {
                mAgoYear = a.getInt(attr, 1);
            }
        }
        a.recycle();

        onCreate(context);
    }

    private void onCreate(Context context) {
        this.mActivity = (Activity) context;
        LayoutInflater.from(context).inflate(R.layout.wheel_date_picker, this);
        initView();
    }

    private void initView() {
        getCurrentTime();
        mFirstYear = mYearWheel - mBeforYear;
        id_tv_time = (TextView) findViewById(R.id.id_tv_time);
        //年
        mYear = (WheelView) findViewById(R.id.id_wv_year);
        NumericWheelAdapter numericWheelAdapter1 = new NumericWheelAdapter(mActivity, mYearWheel - mBeforYear, mYearWheel + mAgoYear);
        numericWheelAdapter1.setLabel("年");
        mYear.setViewAdapter(numericWheelAdapter1);
        mYear.setCyclic(false);//是否可循环滑动
        mYear.addScrollingListener(scrollListener);
        mYear.addChangingListener(wheelChangedListener);
        //月
        mMonth = (WheelView) findViewById(R.id.id_wv_month);
        NumericWheelAdapter numericWheelAdapter2 = new NumericWheelAdapter(mActivity, 1, 12, "%02d");
        numericWheelAdapter2.setLabel("月");
        mMonth.setViewAdapter(numericWheelAdapter2);
        mMonth.setCyclic(true);
        mMonth.addScrollingListener(scrollListener);
        mMonth.addChangingListener(wheelChangedListener);
        //日
        mDay = (WheelView) findViewById(R.id.id_wv_day);
        if (mDayWheel > 15) {
            initDay(mYearWheel, mMonthWheel + 1);
        } else {
            initDay(mYearWheel, mMonthWheel);
        }
        mDay.setCyclic(true);
        mDay.addScrollingListener(scrollListener);
        mDay.addChangingListener(wheelChangedListener);
        //时
        mHour = (WheelView) findViewById(R.id.id_wv_hour);
        NumericWheelAdapter numericWheelAdapter3 = new NumericWheelAdapter(mActivity, 0, 23, "%02d");
        numericWheelAdapter3.setLabel("时");
        mHour.setViewAdapter(numericWheelAdapter3);
        mHour.setCyclic(true);
        mHour.addScrollingListener(scrollListener);
        mHour.addChangingListener(wheelChangedListener);
        //分
        mMin = (WheelView) findViewById(R.id.id_wv_min);
        NumericWheelAdapter numericWheelAdapter4 = new NumericWheelAdapter(mActivity, 0, 59, "%02d");
        numericWheelAdapter4.setLabel("分");
        mMin.setViewAdapter(numericWheelAdapter4);
        mMin.setCyclic(true);
        mMin.addScrollingListener(scrollListener);
        mMin.addChangingListener(wheelChangedListener);
        //设置显示行数
        mYear.setVisibleItems(7);
        mMonth.setVisibleItems(7);
        mDay.setVisibleItems(7);
        mHour.setVisibleItems(7);
        mMin.setVisibleItems(7);
        setCurrentTime();
    }

    /**
     * 设置日期的adapter
     *
     * @param year
     * @param month
     */
    private void initDay(int year, int month) {
        NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(mActivity, 1, getDay(year, month), "%02d");
        numericWheelAdapter.setLabel("日");
        mDay.setViewAdapter(numericWheelAdapter);
    }

    /**
     * 根据年月获取月份的日期
     *
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    /**
     * 滑动停止的监听
     */
    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {

        }
    };

    /**
     * 滑动过程的监听
     */
    OnWheelChangedListener wheelChangedListener = new OnWheelChangedListener() {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            //获取变化后的时间
            if (wheel == mMin) {
                mMinWheel = wheel.getCurrentItem();
            } else if (wheel == mHour) {
                //时
                mHourWheel = wheel.getCurrentItem();
            } else if (wheel == mDay) {
                //日
                mDayWheel = wheel.getCurrentItem() + 1;
            } else if (wheel == mMonth) {
                //月
                mMonthWheel = wheel.getCurrentItem() + 1;
            } else if (wheel == mYear) {
                //年
                mYearWheel = wheel.getCurrentItem() + mFirstYear;
            }
            //时间联动
            if (mIsLink) {
                if (wheel == mMin) {
                    //分
                    if (oldValue == mMin.getViewAdapter().getItemsCount() - 1 && newValue == 0) {
                        mHour.setCurrentItem(mHour.getCurrentItem() + 1);
                    } else if (oldValue == 0 && newValue == mMin.getViewAdapter().getItemsCount() - 1) {
                        mHour.setCurrentItem(mHour.getCurrentItem() - 1);
                    }
                } else if (wheel == mHour) {
                    //时
                    if (oldValue == mHour.getViewAdapter().getItemsCount() - 1 && newValue == 0) {
                        mDay.setCurrentItem(mDay.getCurrentItem() + 1);
                    } else if (oldValue == 0 && newValue == mHour.getViewAdapter().getItemsCount() - 1) {
                        mDay.setCurrentItem(mDay.getCurrentItem() - 1);
                    }
                } else if (wheel == mDay) {
                    //日
                    if (oldValue == mDay.getViewAdapter().getItemsCount() - 1 && newValue == 0) {
                        mMonth.setCurrentItem(mMonth.getCurrentItem() + 1);
                    } else if (oldValue == 0 && newValue == mDay.getViewAdapter().getItemsCount() - 1) {
                        mMonth.setCurrentItem(mMonth.getCurrentItem() - 1);
                    }
                    if (oldValue == 15 && newValue == 16) {
                        initDay(mYearWheel, mMonthWheel);
                    } else if (oldValue == 16 && newValue == 15) {
                        initDay(mYearWheel, mMonthWheel - 1);
                    }
                } else if (wheel == mMonth) {
                    //月
                    if (oldValue == mMonth.getViewAdapter().getItemsCount() - 1 && newValue == 0) {
                        mYear.setCurrentItem(mYear.getCurrentItem() + 1);
                    } else if (oldValue == 0 && newValue == mMonth.getViewAdapter().getItemsCount() - 1) {
                        mYear.setCurrentItem(mYear.getCurrentItem() - 1);
                    }
                    if (mDayWheel > 15) {
                        initDay(mYearWheel, mMonthWheel);
                    } else {
                        initDay(mYearWheel, mMonthWheel - 1);
                    }
                    if (mDayWheel > mDay.getViewAdapter().getItemsCount()) {
                        mDay.setCurrentItem(mDay.getViewAdapter().getItemsCount() - 1);
                        mDayWheel = mDay.getViewAdapter().getItemsCount();
                    }
                } else if (wheel == mYear) {
                    //年
                    if (mDayWheel > 15) {
                        initDay(mYearWheel, mMonthWheel);
                    } else {
                        initDay(mYearWheel, mMonthWheel - 1);
                    }
                }
            }
            id_tv_time.setText(getSelectTimeString());
        }
    };

    /**
     * 获取选择的时间String
     *
     * @return
     */
    private String getSelectTimeString() {
        String mYearText;
        String mMonthText;
        String mDayText;
        String mHourText;
        String mMinText;

        mYearText = String.valueOf(mYearWheel);

        if (mMonthWheel < 10) {
            mMonthText = "0" + mMonthWheel;
        } else {
            mMonthText = String.valueOf(mMonthWheel);
        }

        if (mDayWheel < 10) {
            mDayText = "0" + mDayWheel;
        } else {
            mDayText = String.valueOf(mDayWheel);
        }

        if (mHourWheel < 10) {
            mHourText = "0" + mHourWheel;
        } else {
            mHourText = String.valueOf(mHourWheel);
        }

        if (mMinWheel < 10) {
            mMinText = "0" + mMinWheel;
        } else {
            mMinText = String.valueOf(mMinWheel);
        }

        return mYearText + "-" + mMonthText + "-" + mDayText + "-" + mHourText + "-" + mMinText;
    }

    /**
     * 获取选中的时间
     * @return
     */
    public long getSelectTime() {
        return DateUtils.string2Date(getSelectTimeString(), "yyyy-MM-dd-HH-mm").getTime();
    }

    /**
     * 获取选中的时间
     * @param format
     * @return
     */
    public String getSelectTime(String format) {
        return DateUtils.date2String(getSelectTime(), format);
    }

    /**
     * 获取当前时间
     */
    private void getCurrentTime() {
        Calendar c = Calendar.getInstance();

        mYearWheel = c.get(Calendar.YEAR);
        //通过Calendar算出的月数要+1
        mMonthWheel = c.get(Calendar.MONTH) + 1;
        mDayWheel = c.get(Calendar.DATE);
        mHourWheel = c.get(Calendar.HOUR_OF_DAY);
        mMinWheel = c.get(Calendar.MINUTE);
    }

    /**
     * 设置当前时间
     */
    public void setCurrentTime() {
        getCurrentTime();
        setTime();
    }

    private void setTime() {
        mMin.setCurrentItem(mMinWheel);
        mHour.setCurrentItem(mHourWheel);
        mDay.setCurrentItem(mDayWheel - 1);
        mMonth.setCurrentItem(mMonthWheel - 1);
        mYear.setCurrentItem(mYearWheel - mFirstYear);

        if (mDayWheel > 15) {
            initDay(mYearWheel, mMonthWheel);
        } else {
            initDay(mYearWheel, mMonthWheel - 1);
        }
    }

    /**
     * 设置某一时间点的时间
     *
     * @param time  需要精确到毫秒
     */
    public void setNorTime(long time) {
        // TODO: 2016/9/22  
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        mYearWheel = c.get(Calendar.YEAR);
        //通过Calendar算出的月数要+1
        mMonthWheel = c.get(Calendar.MONTH) + 1;
        mDayWheel = c.get(Calendar.DATE);
        mHourWheel = c.get(Calendar.HOUR_OF_DAY);
        mMinWheel = c.get(Calendar.MINUTE);
        setTime();
    }
}
