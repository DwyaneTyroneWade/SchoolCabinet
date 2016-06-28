package com.xiye.schoolcabinet.modules.admin;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiye.schoolcabinet.R;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.delegates.admin.AdminActivityDelegate;
import com.xiye.schoolcabinet.utils.SCConstants;
import com.xiye.sclibrary.dialog.DialogFactory;
import com.xiye.sclibrary.utils.Tools;

/**
 * Created by wushuang on 6/7/16.
 */
public class AdminActivity extends BaseActivity implements View.OnClickListener, AdminActivityDelegate.AdminActivityCallBack {
    private AdminActivityDelegate mDelegate;
    private AdminAction adminAction;
    BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            if (Tools.isStringEmpty(action)) {
                return;
            }

            if (SCConstants.ACTION_ON_CARD_OUTSIDE_READ.equals(action)) {
                String cardId = intent.getStringExtra(SCConstants.BUNDLE_KEY_CARD_ID);
                if (adminAction == AdminAction.REGISTER) {
                    mDelegate.register(cardId);
                }
            }
        }
    };
    private String adminId;
    private TextView tvID;

    private Button btnBack, btnVerifyId, btnOpenAll, btnStatus, btnSingle;
    private EditText etVerifyId, etStudentOrBoxId;

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(SCConstants.ACTION_ON_CARD_OUTSIDE_READ);
        registerReceiver(receiver, filter);
    }

    private void unregisterReceiver() {
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver();
    }

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.layout_admin);
        mDelegate = new AdminActivityDelegate(this, this);
        getExtras();
        initView();
    }

    private void getExtras() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            adminId = getIntent().getExtras().getString(SCConstants.BUNDLE_KEY_CARD_ID);
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
    }


//    private void initView() {
//        findViewById(R.id.register).setOnClickListener(this);
//        findViewById(R.id.open).setOnClickListener(this);
//
//        tvID = (TextView) findViewById(R.id.admin_id);
//        if (!Tools.isStringEmpty(adminId)) {
//            tvID.setText(adminId);
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register:
                adminAction = AdminAction.REGISTER;
                AlertDialog dialog = DialogFactory.getTipDialog(this, "请刷卡注册");
                DialogFactory.showDialog(dialog);
                break;
            case R.id.open:
                break;
            case R.id.btn_back:
                this.finish();
                break;
            case R.id.btn_open_all:
                //TODO
                break;
            case R.id.btn_open_single_box:
                //TODO
                break;
            default:
                break;
        }
    }

    @Override
    public void onRegisterSuc() {
        adminAction = null;
        AlertDialog dialog = DialogFactory.getTipDialog(this, "注册成功");
        DialogFactory.showDialog(dialog);
    }


    private enum AdminAction {
        REGISTER,
        OPEN,
    }
}
