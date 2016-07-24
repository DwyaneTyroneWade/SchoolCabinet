package com.xiye.schoolcabinet.delegates.admin;

import com.xiye.schoolcabinet.R;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.database.manager.CardTableManager;
import com.xiye.schoolcabinet.manager.ConfigManager;
import com.xiye.schoolcabinet.manager.serialport.BoxLogicManager;
import com.xiye.sclibrary.utils.ToastHelper;
import com.xiye.sclibrary.utils.Tools;

/**
 * Created by wushuang on 6/7/16.
 */
public class AdminActivityDelegate {
    private BaseActivity activity;
    private AdminActivityCallBack callBack;
    private CardTableManager dbManager;

    private AdminActivityDelegate() {

    }

    public AdminActivityDelegate(BaseActivity activity, AdminActivityCallBack callBack) {
        this.activity = activity;
        this.callBack = callBack;
    }

    /**
     * 验证ID 只验证一次（首次打开APP时）服务器要注意不能重复
     *
     * @param cabinetId
     * @param callBack
     */
    public void verifyId(String cabinetId, ConfigManager.GetAllCardInfoCallBack callBack) {
        if (Tools.isStringEmpty(cabinetId)) {
            ToastHelper.showShortToast(R.string.cabinet_id_necessary);
            return;
        }

        ConfigManager.getAllCardInfo(cabinetId, activity, callBack);
    }

    public void openAllLock() {
        int boxTotal = ConfigManager.getBoxTotal();
        if (boxTotal > 0) {
            for (int i = 1; i <= boxTotal; i++) {
                BoxLogicManager.openBox(String.valueOf(i));
            }
        } else {
            ToastHelper.showShortToast(activity.getString(R.string.server_data_unusual));
        }
    }

    public void openSingleLock(String studentOrBoxId) {
        //TODO 暂时只能开柜子号，学号与柜子号的联系未知
        if (Tools.isStringEmpty(studentOrBoxId)) {
            ToastHelper.showShortToast(R.string.open_single_box_id_necessary);
        } else {
            int boxId = Integer.parseInt(studentOrBoxId);
            int boxTotal = ConfigManager.getBoxTotal();
            if (boxId > boxTotal) {
                ToastHelper.showShortToast(R.string.open_single_box_id_real);
            } else {
                BoxLogicManager.openBox(studentOrBoxId);
            }
        }
    }

    public interface AdminActivityCallBack {
    }
}
