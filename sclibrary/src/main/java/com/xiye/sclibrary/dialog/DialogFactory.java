package com.xiye.sclibrary.dialog;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

import java.util.ArrayList;
import java.util.List;

public class DialogFactory {
    private static List<AlertDialog> dialogArr = new ArrayList<>();
    private final String TAG = DialogFactory.class.getSimpleName();

    public static void showOneConfirmDialog(Context context, String title, String msg, String okMsg, final OnTwoButtonClickListener twoButtonclickListener) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(okMsg, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                twoButtonclickListener.btnOkOnClicklistener();
                dialog.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(null);

        dialog.show();

    }

    public static void showSecondConfirmDialog(Context context, String title, String msg, String okMsg, String cancelMsg, final OnTwoButtonClickListener twoButtonclickListener) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(okMsg, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                twoButtonclickListener.btnOkOnClicklistener();
                dialog.dismiss();

            }
        });
        builder.setNegativeButton(cancelMsg, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                twoButtonclickListener.btnCancleOnClicklistener();
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(null);

        dialog.show();

    }

    /**
     * @param title
     * @param items
     * @param checkItem              specifies which item is checked. If -1 no items are checked.
     * @param okMsg
     * @param listener
     * @param twoButtonclickListener
     */
    public static void showSingleChoiceDialog(Context context, String title, String[] items, int checkItem, String okMsg, OnClickListener listener, final OnTwoButtonClickListener twoButtonclickListener) {
        Builder builder = new Builder(context);
        builder.setTitle(title);
        builder.setSingleChoiceItems(items, checkItem, listener);
        builder.setCancelable(false);
        builder.setPositiveButton(okMsg, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                twoButtonclickListener.btnCancleOnClicklistener();
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(null);

        dialog.show();
    }

    public static AlertDialog getTipDialog(Context context, String msg) {
        final OnTwoButtonClickListener twoButtonclickListener = new OnTwoButtonClickListener() {
            @Override
            public void btnOkOnClicklistener() {

            }

            @Override
            public void btnCancleOnClicklistener() {

            }
        };

        Builder builder = new Builder(context);
        builder.setTitle("提示");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                twoButtonclickListener.btnOkOnClicklistener();
                dialog.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(null);
        return dialog;
    }

    public static AlertDialog getTipDialog(Context context, String msg, final OnTwoButtonClickListener twoButtonclickListener) {
        Builder builder = new Builder(context);
        builder.setTitle("提示");
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton("确定", new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                twoButtonclickListener.btnOkOnClicklistener();
                dialog.dismiss();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(null);
        return dialog;
    }

    public static void showDialog(AlertDialog dialog) {
        clearDialogArr();
        dialogArr.add(dialog);
        dialog.show();
    }

    public static void clearDialogArr() {
        if (dialogArr != null && dialogArr.size() > 0) {
            for (int i = 0; i < dialogArr.size(); i++) {
                dialogArr.get(i).dismiss();
            }
            dialogArr.clear();
        }
    }

    public interface OnTwoButtonClickListener {
        public void btnOkOnClicklistener();

        public void btnCancleOnClicklistener();
    }
}
