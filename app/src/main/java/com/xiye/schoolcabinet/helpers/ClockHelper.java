package com.xiye.schoolcabinet.helpers;

import android.os.Handler;
import android.widget.TextView;

import com.xiye.sclibrary.base.C;
import com.xiye.sclibrary.utils.TimeUtils;


public class ClockHelper {

    private final long UP_TIME = 500;
    private TextView tvDate;
    private TextView tvTime;
    private TextView tvCalendar;
    private Handler mHandler = new Handler();
    private Runnable time = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            upTime();
            upCalendar();
        }
    };

    private ClockHelper() {

    }

    public static ClockHelper getInstance() {
        return Holder.INSTANCE;
    }

    public void setTextView(TextView tvDate, TextView tvTime) {
        this.tvDate = tvDate;
        this.tvTime = tvTime;
    }

    public void upTime() {
        if (tvDate == null || tvTime == null) {// 不存在top的Activity
//			L.d("date null");
            return;
        }
        tvDate.setText(TimeUtils.getDate());
        tvTime.setText(TimeUtils.getTime());
        mHandler.postDelayed(time, UP_TIME);
    }

    public void setTvCalendar(TextView tvCalendar) {
        this.tvCalendar = tvCalendar;
    }

    public void upCalendar() {
        if (tvCalendar == null) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append(TimeUtils.getDate());
        builder.append(" ");
        builder.append(TimeUtils.getTime());
        builder.append(" ");
        builder.append(TimeUtils.getWeek(C.get()));

        tvCalendar.setText(builder.toString());
        mHandler.postDelayed(time, UP_TIME);
    }

    private static class Holder {
        static final ClockHelper INSTANCE = new ClockHelper();
    }
}
