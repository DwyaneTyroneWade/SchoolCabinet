package com.xiye.schoolcabinet.delegates.login;

import com.xiye.schoolcabinet.R;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.utils.SCConstants;
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

    public void checkAccount(String account, String pwd, SCConstants.LoginType loginType) {
        if (Tools.isStringEmpty(account)) {
            switch (loginType) {
                case STUDENT:
                    ToastHelper.showShortToast(R.string.student_number_necessary);
                    break;
                case ADMIN:
                case TEACHER:
                default:
                    ToastHelper.showShortToast(R.string.account_necessary);
                    break;
            }
            return;
        }

        if (Tools.isStringEmpty(pwd)) {
            ToastHelper.showShortToast(R.string.pwd_necessary);
            return;
        }

        //TODO request info

        if (callBack != null) {
            callBack.onLoginSuccess();
        }
    }

    public void openBox() {
        //TODO 通过cardId,找到cardInfo,或者直接让服务器登录成功后，把这个学号和密码，对应的cardInfo返回给客户端

    }


    public interface LoginCallBack extends BaseCallBackListener {
        void onLoginSuccess();
    }

}
