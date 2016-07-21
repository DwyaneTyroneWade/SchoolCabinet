package com.xiye.sclibrary.net.downloader;

public interface DownloadProgressCallback {
	public void onProgress(String url, float progress);
}
