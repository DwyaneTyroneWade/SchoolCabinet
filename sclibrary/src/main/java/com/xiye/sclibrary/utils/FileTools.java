package com.xiye.sclibrary.utils;

import android.os.Environment;

import com.xiye.sclibrary.base.C;
import com.xiye.sclibrary.base.L;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class FileTools {

    public static final String TEMP_PATH = "curry/temp/file";
    public static final String DOWNLOAD_PATH = "curry/download";
    public static final String FILES_PATH = "curry/files";
    private static final String TAG = "FileTools";

    public static boolean isSdcardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState());
    }

    public static File getRandomTempFile() {
        if (isSdcardAvailable()) {
            return getSdRandomTempFile();
        } else {
            return getPrivateRandomTempFile();
        }
    }

    public static File getSdRandomTempFile() {
        String f = Environment.getExternalStorageDirectory().getAbsolutePath();
        return createTempFile(f);
    }

    public static File getPrivateRandomTempFile() {
        String f = C.get().getFilesDir().getAbsolutePath();
        return createTempFile(f);
    }

    private static File createTempFile(String dirPath) {
        String fullDir = dirPath + File.separator + TEMP_PATH;
        File dir = new File(fullDir);
        if (!dir.exists()) {
            dir.mkdirs();
        } else {
            if (!dir.isDirectory()) {
                dir.delete();
                dir.mkdirs();
            }
        }
        String fileFullPath = fullDir + File.separator + UUID.randomUUID().toString();
        return new File(fileFullPath);
    }

    public static boolean copy(File src, String dest) {
        File destFile = new File(dest);
        return copy(src, destFile);
    }

    public static boolean copy(File src, File dest) {
        if (src == null) {
            L.w(TAG, "copy: src is null");
            return false;
        }

        if (dest == null) {
            L.w(TAG, "copy: dest is null");
            return false;
        }

        if (!src.exists()) {
            L.w(TAG, "copy: src dose not exist");
            return false;
        }

        if (dest.exists()) {
            dest.delete();
        }
        File dir = dest.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            dest.createNewFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            L.w(TAG, "copy: create dest file err");
            e.printStackTrace();
            return false;
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            fis = new FileInputStream(src);
            fos = new FileOutputStream(dest);

            final BufferedInputStream bis = new BufferedInputStream(fis, 8 * 1024);
            final BufferedOutputStream bos = new BufferedOutputStream(fos, 8 * 1024);
            byte[] buff = new byte[8 * 1024];
            int len;

            while ((len = bis.read(buff)) > 0) {
                bos.write(buff, 0, len);
            }
            bos.flush();
            bos.close();
            bis.close();
            return true;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static String getDefaultDownloadPath() {
        if (isSdcardAvailable()) {
            return getSdDownloadPath();
        } else {
            return getPrivateDownloadPath();
        }
    }

    public static String getPrivateDownloadPath() {
        return C.get().getFilesDir().getAbsolutePath() + File.separator + DOWNLOAD_PATH;
    }

    public static String getSdDownloadPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + DOWNLOAD_PATH;
    }

    public static String getPrivateFilesPath() {
        return C.get().getFilesDir().getAbsolutePath() + File.separator + FILES_PATH;
    }
}
