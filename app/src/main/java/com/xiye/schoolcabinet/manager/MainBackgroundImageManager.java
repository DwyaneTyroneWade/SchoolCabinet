package com.xiye.schoolcabinet.manager;

import android.text.TextUtils;

import com.xiye.sclibrary.base.C;
import com.xiye.sclibrary.base.L;
import com.xiye.sclibrary.net.downloader.DownloadTask;
import com.xiye.sclibrary.net.downloader.DownloaderNotifyer;
import com.xiye.sclibrary.utils.MD5;
import com.xiye.sclibrary.utils.Tools;

import java.io.File;

/**
 * Created by wushuang on 7/21/16.
 */
public class MainBackgroundImageManager {
    private static final String TAG = MainBackgroundImageManager.class.getSimpleName();

    private static final String MAIN_BG_FILE_PATH = "curry/main_bg";

    private static void doDownload(String url) {
        DownloadTask task = new DownloadTask(url, getMainBgFilePath(url), new DownloaderNotifyer() {

            @Override
            public void onStart(String url) {
                // TODO Auto-generated method stub
                L.i(TAG, "download onstart");
            }

            @Override
            public void onProgress(String url, float progress) {
                // TODO Auto-generated method stub
                L.i(TAG, "download onProgress: " + progress);
            }

            @Override
            public void onFinish(String url, File file) {
                // TODO Auto-generated method stub
                L.i(TAG, "download onFinish: " + file.getName());
            }
        });
        task.execute();
    }

    public static String getMainBgFilePath(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        String name = MD5.encryptByMD5(url);
//		return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + MAIN_BG_FILE_PATH + File.separator + name;
        return C.get().getFilesDir().getAbsolutePath() + File.separator + MAIN_BG_FILE_PATH + File.separator + name;
    }

    public static boolean isMainBgExisted(String url) {
        String filePath = getMainBgFilePath(url);
        File bgFile = new File(filePath);
        return (bgFile != null && bgFile.exists());
    }

    public static void saveUrl(String url) {
        if (!Tools.isStringEmpty(url)) {
            CacheManager.setCache(CacheManager.CACHE_KEY_MAIN_BG_URL, url);
        }
    }

    public static String getUrl() {
        return CacheManager.loadCache(CacheManager.CACHE_KEY_MAIN_BG_URL);
    }

    public static void download(String newUrl) {
        if (!getUrl().equals(newUrl)) {
            //delete old file
            deleteOldBg(getUrl());
            saveUrl(newUrl);
            doDownload(newUrl);
        }
    }

    private static void deleteOldBg(String url) {
        String filePath = getMainBgFilePath(url);
        File bgFile = new File(filePath);
        if (bgFile != null && bgFile.exists()) {
            bgFile.delete();
        }
    }
}
