package com.xiye.sclibrary.utils;

import android.content.Context;
import android.text.TextUtils;

import com.xiye.sclibrary.base.L;

public class Tools {
    public static boolean isStringEmpty(CharSequence input) {
        if (TextUtils.isEmpty(input)) {
            return true;
        }
        if (input.equals("null")) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个字符串是不是纯数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (int i = str.length(); --i >= 0; ) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static int dip2px(Context context, float dip) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f);
    }

    public static byte[] getRealBuffer(byte[] buffer, int size) {
        byte[] realBuffer = new byte[size];
        for (int i = 0; i < size; i++) {
            realBuffer[i] = buffer[i];
        }
        return realBuffer;
    }

    public static StringBuffer newStringBuffer(StringBuffer buffer) {
        if (buffer == null) {
            return new StringBuffer();
        } else {
            buffer.delete(0, buffer.length());
            return buffer;
        }
    }

    /**
     * 获取屏幕的宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕的高度
     *
     * @return
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static void LogDpi(Context context) {
        int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
        float density = context.getResources().getDisplayMetrics().density;
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        L.d("DPI", "[densityDpi]:" + densityDpi + "[density]:" + density + "[scaledDensity]:" + scaledDensity);
    }
}
