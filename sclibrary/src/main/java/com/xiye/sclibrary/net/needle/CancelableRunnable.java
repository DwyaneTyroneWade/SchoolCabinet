package com.xiye.sclibrary.net.needle;

public interface CancelableRunnable extends Runnable {

	void cancel();

	boolean isCanceled();
}
