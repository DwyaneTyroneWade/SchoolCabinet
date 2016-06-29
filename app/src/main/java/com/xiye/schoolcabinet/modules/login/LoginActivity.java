package com.xiye.schoolcabinet.modules.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiye.schoolcabinet.R;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.delegates.login.LoginActivityDelegate;
import com.xiye.schoolcabinet.dispatcher.ActivityDispatcher;
import com.xiye.schoolcabinet.utils.SCConstants;
import com.xiye.sclibrary.widget.edittext.DeleteableEditText;

/**
 * Created by wushuang on 6/25/16.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginActivityDelegate.LoginCallBack {
    private SCConstants.LoginType loginType;
    private TextView tvEditTitle, tvAccount;
    private DeleteableEditText etAccount, etPwd;
    private Button btnConfirm, btnBack;

    private LoginActivityDelegate mDelegate;


    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_login);
        mDelegate = new LoginActivityDelegate(this, this);
        getExtras();
        initView();
    }

    private void getExtras() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            loginType = (SCConstants.LoginType) extras.getSerializable(SCConstants.BUNDLE_KEY_LOGIN_TYPE);
        }
    }

    private void initView() {
        tvEditTitle = (TextView) findViewById(R.id.tv_edit_title);
        tvAccount = (TextView) findViewById(R.id.tv_account);
        etAccount = (DeleteableEditText) findViewById(R.id.et_account);
        etPwd = (DeleteableEditText) findViewById(R.id.et_pwd);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);

        switch (loginType) {
            case STUDENT:
                initStudentView();
                break;
            case ADMIN:
            default:
                initAdminView();
                break;
        }
    }

    private void initStudentView() {
        tvEditTitle.setText(getString(R.string.edit_title_student));
        tvAccount.setText(getString(R.string.student_number));
    }

    private void initAdminView() {
        tvEditTitle.setText(getString(R.string.edit_title_admin));
        tvAccount.setText(getString(R.string.account));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                checkAccount();
                break;
            case R.id.btn_back:
                this.finish();
                break;
        }
    }

    private void checkAccount() {
        String account = etAccount.getText().toString();
        String pwd = etPwd.getText().toString();
        mDelegate.checkAccount(account, pwd);
    }

    @Override
    public void onLoginSuccess() {
        dealWithLoginSuc();
    }

    @Override
    public void onGetDataStart() {

    }

    @Override
    public void onGetDataFail() {

    }

    private void dealWithLoginSuc() {
        switch (loginType) {
            case ADMIN:
            default:
                //TODO
                ActivityDispatcher.goAdmin(this, null);
                this.finish();
                break;
            case STUDENT:
                //TODO 开箱，记录开箱数据
                this.finish();
                break;
        }
    }
}
