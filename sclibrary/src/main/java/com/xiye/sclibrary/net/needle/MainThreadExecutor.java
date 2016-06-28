package com.xiye.sclibrary.net.needle;

import java.util.concurrent.Executor;

import android.os.Handler;
import android.os.Looper;

/**
 * Use for tasks that need to run on the UI/main thread.
 */
class MainThreadExecutor implements Executor {

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(Runnable runnable) {
        mHandler.post(runnable);
    }
}
