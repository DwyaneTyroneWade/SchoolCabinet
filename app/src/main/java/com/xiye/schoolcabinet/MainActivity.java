package com.xiye.schoolcabinet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiye.schoolcabinet.beans.CardInfoBean;
import com.xiye.schoolcabinet.delegates.MainActivityDelegate;
import com.xiye.schoolcabinet.dispatcher.ActivityDispatcher;
import com.xiye.schoolcabinet.manager.ConfigManager;
import com.xiye.schoolcabinet.utils.SCConstants;
import com.xiye.sclibrary.base.L;
import com.xiye.sclibrary.utils.Tools;
import com.xiye.sclibrary.utils.TypeUtil;

public class MainActivity extends SerialPortActivity implements View.OnClickListener, View.OnLongClickListener, MainActivityDelegate.RemoteFromBackstageCallback, ConfigManager.GetAllCardInfoCallBack {
    public final static int MESSAGE_ON_IC_OUTSIDE_DATA_RECEIVED = 1;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) {
                return;
            }
            String action = intent.getAction();
            if (Tools.isStringEmpty(action)) {
                return;
            }

            //TODO
        }
    };
    private MainActivityDelegate mDelegate;
    private ImageView logo;
    private RelativeLayout rlBg;
    private TextView tvNotice;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_ON_IC_OUTSIDE_DATA_RECEIVED:
                    String str = TypeUtil.hexStr2Str((String) msg.obj);
//                    ToastHelper.showShortToast("卡hexStr" + (String) msg.obj);
//                    3848297037  0994927685
                    //TODO test code
                    if (str.contains("384")) {
                        str = "3848297037";
                    } else if (str.contains("099")) {
                        str = "0994927685";
                    } else {
                        break;
                    }

                    mDelegate.dealWithIC(str);
                    break;
            }
        }
    };

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        //TODO

        registerReceiver(receiver, filter);
    }

    private void unregisterReceiver() {
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main_boot);
//        setContentView(R.layout.activity_main);
        L.d("sw:" + Tools.getScreenWidth(this));
        L.d("sh:" + Tools.getScreenHeight(this));
        mDelegate = new MainActivityDelegate(this, mOutputStreamLock, this);
//        getExtras();
        initView();
//        registerReceiver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDelegate.getBasicData(this);
        mDelegate.startGetRemoteFromBackStage();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDelegate.pauseGetRemoteFromBackStage();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unregisterReceiver();
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
        rlBg = (RelativeLayout) findViewById(R.id.main_bg);
        tvNotice = (TextView) findViewById(R.id.tv_notice);
//        findViewById(R.id.btn_no_card).setOnClickListener(this);
    }

    @Override
    protected void onICOutsideDataReceived(byte[] buffer, int size) {
        String hexStr = TypeUtil.bytesToHex(Tools.getRealBuffer(buffer, size));
        L.d("onICOutsideDataReceived hexStr:" + hexStr);
        Message msg = mHandler.obtainMessage();
        msg.what = MESSAGE_ON_IC_OUTSIDE_DATA_RECEIVED;
        msg.obj = hexStr;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void onLockDataReceived(byte[] buffer, int size) {
//        L.wtf("wtf", "onLockDataReceived hexStr:" + TypeUtil.bytesToHex(Tools.getRealBuffer(buffer, size)));
        L.d("onLockDataReceived hexStr:" + TypeUtil.bytesToHex(Tools.getRealBuffer(buffer, size)));
        //TODO

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.get:
//                dealWithGetClick();
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
//                break;
//            case R.id.save:
//                mDelegate.setType(MainActivityDelegate.OperationType.SAVE);
//                AlertDialog dialog2 = DialogFactory.getTipDialog(this, "请刷卡存物", new DialogFactory.OnTwoButtonClickListener() {
//
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
//                DialogFactory.showDialog(dialog2);
//                break;
            case R.id.btn_no_card_teacher:
            case R.id.btn_no_card_admin:
                dealWithNoCardClick(SCConstants.LoginType.ADMIN);
                break;
            case R.id.btn_no_card_student:
                dealWithNoCardClick(SCConstants.LoginType.STUDENT);
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

//    private void dealWithGetClick() {
//        Bundle extras = new Bundle();
//        extras.putSerializable(SCConstants.BUNDLE_KEY_LOGIN_TYPE, SCConstants.LoginType.STUDENT);
//        extras.putSerializable(SCConstants.BUNDLE_KEY_OPERATION_TYPE, MainActivityDelegate.OperationType.GET);
//        ActivityDispatcher.goLogin(this, extras);
//    }
//
//    private void dealWithSaveClick() {
//        Bundle extras = new Bundle();
//        extras.putSerializable(SCConstants.BUNDLE_KEY_LOGIN_TYPE, SCConstants.LoginType.STUDENT);
//        extras.putSerializable(SCConstants.BUNDLE_KEY_OPERATION_TYPE, MainActivityDelegate.OperationType.SAVE);
//        ActivityDispatcher.goLogin(this, extras);
//    }

    private void dealWithNoCardClick(SCConstants.LoginType type) {
        Bundle extras = new Bundle();
        extras.putSerializable(SCConstants.BUNDLE_KEY_LOGIN_TYPE, type);
        ActivityDispatcher.goLogin(this, extras);
    }

    @Override
    public void onGetDataSuc(CardInfoBean bean) {
//        dismissLoading();
        fillData(bean);
    }

    @Override
    public void onGetDataStart() {
//        showLoading();
    }

    @Override
    public void onGetDataFail() {
//        dismissLoading();
    }

    private void fillData(CardInfoBean bean) {
        if (rlBg.getBackground() == null) {
            Drawable d = mDelegate.getBg();
            if (d != null) {
                rlBg.setBackgroundDrawable(d);
            }
        }

        if (bean.results != null) {
            String school = bean.results.school;
            String classname = bean.results.className;
            StringBuilder builder = new StringBuilder();
            builder.append(school);
            builder.append(classname);
            setClassInfo(builder.toString());
        }
    }

    @Override
    public void onNotice(String notice) {
        if (tvNotice != null) {
            if (Tools.isStringEmpty(notice)) {
                tvNotice.setText("");
            } else {
                tvNotice.setText(notice);
            }
        }
    }
}
