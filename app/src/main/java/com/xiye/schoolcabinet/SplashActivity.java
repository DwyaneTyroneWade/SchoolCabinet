package com.xiye.schoolcabinet;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.dispatcher.ActivityDispatcher;
import com.xiye.sclibrary.utils.TimeUtils;

/**
 * Created by wushuang on 6/25/16.
 */
public class SplashActivity extends BaseActivity {
    private static final int SPLASH_TIME = 3000;
    private Handler mHandler = new Handler();
    private Runnable mGoRunnable = new Runnable() {

        @Override
        public void run() {
            goNext();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_splash);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        go();
    }

    private void initView() {
        TextView copyRight = (TextView) findViewById(R.id.splash_right);
        copyRight.setText(getString(R.string.copy_right, TimeUtils.getYear()));
    }

    private void go() {
        mHandler.postDelayed(mGoRunnable, SPLASH_TIME);
    }

    private void goNext() {
        ActivityDispatcher.goMain(this, null);
        finish();
    }
}
