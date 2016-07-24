package com.xiye.sclibrary.timer;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class DelayTimerSingleInstance {
    private OnTimeToFinishActivitySingleListener mListener;
    private long delayTime = Long.valueOf(8) * 60 * 1000;
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

    private DelayTimerSingleInstance() {

    }

    public static DelayTimerSingleInstance getInstance() {
        return Holder.INSTANCE;
    }

    public void init(OnTimeToFinishActivitySingleListener listener, long delayTime) {
        this.mListener = listener;
        this.delayTime = delayTime;
    }

    public void setOnTimeToFinishActivityListener(OnTimeToFinishActivitySingleListener listener) {
        this.mListener = listener;
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

    public void resetTimer() {
        cancelTimer();
        startTimer();
    }

    public interface OnTimeToFinishActivitySingleListener {
        void onTimeToFinishActivity();
    }

    private static class Holder {
        static final DelayTimerSingleInstance INSTANCE = new DelayTimerSingleInstance();
    }

}
