package com.xiye.sclibrary.timer;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class DelayTimerWithRunnable {
    private OnTimeToFinishActivityListener mListener;
    private OnTimeRunnableListener onTimeRunnableListener;
    private long delayTime = Long.valueOf(3) * 60 * 1000;
    private long ssTime;
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

    private Runnable timeRunnable = new Runnable() {

        @Override
        public void run() {
            if (onTimeRunnableListener != null) {
                onTimeRunnableListener.onTimerun(String.valueOf(ssTime--));
            }
            mHandler.postDelayed(timeRunnable, 1000);
        }
    };

    public DelayTimerWithRunnable() {

    }

    public DelayTimerWithRunnable(OnTimeToFinishActivityListener listener) {
        this.mListener = listener;
    }

    public DelayTimerWithRunnable(OnTimeToFinishActivityListener listener, long delayTime) {
        this.mListener = listener;
        this.delayTime = delayTime;
        ssTime = delayTime / 1000;
    }

    public void setTimeRunnableListener(OnTimeRunnableListener onTimeRunnableListener) {
        this.onTimeRunnableListener = onTimeRunnableListener;
    }

    public void setDelayTime(long delayTime) {
        this.delayTime = delayTime;
        ssTime = delayTime / 1000;
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
        startTimeRunnable();
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
        stopTimeRunnable();
    }

    public void resetTimer() {
        cancelTimer();
        startTimer();
    }

    private void startTimeRunnable() {
        ssTime = delayTime / 1000;
        mHandler.post(timeRunnable);
    }

    private void stopTimeRunnable() {
        mHandler.removeCallbacks(timeRunnable);
    }

    public interface OnTimeToFinishActivityListener {
        void onTimeToFinishActivity();
    }

    public interface OnTimeRunnableListener {
        void onTimerun(String time);
    }
}
