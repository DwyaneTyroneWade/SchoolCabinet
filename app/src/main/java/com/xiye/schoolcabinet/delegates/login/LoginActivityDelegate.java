package com.xiye.schoolcabinet.delegates.login;

import com.xiye.schoolcabinet.R;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.utils.SCConstants;
import com.xiye.sclibrary.dialog.DialogFactory;
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

    public void checkAccount(String account, String pwd, String newPwd, SCConstants.LoginType loginType) {
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
        } else {
            if (!checkPwdLength(pwd)) {
                ToastHelper.showShortToast(R.string.pwd_rule);
                return;
            }
        }

        if (Tools.isStringEmpty(newPwd)) {
            newPwd = "000000";//default 服务器会判断为不需要修改密码
        } else {
            if (!checkPwdLength(newPwd)) {
                ToastHelper.showShortToast(R.string.new_pwd_rule);
                return;
            } else {
                if (newPwd.equals(pwd)) {
                    DialogFactory.showSecondConfirmDialog(activity, activity.getString(R.string.tip),
                            activity.getString(R.string.new_pwd_equal_old),
                            activity.getString(R.string.confirm),
                            activity.getString(R.string.cancel),
                            new DialogFactory.OnTwoButtonClickListener() {
                                @Override
                                public void btnOkOnClicklistener() {
                                    //TODO request info
                                    //TODO test code
                                    if (callBack != null) {
                                        callBack.onLoginSuccess();
                                    }
                                }

                                @Override
                                public void btnCancleOnClicklistener() {

                                }
                            });
                    return;
                }
            }
        }

        //TODO request info
        //TODO test code
        if (callBack != null) {
            callBack.onLoginSuccess();
        }
    }

    public void openBox() {
        //TODO 通过cardId,找到cardInfo,或者直接让服务器登录成功后，把这个学号和密码，对应的cardInfo返回给客户端

    }

    /**
     * 必须是6位数字
     *
     * @param pwd
     */
    private boolean checkPwdLength(String pwd) {
        if (Tools.isNumeric(pwd)) {
            if (pwd.length() != 6) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }


    public interface LoginCallBack extends BaseCallBackListener {
        void onLoginSuccess();
    }

}
