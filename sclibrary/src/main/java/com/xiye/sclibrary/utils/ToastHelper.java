package com.xiye.sclibrary.utils;

import android.view.Gravity;
import android.widget.Toast;

import com.xiye.sclibrary.base.C;

public class ToastHelper {

    public static Toast showShortToast(int textResId) {
        return showShortToast(C.get().getString(textResId));
    }

    public static Toast showLongToast(int textResId) {
        return showLongToast(C.get().getString(textResId));
    }

    public static Toast showShortToast(String text) {
        return showToast(Toast.LENGTH_SHORT, text);
    }

    public static Toast showLongToast(String text) {
        return showToast(Toast.LENGTH_LONG, text);
    }

    private static Toast showToast(int length, String text) {
        Toast toast = Toast.makeText(C.get(), text, length);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return toast;
    }
}
