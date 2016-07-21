package com.xiye.sclibrary.net.downloader;

import android.text.TextUtils;


import com.xiye.sclibrary.async.PoolAsyncTask;
import com.xiye.sclibrary.utils.FileTools;
import com.xiye.sclibrary.utils.MD5;

import java.io.File;

public class DownloadTask extends PoolAsyncTask<Void, Float, File> implements DownloadProgressCallback{

	private String mUrl;
	private DownloaderNotifyer mNotify;
	private String mDestPath;
	
	public DownloadTask(String url, String dest, DownloaderNotifyer notify){
		mUrl = url;
		mNotify = notify;
		mDestPath = TextUtils.isEmpty(dest)?(FileTools.getDefaultDownloadPath() + File.separator + MD5.encryptByMD5(url)):dest;
	}
	
	@Override
	protected File doInBackground(Void... params) {
		// TODO Auto-generated method stub
		File temp = NetworkFileFetcher.fetch(mUrl, this);
		if (temp != null){
			File ret = new File(mDestPath);
			FileTools.copy(temp, ret);
			temp.delete();
			return ret;
		} else {
			return null;
		}
		
	}

	@Override
	protected void onPostExecute(File result) {
		// TODO Auto-generated method stub
		if (mNotify != null){
			mNotify.onFinish(mUrl, result);
		}
	}
	
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		if (mNotify != null){
			mNotify.onStart(mUrl);
		}
	}
	
	@Override
	protected void onProgressUpdate(Float... values) {
		// TODO Auto-generated method stub
		float progress = values[0];
		if (mNotify != null){
			mNotify.onProgress(mUrl, progress);
		}
	}

	@Override
	public void onProgress(String url, float progress) {
		// TODO Auto-generated method stub
		publishProgress(progress);
	}
}
