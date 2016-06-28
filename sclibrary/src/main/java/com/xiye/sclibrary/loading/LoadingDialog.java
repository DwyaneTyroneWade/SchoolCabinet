package com.xiye.sclibrary.loading;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiye.sclibrary.R;


public class LoadingDialog extends Dialog {

    public final static int PROGRESS_MODE = 1;
    public final static int ICON_MODE = 2;
    private final Window mWindow;
    private Animation mAnimation;
    private TextView mTipTextView;
    private ImageView mProgressImage;
    private int mMode;

    public LoadingDialog(Context context) {
        super(context, R.style.dialog);
        mWindow = getWindow();
        mMode = PROGRESS_MODE;
        create(context, 0);
    }

    /*
     * imageResID：图片资源，在PROGRESS_MODE下不起作用，由默认的菊花图案完成动画mode：PROGRESS_MODE
     * 动画模式，ICON_MODE 静态图标展示模式
     */
    public LoadingDialog(Context context, int imageResID, int mode) {
        super(context, R.style.dialog);
        mWindow = getWindow();
        mMode = mode;
        create(context, imageResID);
    }

    public static LoadingDialog show(Context context, CharSequence title,
                                     CharSequence message) {
        LoadingDialog dialog = new LoadingDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.show();
        return dialog;
    }

    public static LoadingDialog show(Context context, CharSequence title,
                                     CharSequence message, boolean indeterminate, boolean cancelable,
                                     OnCancelListener cancelListener) {
        LoadingDialog dialog = new LoadingDialog(context);
        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setCancelable(cancelable);
        dialog.setOnCancelListener(cancelListener);
        dialog.show();
        return dialog;
    }

    public void create(Context context, int imageResID) {
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.dimAmount = 0.5f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
        mWindow.setContentView(R.layout.progress_dialog);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        mProgressImage = (ImageView) mWindow.findViewById(R.id.progress_img);
        mTipTextView = (TextView) mWindow.findViewById(R.id.message_textview);
        if (mMode == PROGRESS_MODE) {
            mAnimation = AnimationUtils.loadAnimation(context,
                    R.anim.dialog_progress_rotate);
        } else {
            mProgressImage.setImageResource(imageResID);
        }
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    public void setMessage(CharSequence message) {
        mTipTextView.setText(message);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mMode == PROGRESS_MODE) {
            mProgressImage.startAnimation(mAnimation);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mMode == PROGRESS_MODE) {
            mProgressImage.clearAnimation();
        }
    }

    public void setIndeterminate(boolean indeterminate) {
        // mIndeterminate = indeterminate;
    }

}
