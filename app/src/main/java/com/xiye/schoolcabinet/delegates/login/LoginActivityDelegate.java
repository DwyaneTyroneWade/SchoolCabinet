package com.xiye.schoolcabinet.delegates.login;

import com.xiye.schoolcabinet.R;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.sclibrary.listener.BaseCallBackListener;
import com.xiye.sclibrary.utils.ToastHelper;
import com.xiye.sclibrary.utils.Tools;

/**
 * Created by wushuang on 6/25/16.
 */
public class LoginActivityDelegate {
    private BaseActivity activity;
    private LoginCallBack callBack;


    public LoginActivityDelegate(BaseActivity activity, LoginCallBack callBack) {
        this.activity = activity;
        this.callBack = callBack;
    }

    public void checkAccount(String account, String pwd) {
        if (Tools.isStringEmpty(account)) {
            ToastHelper.showShortToast(R.string.account_necessray);
            return;
        }

        if (Tools.isStringEmpty(pwd)) {
            ToastHelper.showShortToast(R.string.pwd_necessray);
            return;
        }

        //TODO request info

        if (callBack != null) {
            callBack.onLoginSuccess();
        }
    }

    public interface LoginCallBack extends BaseCallBackListener {
        void onLoginSuccess();
    }

}
