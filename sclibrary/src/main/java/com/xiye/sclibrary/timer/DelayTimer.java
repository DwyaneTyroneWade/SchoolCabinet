package com.xiye.sclibrary.timer;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class DelayTimer {
    private OnTimeToFinishActivityListener mListener;
    private long delayTime = Long.valueOf(3) * 60 * 1000;
    private TimerTask mTimerTask;
    private Timer mTimer;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (msg.what == 0) {
                if (mListener != null) {
                    mListener.onTimeToFinishActivity();
                }
            }
        }

        ;
    };

    public DelayTimer() {

    }

    public DelayTimer(OnTimeToFinishActivityListener listener) {
        this.mListener = listener;
    }

    public DelayTimer(OnTimeToFinishActivityListener listener, long delayTime) {
        this.mListener = listener;
        this.delayTime = delayTime;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
    }

    public void startTimer() {
        mTimerTask = new TimerTask() {

            @Override
            public void run() {
                mHandler.sendMessage(mHandler.obtainMessage(0));
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTimerTask, delayTime);
    }

    public void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    public interface OnTimeToFinishActivityListener {
        void onTimeToFinishActivity();
    }

}
