package com.xiye.schoolcabinet.manager;

import com.xiye.sclibrary.net.needle.BackgroundThreadExecutor;
import com.xiye.sclibrary.net.needle.Needle;

import java.util.concurrent.Executor;

/**
 * Created by wushuang on 7/3/16.
 */
public class NeedleManager {
    public static final String NEEDLE_TYPE_LOCK_ACTION = "needle_lock_action";

    public static BackgroundThreadExecutor getBackgroundThreadExecutorForLock() {
        return Needle.onBackgroundThread().serially().withTaskType(NEEDLE_TYPE_LOCK_ACTION);
    }

    public static Executor getMainThreadExecutorForLock() {
        return Needle.onMainThread();
    }
}
