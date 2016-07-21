package com.xiye.sclibrary.net.downloader;

public class FileDownloader {

	public static final void downloadFile(String url, String dest, DownloaderNotifyer notifyer){
		new DownloadTask(url, dest, notifyer).execute();
	}
}
