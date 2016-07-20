package com.xiye.schoolcabinet.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RequestQueue.RequestFilter;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.xiye.schoolcabinet.R;
import com.xiye.schoolcabinet.helpers.ClockHelper;
import com.xiye.schoolcabinet.utils.ActivityStack;
import com.xiye.sclibrary.base.C;
import com.xiye.sclibrary.base.L;
import com.xiye.sclibrary.loading.LoadingDialog;
import com.xiye.sclibrary.net.volley.BaseResultBean;
import com.xiye.sclibrary.net.volley.WillCancelDelegate;
import com.xiye.sclibrary.utils.ToastHelper;
import com.xiye.sclibrary.utils.Tools;


public abstract class BaseActivity extends FragmentActivity {
    protected boolean isDestroyed = false;
    private RequestQueue mQueue;
    private LoadingDialog mLoadingDialog;
    private LoadingDialog mMsgLoadingDialog;
    private boolean mIsShowing;

    public void executeRequest(Request<?> request) {
        final RequestQueue queue = getRequestQueue();
        queue.add(request);
    }

    public RequestQueue getRequestQueue() {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(C.get());
        }

        return mQueue;
    }

    /**
     * 4.4 api19以上，才能完全隐藏，低于4.4会出现按一次出现navigation bar,再按一次屏幕才能获取焦点
     */
    public void hideNavigationBar() {
        if (android.os.Build.VERSION.SDK_INT < 19) {
            return;
        }

        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN; // hide status bar

        if (android.os.Build.VERSION.SDK_INT >= 19) {
            uiFlags |= 0x00001000; // SYSTEM_UI_FLAG_IMMERSIVE_STICKY: hide
            // navigation bars - compatibility: building
            // API level is lower thatn 19, use magic
            // number directly for higher API target
            // level
        } else {
            //TODO useless code
            uiFlags |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }

        getWindow().getDecorView().setSystemUiVisibility(uiFlags);
    }

    /**
     * 右上角时钟
     */
    protected void initClock() {
//        ClockHelper.getInstance().setTextView(
//                (TextView) findViewById(R.id.tv_date),
//                (TextView) findViewById(R.id.tv_time));
//        ClockHelper.getInstance().upTime();
        ClockHelper.getInstance().setTvCalendar((TextView) findViewById(R.id.tv_calendar));
        ClockHelper.getInstance().upCalendar();
    }

    /**
     * 设置TOP的班级信息
     *
     * @param classInfoStr
     */
    protected void setClassInfo(String classInfoStr) {
        TextView tvClassInfo = (TextView) findViewById(R.id.tv_class_info);
        if (tvClassInfo != null && !Tools.isStringEmpty(classInfoStr)) {
            tvClassInfo.setText(classInfoStr);
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        // TODO Auto-generated method stub
        hideNavigationBar();
        super.onCreate(arg0);

        ActivityStack.getInstance().push(this);
        mIsShowing = true;
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        initClock();

        mIsShowing = true;
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        mIsShowing = false;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        isDestroyed = true;
        ActivityStack.getInstance().pop(this);

        if (mQueue != null) {
            mQueue.cancelAll(new RequestFilter() {
                @Override
                public boolean apply(Request<?> request) {
                    return (request instanceof WillCancelDelegate)
                            && ((WillCancelDelegate) request)
                            .willCancelWhenOnDestroy();
                }
            });
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideNavigationBar();
        }
    }

    public void showLoading() {
        L.d("isshowing: " + mIsShowing);
        if (!mIsShowing) {
            return;
        }
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this);
        } else {
            if (mLoadingDialog.isShowing()) {
                return;
            }
        }
        mLoadingDialog.show();
    }

    public boolean isShowing() {
        boolean isShowing = false;
        if (mLoadingDialog != null) {
            isShowing = mLoadingDialog.isShowing();
        } else if (mMsgLoadingDialog != null) {
            isShowing = mMsgLoadingDialog.isShowing();
        }
        L.d("MainActivity", "isShowing:" + isShowing);
        return isShowing;
    }

    public void dismissLoading() {
        try {
            if (mLoadingDialog != null) {
                mLoadingDialog.dismiss();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            if (mMsgLoadingDialog != null) {
                mMsgLoadingDialog.dismiss();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    public void showLoading(int msgResId) {
        if (!mIsShowing) {
            return;
        }
        showLoading(getString(msgResId));
    }

    public void showLoading(String msg) {
        if (!mIsShowing) {
            return;
        }
        if (mMsgLoadingDialog == null) {
            mMsgLoadingDialog = new LoadingDialog(this);
        }
        if (mMsgLoadingDialog.isShowing()) {
            mMsgLoadingDialog.dismiss();
        }
        mMsgLoadingDialog.setMessage(msg);
        mMsgLoadingDialog.show();
    }

    public void processErrorCode(BaseResultBean result) {
        // TODO 提示用户Dialog
        if (result == null) {
            return;
        }

        L.d("error_no:" + result.err_no + "err_msg:" + result.err_msg);

        if (!TextUtils.isEmpty(result.err_msg) && !"FALSE".equals(result.err_msg.toUpperCase())) {
            ToastHelper.showShortToast(result.err_msg);
        }
    }

    public void processVolleyError(VolleyError error) {
        L.volleyError(error);
        if (error != null) {
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                ToastHelper.showShortToast(R.string.err_network_timeout);
            } else if (error instanceof AuthFailureError) {
                //TODO
            } else if (error instanceof ServerError) {
                //TODO
            } else if (error instanceof NetworkError) {
                //TODO
            } else if (error instanceof ParseError) {
                //TODO
            }
        }
    }
}
