package com.xiye.schoolcabinet.manager.serialport;

import android.os.Handler;
import android.os.Message;

import com.xiye.schoolcabinet.R;
import com.xiye.sclibrary.base.C;
import com.xiye.sclibrary.base.L;
import com.xiye.sclibrary.utils.ToastHelper;
import com.xiye.sclibrary.utils.Tools;
import com.xiye.sclibrary.utils.TypeUtil;

/**
 * Created by wushuang on 7/1/16.
 */
public class BoxLogicManager {
    public static final String TAG = BoxLogicManager.class.getSimpleName();

    private static final long DELAY_TIME = 300;
    private static final long DELAY_TIME_CHECK = 60 * 1000;//60s

    private static final int MSG_OPEN_LOCK_FAIL = 0;
    private static final int MSG_OPEN_LOCK_SUC = 1;
    private static final int MSG_CHECK_DOOR_HAS_CLOSED = 2;
    private static final int MSG_CHECK_DOOR_STILL_OPEN = 3;

    private static ReadPurpose mReadPurpose;
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            final int boxId = (int) msg.obj;

            switch (msg.what) {
                //TODO 上传至服务器
                case MSG_OPEN_LOCK_FAIL:
                    ToastHelper.showShortToast(C.get().getString(R.string.open_lock_fail, boxId));
                    break;
                case MSG_OPEN_LOCK_SUC:
                    ToastHelper.showShortToast(C.get().getString(R.string.open_lock_suc, boxId));
                    //1分钟之后，读状态
                    checkDoorClosedWith1MinDelay(String.valueOf(boxId));
                    break;
                case MSG_CHECK_DOOR_HAS_CLOSED:
                    break;
                case MSG_CHECK_DOOR_STILL_OPEN:
                    //1分钟之后，读状态
                    checkDoorClosedWith1MinDelay(String.valueOf(boxId));
                    break;
            }
        }
    };

    public static void openBox(final String boxId) {//循环3次开锁，每次延时300ms
        if (Tools.isStringEmpty(boxId)) {
            return;
        }

        // 连续3次发送开锁指令，每次间隔300ms;
        // 然后延时300ms 去读取 锁的状态：
        // 如果锁打开，提示 开箱成功;
        // 如果锁关闭，提示箱子损坏，请找管理员开箱（涉及到多箱子同时开的情况，最好提示里能加上箱子编号）
        //过1分钟 去读取此箱子的状态...

        for (int i = 0; i < 3; i++) {
            if (i > 0) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sendOpenLockCommand(boxId);
                    }
                }, DELAY_TIME * i);
            } else {
                sendOpenLockCommand(boxId);
            }
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendReadLockStatusCommand(boxId, ReadPurpose.CHECK_OPEN);
            }
        }, DELAY_TIME * 3);

    }

    public static void onSerialPortBack(byte[] buffer, int size) {
        String hexStr = TypeUtil.bytesToHex(Tools.getRealBuffer(buffer, size));
        L.d(TAG, "onSerialPortBack [hexStr]:" + hexStr);

        if (buffer.length > 1) {//地址＋结果码
            int resultNo = TypeUtil.byteToInt(buffer[1]);
            //得到箱子编号
            int boxId = TypeUtil.byteToInt(buffer[0]);
            L.d(TAG, "onSerialPortBack [boxId]:" + boxId);
            switch (resultNo) {
                //这个开锁成功是给锁通电成功，锁通电门不一定开，还是要读的
                case 0X59://开锁，成功
                    break;
                case 0X5E://开锁，失败
                    break;
                case 0X00://读取锁状态，门关闭
                    switch (mReadPurpose) {
                        case CHECK_CLOSE:
                            //门关闭
                            mHandler.sendMessage(obtainMMessage(MSG_CHECK_DOOR_HAS_CLOSED, boxId));
                            break;
                        case CHECK_OPEN:
                            //提示箱子＋编号损坏，请找管理员开箱
                            mHandler.sendMessage(obtainMMessage(MSG_OPEN_LOCK_FAIL, boxId));
                            break;
                    }
                    break;
                case 0X01://读取锁状态，门打开
                    switch (mReadPurpose) {
                        case CHECK_CLOSE:
                            //过1分钟再去读取一次门的状态
                            mHandler.sendMessage(obtainMMessage(MSG_CHECK_DOOR_STILL_OPEN, boxId));
                            break;
                        case CHECK_OPEN:
                            //提示开箱成功
                            mHandler.sendMessage(obtainMMessage(MSG_OPEN_LOCK_SUC, boxId));
                            break;
                    }
                    break;
                case 0X02://红外，没有物体
                    break;
                case 0X03://红外，有物体
                    break;
            }
        }
    }

    /**
     * 发送开锁指令
     */
    private static void sendOpenLockCommand(String boxId) {
        L.d(TAG, "sendOpenLockCommand [boxId]:" + boxId);
        BoxActionManager.getInstance().openLock(boxId);
    }

    private static void sendReadLockStatusCommand(String boxId, ReadPurpose purpose) {
        L.d(TAG, "sendReadLockStatusCommand [purpose]:" + purpose + "[boxId]:" + boxId);
        mReadPurpose = purpose;
        BoxActionManager.getInstance().readLockStatus(boxId);
    }

    private static void checkDoorClosedWith1MinDelay(final String boxId) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendReadLockStatusCommand(boxId, ReadPurpose.CHECK_CLOSE);
            }
        }, DELAY_TIME_CHECK);
    }


    private static Message obtainMMessage(int what, Object obj) {
        Message msg = mHandler.obtainMessage();
        msg.what = what;
        msg.obj = obj;
        return msg;
    }

    public enum ReadPurpose {
        CHECK_OPEN, CHECK_CLOSE,
    }
}
