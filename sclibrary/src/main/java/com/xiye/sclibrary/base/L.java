package com.xiye.sclibrary.base;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import com.android.volley.VolleyError;
import com.xiye.sclibrary.net.needle.Needle;

public class L {

	public static final boolean DEBUG = Env.LOGABLE;

	public static final String TAG = "XIYE_SCHOOL_CABINET";

	public static void d(String tag, Object o) {
		if (DEBUG) {
			Log.d(tag, String.valueOf(o));
		}
	}

	public static void d(Object o) {
		if (DEBUG) {
			Log.d(TAG, String.valueOf(o));
		}
	}

	public static void e(String tag, Object o) {
		if (DEBUG) {
			Log.e(tag, String.valueOf(o));
		}
	}

	public static void e(Object o) {
		if (DEBUG) {
			Log.e(TAG, String.valueOf(o));
		}
	}

	public static void i(String tag, Object o) {
		if (DEBUG) {
			Log.i(tag, String.valueOf(o));
		}
	}

	public static void i(Object o) {
		if (DEBUG) {
			Log.i(TAG, String.valueOf(o));
		}
	}

	public static void v(String tag, Object o) {
		if (DEBUG) {
			Log.v(tag, String.valueOf(o));
		}
	}

	public static void v(Object o) {
		if (DEBUG) {
			Log.v(TAG, String.valueOf(o));
		}
	}

	public static void w(String tag, Object o) {

		if (DEBUG) {
			Log.w(tag, String.valueOf(o));
		}
	}

	public static void w(Object o) {
		if (DEBUG) {
			Log.w(TAG, String.valueOf(o));
		}
	}

	public static void volleyError(VolleyError err) {
		if (DEBUG) {
			err.printStackTrace();
		}
	}

	public static void wtf(final String tag, final Object o) {

		// new PoolAsyncTask<Object, Void, Void>() {
		//
		// @Override
		// protected Void doInBackground(Object... params) {
		// // TODO Auto-generated method stub
		// doWtf(String.valueOf(params[0]), params[1]);
		// return null;
		// }
		//
		// }.execute(tag, o);

		Needle.onBackgroundThread().withTaskType(LibConstants.NEEDLE_TYPE_LOG)
				.execute(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						doWtf(tag, o);
					}
				});
	}

	private static synchronized void doWtf(String tag, Object o) {

		if (!DEBUG) {
			return;
		}

		File logFile = createOrOpenLogFile();

		if (logFile == null) {
			return;
		}

		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(logFile, "rw");
			raf.seek(raf.length());
			raf.writeBytes(current());
			raf.writeBytes("\t");
			raf.writeBytes("TAG:\t");
			raf.write(tag.getBytes("gbk"));
			raf.writeBytes("\t\t");
			raf.write(String.valueOf(o).getBytes("gbk"));
			raf.writeBytes("\n");
			raf.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		} finally {
			if (raf != null) {
				try {
					raf.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
				}
			}
		}
	}

	private static String current() {
		long now = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssss",
				Locale.getDefault());
		return sdf.format(new Date(now));
	}

	@SuppressLint("NewApi")
	private static File createOrOpenLogFile() {

		String fileName = "log.txt";
		String path = "com.android.files.cache";
		boolean hasSdcard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		if (!hasSdcard) {
			return null;
		}

		String dirPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + File.separator + path;
		File dirFile = new File(dirPath);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		String logPath = dirPath + File.separator + fileName;
		File logF = new File(logPath);
		if (logF.exists()) {
			if (logF.length() > 10 * 1024 * 1024) {// if log file's size > 10M ,
													// delete file
				logF.delete();
			}
		}
		File sdcard = Environment.getExternalStorageDirectory();
		StatFs stat = new StatFs(sdcard.getPath());

		long blockSize = 0l;
		long availableBlocks = 0l;
		if (android.os.Build.VERSION.SDK_INT < 18) {
			blockSize = stat.getBlockSize();
			availableBlocks = stat.getAvailableBlocks();
		} else {
			blockSize = stat.getBlockSizeLong();
			availableBlocks = stat.getAvailableBlocksLong();
		}
		long available = blockSize * availableBlocks;
		if (available < 20 * 1024 * 1024) {
			return null;
		}

		boolean suc = false;

		if (!logF.exists()) {
			try {
				suc = logF.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				return null;
			}
		} else {
			suc = true;
		}
		if (suc) {
			return logF;
		} else {
			return null;
		}
	}
}
