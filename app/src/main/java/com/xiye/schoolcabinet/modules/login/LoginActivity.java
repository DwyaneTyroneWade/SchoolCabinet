package com.xiye.schoolcabinet.modules.login;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xiye.schoolcabinet.R;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.delegates.login.LoginActivityDelegate;
import com.xiye.schoolcabinet.dispatcher.ActivityDispatcher;
import com.xiye.schoolcabinet.manager.ConfigManager;
import com.xiye.schoolcabinet.utils.SCConstants;
import com.xiye.sclibrary.timer.DelayTimerWithRunnable;
import com.xiye.sclibrary.utils.Tools;
import com.xiye.sclibrary.widget.edittext.DeleteableEditText;

/**
 * Created by wushuang on 6/25/16.
 */
public class LoginActivity extends BaseActivity implements View.OnFocusChangeListener, DelayTimerWithRunnable.OnTimeRunnableListener, View.OnClickListener, LoginActivityDelegate.LoginCallBack, DelayTimerWithRunnable.OnTimeToFinishActivityListener {
    private SCConstants.LoginType loginType;
    private TextView tvEditTitle, tvAccount, tvCD;
    private DeleteableEditText etAccount, etPwd, etNewPwd;
    private Button btnConfirm, btnBack;

    private LoginActivityDelegate mDelegate;

    private DelayTimerWithRunnable mDelayTimer;
    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mDelayTimer != null) {
                mDelayTimer.resetTimer();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_login);
        mDelayTimer = new DelayTimerWithRunnable(this, 30 * 1000);
        mDelayTimer.setTimeRunnableListener(this);
        mDelayTimer.startTimer();
        mDelegate = new LoginActivityDelegate(this, this);
        getExtras();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDelayTimer != null) {
            mDelayTimer.cancelTimer();
        }
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
        etNewPwd = (DeleteableEditText) findViewById(R.id.et_new_pwd);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
        btnBack = (Button) findViewById(R.id.btn_back);
        btnBack.setVisibility(View.VISIBLE);
        btnBack.setOnClickListener(this);
        tvCD = (TextView) findViewById(R.id.tv_cd);
        etAccount.addTextChangedListener(mTextWatcher);
        etPwd.addTextChangedListener(mTextWatcher);
        etNewPwd.addTextChangedListener(mTextWatcher);
        etAccount.setOnFocusChangeListener(this);
        etPwd.setOnFocusChangeListener(this);
        etNewPwd.setOnFocusChangeListener(this);

        switch (loginType) {
            case STUDENT:
                initStudentView();
                break;
            case ADMIN:
            case TEACHER:
            default:
                initAdminView();
                break;
        }
        setClassInfo(ConfigManager.getClassName());
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
                if (mDelayTimer != null) {
                    mDelayTimer.resetTimer();
                }
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
        String newPwd = etNewPwd.getText().toString();
        if (Tools.isStringEmpty(newPwd)) {
            newPwd = "000000";//default 服务器会判断为不需要修改密码
        }
        mDelegate.checkAccount(account, pwd, newPwd, loginType);
    }

    @Override
    public void onLoginSuccess(String cardOrStudentId) {
        dealWithLoginSuc(cardOrStudentId);
    }

    @Override
    public void onGetDataStart() {

    }

    @Override
    public void onGetDataFail() {

    }

    private void dealWithLoginSuc(String cardOrStudentId) {
        switch (loginType) {
            case ADMIN:
            case TEACHER:
            default:
                //TODO 记录,交给服务器去记录，本地只记录箱子状态
                ActivityDispatcher.goAdmin(this, null);
                this.finish();
                break;
            case STUDENT:
                //TODO 开箱，记录开箱数据
                mDelegate.openBox(cardOrStudentId);
                this.finish();
                break;
        }
    }

    @Override
    public void onTimeToFinishActivity() {
        this.finish();
    }

    @Override
    public void onTimerun(String time) {
        if (tvCD != null) {
            tvCD.setText(time);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (mDelayTimer != null) {
                mDelayTimer.resetTimer();
            }
        }
    }
}
