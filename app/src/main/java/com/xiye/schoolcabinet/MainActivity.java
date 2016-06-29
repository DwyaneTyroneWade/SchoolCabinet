package com.xiye.schoolcabinet;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.xiye.schoolcabinet.delegates.MainActivityDelegate;
import com.xiye.schoolcabinet.dispatcher.ActivityDispatcher;
import com.xiye.schoolcabinet.manager.ConfigManager;
import com.xiye.schoolcabinet.utils.SCConstants;
import com.xiye.sclibrary.base.L;
import com.xiye.sclibrary.dialog.DialogFactory;
import com.xiye.sclibrary.utils.Tools;
import com.xiye.sclibrary.utils.TypeUtil;

public class MainActivity extends SerialPortActivity implements View.OnClickListener, View.OnLongClickListener, ConfigManager.GetAllCardInfoCallBack {
    private MainActivityDelegate mDelegate;
    private ImageView logo;
    private boolean hasGoVerifyId = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_boot);
//        setContentView(R.layout.activity_main);
        L.d("sw:" + Tools.getScreenWidth(this));
        L.d("sh:" + Tools.getScreenHeight(this));
        mDelegate = new MainActivityDelegate(this);
        mDelegate.setmOutputStreamLock(mOutputStreamLock);
//        getExtras();
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getBasicData();
    }

    private void getExtras() {
//        if (getIntent() != null && getIntent().getExtras() != null) {
//            Bundle extras = getIntent().getExtras();
//        }
    }

    private void initView() {
        findViewById(R.id.get).setOnClickListener(this);
        findViewById(R.id.save).setOnClickListener(this);
        logo = (ImageView) findViewById(R.id.logo);
        logo.setOnLongClickListener(this);
        findViewById(R.id.btn_no_card).setOnClickListener(this);
    }

    private void getBasicData() {
        String cabinetId = ConfigManager.getCabinetId();
        if (Tools.isStringEmpty(cabinetId)) {//保证只有在从系统运行app（关闭-->开启）的时候直接进入管理员界面
            if (!hasGoVerifyId) {
                Bundle extras = new Bundle();
                extras.putSerializable(SCConstants.BUNDLE_KEY_LOGIN_TYPE, SCConstants.LoginType.ADMIN);
                ActivityDispatcher.goLogin(this, extras);
                hasGoVerifyId = true;
            }
        } else {
            //每次onResume都会去去一次数据，理论上来说是不对的，应该只在验证ID的地方取一次，其他情况需要服务器通知
            ConfigManager.getAllCardInfo(this, this);
        }
    }

    @Override
    protected void onICOutsideDataReceived(byte[] buffer, int size) {
        String hexStr = TypeUtil.bytesToHex(Tools.getRealBuffer(buffer, size));
        L.d("onICOutsideDataReceived hexStr:" + hexStr);
        final String str = TypeUtil.hexStr2Str(hexStr);
        mDelegate.dealWithIC(str);
    }

    @Override
    protected void onLockDataReceived(byte[] buffer, int size) {
        L.wtf("wtf", "onLockDataReceived hexStr:" + TypeUtil.bytesToHex(Tools.getRealBuffer(buffer, size)));
        L.d("onLockDataReceived hexStr:" + TypeUtil.bytesToHex(Tools.getRealBuffer(buffer, size)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get:
                dealWithGetClick();
//                mDelegate.setType(MainActivityDelegate.OperationType.GET);
//                AlertDialog dialog = DialogFactory.getTipDialog(this, "请刷卡取物", new DialogFactory.OnTwoButtonClickListener() {
//                    @Override
//                    public void btnOkOnClicklistener() {
//                        mDelegate.setType(null);
//                    }
//
//                    @Override
//                    public void btnCancleOnClicklistener() {
//
//                    }
//                });
//                DialogFactory.showDialog(dialog);
                break;
            case R.id.save:
                mDelegate.setType(MainActivityDelegate.OperationType.SAVE);
                AlertDialog dialog2 = DialogFactory.getTipDialog(this, "请刷卡存物", new DialogFactory.OnTwoButtonClickListener() {

                    @Override
                    public void btnOkOnClicklistener() {
                        mDelegate.setType(null);
                    }

                    @Override
                    public void btnCancleOnClicklistener() {

                    }
                });
                DialogFactory.showDialog(dialog2);
                break;
            case R.id.btn_no_card:
                dealWithNoCardClick();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //TODO return
        super.onBackPressed();
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.logo:
                Bundle extras = new Bundle();
                extras.putSerializable(SCConstants.BUNDLE_KEY_LOGIN_TYPE, SCConstants.LoginType.ADMIN);
                ActivityDispatcher.goLogin(this, extras);
                return true;
        }
        return false;
    }

    private void dealWithGetClick() {
        Bundle extras = new Bundle();
        extras.putSerializable(SCConstants.BUNDLE_KEY_LOGIN_TYPE, SCConstants.LoginType.STUDENT);
        extras.putSerializable(SCConstants.BUNDLE_KEY_OPERATION_TYPE, MainActivityDelegate.OperationType.GET);
        ActivityDispatcher.goLogin(this, extras);
    }

    private void dealWithSaveClick() {
        Bundle extras = new Bundle();
        extras.putSerializable(SCConstants.BUNDLE_KEY_LOGIN_TYPE, SCConstants.LoginType.STUDENT);
        extras.putSerializable(SCConstants.BUNDLE_KEY_OPERATION_TYPE, MainActivityDelegate.OperationType.SAVE);
        ActivityDispatcher.goLogin(this, extras);
    }

    private void dealWithNoCardClick() {
        Bundle extras = new Bundle();
        extras.putSerializable(SCConstants.BUNDLE_KEY_LOGIN_TYPE, SCConstants.LoginType.STUDENT);
        ActivityDispatcher.goLogin(this, extras);
    }

    @Override
    public void onGetDataSuc() {
//        dismissLoading();
    }

    @Override
    public void onGetDataStart() {
//        showLoading();
    }

    @Override
    public void onGetDataFail() {
//        dismissLoading();
    }
}
