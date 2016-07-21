package com.xiye.sclibrary.net.downloader;

import java.io.File;

public interface DownloaderNotifyer {

	public void onStart(String url);
	public void onProgress(String url, float progress);
	public void onFinish(String url, File file);
}
