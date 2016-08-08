package com.xiye.schoolcabinet.modules.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xiye.schoolcabinet.R;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.beans.CardInfoBean;
import com.xiye.schoolcabinet.delegates.admin.AdminActivityDelegate;
import com.xiye.schoolcabinet.manager.ConfigManager;
import com.xiye.schoolcabinet.utils.SCConstants;
import com.xiye.sclibrary.utils.ToastHelper;
import com.xiye.sclibrary.utils.Tools;

/**
 * Created by wushuang on 6/7/16.
 */
public class AdminActivity extends BaseActivity implements View.OnClickListener, AdminActivityDelegate.AdminActivityCallBack, ConfigManager.GetAllCardInfoCallBack {
    private AdminActivityDelegate mDelegate;
    //    BroadcastReceiver receiver = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent == null) {
//                return;
//            }
//            String action = intent.getAction();
//            if (Tools.isStringEmpty(action)) {
//                return;
//            }
//
//            if (BroadCastDispatcher.ACTION_ON_CARD_OUTSIDE_READ.equals(action)) {
//                String cardId = intent.getStringExtra(SCConstants.BUNDLE_KEY_CARD_ID);
//                if (adminAction == AdminAction.REGISTER) {
//                    mDelegate.register(cardId);
//                }
//            }
//        }
//    };
    private String adminId;

    private Button btnBack, btnVerifyId, btnOpenAll, btnStatus, btnSingle;
    private EditText etVerifyId, etStudentOrBoxId;

//    private void registerReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BroadCastDispatcher.ACTION_ON_CARD_OUTSIDE_READ);
//        registerReceiver(receiver, filter);
//    }
//
//    private void unregisterReceiver() {
//        unregisterReceiver(receiver);
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        unregisterReceiver();
    }

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_admin);
        mDelegate = new AdminActivityDelegate(this, this);
        getExtras();
        initView();
        initUI();
    }

    private void getExtras() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            adminId = getIntent().getExtras().getString(SCConstants.BUNDLE_KEY_CARD_ID);
        } else {
            //TODO 可能存在ADMINID为空的情况 比如第一次进入
            adminId = "admin";
        }
    }

    private void initView() {
        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);

        etVerifyId = (EditText) findViewById(R.id.et_cabinet_id);
        etStudentOrBoxId = (EditText) findViewById(R.id.et_student_or_box_number);

        btnOpenAll = (Button) findViewById(R.id.btn_open_all);
        btnSingle = (Button) findViewById(R.id.btn_open_single_box);
        btnSingle.setOnClickListener(this);
        btnOpenAll.setOnClickListener(this);

        btnVerifyId = (Button) findViewById(R.id.btn_verify_id);
        btnStatus = (Button) findViewById(R.id.btn_status_confirm);
        btnVerifyId.setOnClickListener(this);
        btnStatus.setOnClickListener(this);

        setClassInfo(ConfigManager.getClassName());
    }


    private void initUI() {
        String cabinetId = ConfigManager.getCabinetId();
        if (!Tools.isStringEmpty(cabinetId)) {
            etVerifyId.setText(cabinetId);
            etVerifyId.setEnabled(false);
            btnVerifyId.setEnabled(false);
            btnVerifyId.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        String cabinetId = ConfigManager.getCabinetId();
        if (!Tools.isStringEmpty(cabinetId)) {
            super.onBackPressed();
        } else {
            ToastHelper.showShortToast(R.string.cabinet_id_necessary);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                String cabinetId = ConfigManager.getCabinetId();
                if (!Tools.isStringEmpty(cabinetId)) {
                    this.finish();
                } else {
                    ToastHelper.showShortToast(R.string.cabinet_id_necessary);
                }
                break;
            case R.id.btn_open_all:
                if (!Tools.isStringEmpty(ConfigManager.getCabinetId())) {
                    mDelegate.openAllLock(adminId);
                } else {
                    ToastHelper.showShortToast(R.string.cabinet_id_necessary);
                }
                break;
            case R.id.btn_open_single_box:
                if (!Tools.isStringEmpty(ConfigManager.getCabinetId())) {
                    String studentOrBoxId = etStudentOrBoxId.getText().toString();
                    mDelegate.openSingleLock(studentOrBoxId, adminId);
                } else {
                    ToastHelper.showShortToast(R.string.cabinet_id_necessary);
                }
                break;
            case R.id.btn_verify_id:
                mDelegate.verifyId(etVerifyId.getText().toString(), this);
                break;
            case R.id.btn_status_confirm:
                if (!Tools.isStringEmpty(ConfigManager.getCabinetId())) {
                    //TODO
                } else {
                    ToastHelper.showShortToast(R.string.cabinet_id_necessary);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onGetDataSuc(CardInfoBean bean) {
        dismissLoading();
        ToastHelper.showShortToast(R.string.verify_cabinet_id_suc);
        updateUI();
        //第一次注册完，自动返回首页
        this.finish();
    }

    @Override
    public void onGetDataStart() {
        showLoading(R.string.verify_ing);
    }

    @Override
    public void onGetDataFail() {
        dismissLoading();
        ToastHelper.showShortToast(R.string.verify_cabinet_id_fail);
    }

    private void updateUI() {
        etVerifyId.setEnabled(false);
        btnVerifyId.setEnabled(false);
        btnVerifyId.setVisibility(View.GONE);
    }
}
