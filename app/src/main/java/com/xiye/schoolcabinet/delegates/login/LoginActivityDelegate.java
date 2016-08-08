package com.xiye.schoolcabinet.delegates.login;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xiye.schoolcabinet.R;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.beans.Box;
import com.xiye.schoolcabinet.beans.BoxLogicItem;
import com.xiye.schoolcabinet.beans.CardInfo;
import com.xiye.schoolcabinet.manager.ConfigManager;
import com.xiye.schoolcabinet.manager.serialport.BoxLogicManager;
import com.xiye.schoolcabinet.utils.SCConstants;
import com.xiye.schoolcabinet.utils.StringUtils;
import com.xiye.schoolcabinet.utils.net.RequestFactory;
import com.xiye.schoolcabinet.utils.net.ServerConstants;
import com.xiye.sclibrary.dialog.DialogFactory;
import com.xiye.sclibrary.listener.BaseCallBackListener;
import com.xiye.sclibrary.net.volley.BaseResultBean;
import com.xiye.sclibrary.utils.ToastHelper;
import com.xiye.sclibrary.utils.Tools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wushuang on 6/25/16.
 */
public class LoginActivityDelegate {
    private BaseActivity activity;
    private LoginCallBack callBack;
    private String cardOrStudentIdForLogin;

    public LoginActivityDelegate(BaseActivity activity, LoginCallBack callBack) {
        this.activity = activity;
        this.callBack = callBack;
    }

    public void checkAccount(final String account, final String pwd, final String newPwd, final SCConstants.LoginType loginType) {
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

        cardOrStudentIdForLogin = ConfigManager.getRealCardIdByRealStudentId(account);
        if (Tools.isStringEmpty(cardOrStudentIdForLogin)) {
            cardOrStudentIdForLogin = account;
        }

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
                                doLogin(cardOrStudentIdForLogin, pwd, newPwd, loginType);
                            }

                            @Override
                            public void btnCancleOnClicklistener() {

                            }
                        });
                return;
            }
        }


        doLogin(cardOrStudentIdForLogin, pwd, newPwd, loginType);
    }

    private void doLogin(final String cardOrStudentId, String pwd, String newPwd, SCConstants.LoginType loginType) {
        if (activity == null) {
            return;
        }

        String chestNo = ConfigManager.getCabinetId();

        if (Tools.isStringEmpty(chestNo)) {
            //TODO 管理员不需要，老师和学生还是需要的，这个要后台自己判断
        } else if (Tools.isStringEmpty(cardOrStudentId)) {
            //TODO 这个也给后台去判断，究竟有没有这个人,这里的cardId可能是卡号，也可能是学号
        }

        activity.executeRequest(RequestFactory.getLoginRequest(chestNo, cardOrStudentId, pwd, newPwd, new Response.Listener<BaseResultBean>() {
            @Override
            public void onResponse(BaseResultBean bean) {
                if (bean != null && bean.err_no == ServerConstants.SUCCESS_CODE) {
                    if (callBack != null) {
                        callBack.onLoginSuccess(cardOrStudentId);
                    }
                } else {
                    if (bean != null) {
                        activity.processErrorCode(bean);
                    }

                    //TODO callback fail
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                activity.processVolleyError(volleyError);
                //call back fail
            }
        }));
    }

    public void openBox(String cardOrStudentId) {
        //TODO 通过cardId,找到cardInfo,或者直接让服务器登录成功后，把这个学号和密码，对应的cardInfo返回给客户端
        CardInfo cardInfo = ConfigManager.getCardInfoByCardId(cardOrStudentId);

        if (cardInfo == null) {
            cardInfo = ConfigManager.getCardInfoByStudentId(cardOrStudentId);
        }

        if (cardInfo != null) {
            List<Box> boxList = cardInfo.box;
            List<BoxLogicItem> boxLogicItemList = new ArrayList<>();
            if (boxList != null && boxList.size() > 0) {
                for (Box box : boxList) {
                    if (box != null) {
                        String cardId = cardInfo.realCardId;
                        String boxId = StringUtils.getRealBoxId(box.box_id, ConfigManager.getCabinetId());
                        BoxLogicItem item = new BoxLogicItem();
                        item.cardId = cardId;
                        item.boxId = boxId;
                        boxLogicItemList.add(item);
                    }
                }
            }
            BoxLogicManager.openBoxList(boxLogicItemList);
        }
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
        void onLoginSuccess(String cardOrStudentId);
    }

}
