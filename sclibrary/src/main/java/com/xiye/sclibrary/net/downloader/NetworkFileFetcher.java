
package com.xiye.sclibrary.net.downloader;


import com.xiye.sclibrary.base.L;
import com.xiye.sclibrary.utils.FileTools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkFileFetcher {

	public static final String TAG = "NetworkFileFetcher";

	static final int CONNECTION_TIMEOUT = 20000;
	static final int SO_TIMEOUT = 20000;
	private static final int BUFF_SIZE = 8 * 1024;

	public static File fetch(String url, DownloadProgressCallback notify) {				
		File bitmap = null;
		bitmap = urlFetch(url, notify);
		return bitmap;
	}

	private static File urlFetch(String url, DownloadProgressCallback notify) {
		try {
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setConnectTimeout(CONNECTION_TIMEOUT);
			conn.setReadTimeout(SO_TIMEOUT);
			int rspCode = conn.getResponseCode();
			if (rspCode != 200) {
				L.w(TAG, "Error " + rspCode
						 + " while download file from " + url);
				return null;
			}
			File tmpFile = null;
			InputStream inputStream = null;
			FileOutputStream fos = null;
			inputStream = conn.getInputStream();
			long total = conn.getContentLength();
			float progress = 0;
			try {
				tmpFile = FileTools.getRandomTempFile();
				// Log.d(TAG, "path:" + tmpFile.getAbsolutePath() + " "
				// + tmpFile.canWrite());
				if (tmpFile != null) {
					fos = new FileOutputStream(tmpFile);
					final BufferedInputStream bis = new BufferedInputStream(
							inputStream, BUFF_SIZE);
					final BufferedOutputStream bos = new BufferedOutputStream(
							fos, BUFF_SIZE);
					byte[] buff = new byte[BUFF_SIZE];
					int len;
					int downloaded = 0;
					while ((len = bis.read(buff)) > 0) {
						bos.write(buff, 0, len);
						downloaded += len;
						progress = 1.0f * downloaded / total;
						// PROGRESS
						if (notify != null) {
							notify.onProgress(url, progress);
						}
					}
					bos.flush();
					bos.close();
					bis.close();
					fos.close();
					inputStream.close();
					inputStream = null;
					fos = null;
					return tmpFile;
				}

			} finally {
				if (inputStream != null) {
					inputStream.close();
				}

				if (fos != null) {
					fos.close();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			// Log.w(TAG, "Error while retrieving bitmap from " + url, e);
		} catch (IOException e) {
			e.printStackTrace();
			// Log.w(TAG, "Error while retrieving bitmap from " + url, e);
		}
		return null;
	}
}
