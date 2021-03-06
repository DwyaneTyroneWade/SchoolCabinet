package com.xiye.schoolcabinet.manager.serialport;

import android.os.Handler;
import android.os.Message;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.xiye.schoolcabinet.R;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.beans.BoxLogicItem;
import com.xiye.schoolcabinet.beans.Record;
import com.xiye.schoolcabinet.database.manager.RecordTableManager;
import com.xiye.schoolcabinet.manager.ConfigManager;
import com.xiye.schoolcabinet.manager.NeedleManager;
import com.xiye.schoolcabinet.utils.ActivityStack;
import com.xiye.schoolcabinet.utils.net.RequestFactory;
import com.xiye.sclibrary.base.C;
import com.xiye.sclibrary.base.L;
import com.xiye.sclibrary.net.needle.UiRelatedTask;
import com.xiye.sclibrary.net.volley.BaseResultBean;
import com.xiye.sclibrary.timer.DelayTimer;
import com.xiye.sclibrary.utils.TimeUtils;
import com.xiye.sclibrary.utils.ToastHelper;
import com.xiye.sclibrary.utils.Tools;
import com.xiye.sclibrary.utils.TypeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wushuang on 7/1/16.
 */
public class BoxLogicManager {
    public static final String TAG = BoxLogicManager.class.getSimpleName();
    public static final int BOX_DOOR_STATUS_CLOSE = 0;
    public static final int BOX_DOOR_STATUS_OPEN = 1;
    public static final int BOX_DOOR_STATUS_DAMAGE = 2;

    public static final int BOX_IS_FILLED_FALSE = 0;
    public static final int BOX_IF_FILLED_TRUE = 1;
    public static final int BOX_IS_FILLED_UNKNOW = 2;

    private static final long DELAY_TIME = 600;
    private static final long DELAY_TIME_CHECK = 60 * 1000;//60s

    private static final int MSG_OPEN_LOCK_FAIL = 0;
    private static final int MSG_OPEN_LOCK_SUC = 1;
    private static final int MSG_CHECK_DOOR_HAS_CLOSED = 2;
    private static final int MSG_CHECK_DOOR_STILL_OPEN = 3;
    private static final int MSG_DATA_TRANS_ERROR = 4;
    private static final int MSG_DATA_TRANS_TIME_OUT = 5;

    private static int currBoxId;
    private static int openTimes = 0;
    private static ReadPurpose mReadPurpose;
    private static OnOpenLockListener mOnOpenLockListener;
    //    private static List<String> boxIdToOpenList = new ArrayList<>();
    private static List<BoxLogicItem> boxLogicItemToOpenList = new ArrayList<>();
    private static boolean isProcessing = false;
    private static DelayTimer timeoutTimer = new DelayTimer(new DelayTimer.OnTimeToFinishActivityListener() {
        @Override
        public void onTimeToFinishActivity() {
            if (isProcessing) {
                mHandler.sendMessage(obtainMMessage(MSG_DATA_TRANS_TIME_OUT, currBoxId));
            }
        }
    }, 2000);//超时时间2S
    private static OnOpenBoxProcessListener mProcessListener = new OnOpenBoxProcessListener() {
        @Override
        public void onOpenProcessStart() {
            timeoutTimer.startTimer();
            isProcessing = true;
        }

        @Override
        public void onOpenProcessRetry() {
            timeoutTimer.cancelTimer();
        }

        @Override
        public void onOpenProcessEnd() {
            timeoutTimer.cancelTimer();

            if (boxLogicItemToOpenList != null && boxLogicItemToOpenList.size() > 0) {
                boxLogicItemToOpenList.remove(0);
            }

            isProcessing = false;

            openListTop();
        }
    };
    private static Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            final int boxId = (int) msg.obj;

            switch (msg.what) {
                //TODO 上传至服务器
                case MSG_OPEN_LOCK_FAIL:
                    if (openTimes < 3) {
                        if (mProcessListener != null) {
                            mProcessListener.onOpenProcessRetry();
                        }
                        openBox(String.valueOf(boxId));
                    } else {
                        openTimes = 0;
                        ToastHelper.showShortToast(C.get().getString(R.string.open_lock_fail, boxId));
                        //TODO 检测到门坏了，改变本地数据库，上传服务器
                        updateRecordDB(String.valueOf(boxId), getCardId(), BOX_DOOR_STATUS_DAMAGE, BOX_IS_FILLED_UNKNOW);

                        if (mOnOpenLockListener != null) {
                            mOnOpenLockListener.onOpenFail(String.valueOf(boxId));
                        }

                        if (mProcessListener != null) {
                            mProcessListener.onOpenProcessEnd();
                        }
                    }
                    break;
                case MSG_OPEN_LOCK_SUC:
                    openTimes = 0;
                    ToastHelper.showShortToast(C.get().getString(R.string.open_lock_suc, boxId));
                    //TODO 门打开，改变本地数据库，上传服务器
                    updateRecordDB(String.valueOf(boxId), getCardId(), BOX_DOOR_STATUS_OPEN, BOX_IS_FILLED_UNKNOW);

                    if (mOnOpenLockListener != null) {
                        mOnOpenLockListener.onOpenSuc(String.valueOf(boxId));
                    }

                    if (mProcessListener != null) {
                        mProcessListener.onOpenProcessEnd();
                    }

                    //1分钟之后，读状态
                    checkDoorClosedWith1MinDelay(String.valueOf(boxId));
                    break;
                case MSG_CHECK_DOOR_HAS_CLOSED:
                    //TODO 检测到门关上了，改变本地数据库，上传服务器
                    updateRecordDB(String.valueOf(boxId), getCardId(), BOX_DOOR_STATUS_CLOSE, BOX_IS_FILLED_UNKNOW);
                    break;
                case MSG_CHECK_DOOR_STILL_OPEN:
                    //TODO 是不是应该增加一个长时间不关门报警的机制
                    //1分钟之后，读状态
                    checkDoorClosedWith1MinDelay(String.valueOf(boxId));
                    break;
                case MSG_DATA_TRANS_ERROR:
                case MSG_DATA_TRANS_TIME_OUT:
                    //TODO 统一给个TOAST
                    openTimes = 0;

                    if (mOnOpenLockListener != null) {
                        mOnOpenLockListener.onOpenFail(String.valueOf(boxId));
                    }

                    if (mProcessListener != null) {
                        mProcessListener.onOpenProcessEnd();
                    }
                    break;
            }
        }
    };

    public static void setmOnOpenLockListener(OnOpenLockListener onOpenLockListener) {
        mOnOpenLockListener = onOpenLockListener;
    }

    public static void openBoxList(List<BoxLogicItem> boxLogicItemsList) {
        if (boxLogicItemsList != null && boxLogicItemsList.size() > 0) {
            //check contains
            for (BoxLogicItem item : boxLogicItemsList) {
                if (item != null) {
                    if (!isItemExist(boxLogicItemToOpenList, item)) {
                        boxLogicItemToOpenList.add(item);
                    }
                }
            }
        }

        openListTop();
    }

    public static void openBoxSingle(BoxLogicItem boxLogicItem) {
        if (boxLogicItem != null && !Tools.isStringEmpty(boxLogicItem.boxId)) {
            //check contains
            if (!isItemExist(boxLogicItemToOpenList, boxLogicItem)) {
                boxLogicItemToOpenList.add(boxLogicItem);
            }
        }

        openListTop();
    }

    private static void openListTop() {
        if (isProcessing) {
            return;
        }
        if (boxLogicItemToOpenList != null && boxLogicItemToOpenList.size() > 0 && boxLogicItemToOpenList.get(0) != null) {
            openBox(boxLogicItemToOpenList.get(0).boxId);
        }
    }

    private static void openBox(final String boxId) {
        if (Tools.isStringEmpty(boxId)) {
            return;
        }

        //TODO 发送开锁指令
        //延时600ms读取锁状态
        // 如果锁打开，提示 开箱成功;
        // 如果锁关闭，重新开箱，
        // （最多三次,三次都关闭，提示箱子损坏，请找管理员开箱（涉及到多箱子同时开的情况，最好提示里能加上箱子编号）），延时读取锁状态
        //过1分钟 去读取此箱子的状态...

        sendOpenLockCommand(boxId);
        openTimes++;

        if (mOnOpenLockListener != null) {
            mOnOpenLockListener.onOpenStart(boxId);
        }

        if (mProcessListener != null) {
            mProcessListener.onOpenProcessStart();
        }

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                sendReadLockStatusCommand(boxId, ReadPurpose.CHECK_OPEN);
            }
        }, DELAY_TIME);

        //TIMEOUT_TIMER要用到(因为板子返回的板子地址，不一定是boxId,所以都用这个currBoxId进行操作)
        currBoxId = Integer.parseInt(boxId);

//        for (int i = 0; i < 3; i++) {
//            if (i > 0) {
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        sendOpenLockCommand(boxId);
//                    }
//                }, DELAY_TIME * i);
//            } else {
//                sendOpenLockCommand(boxId);
//            }
//        }

//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                sendReadLockStatusCommand(boxId, ReadPurpose.CHECK_OPEN);
//            }
//        }, DELAY_TIME * 3);
        //TODO 读锁超时，或者 读不到 应有的结果：1.读状态很快，50ms以内返回，否则就是没有收到你的指令，不会返回 2.正常不会有其他返回值，除非数据传输错误
        //如果你传过来的数据有错误，我是不会有返回值的
        //TODO 增加LOADING
        //TODO 每次间隔1S
    }

    public static void onSerialPortBack(byte[] buffer, int size) {
        String hexStr = TypeUtil.bytesToHex(Tools.getRealBuffer(buffer, size));
        L.d(TAG, "onSerialPortBack [hexStr]:" + hexStr);

        if (buffer.length > 1) {//地址＋结果码
            int resultNo = TypeUtil.byteToInt(buffer[1]);
            //得到箱子编号
            // 返回的是第一个板子地址
            int boxId = currBoxId;
            L.d(TAG, "onSerialPortBack [currentBoxId]:" + currBoxId);
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
                            //
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
                default:
                    //数据传输错误
                    mHandler.sendMessage(obtainMMessage(MSG_DATA_TRANS_ERROR, boxId));
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

    private static boolean isItemExist(List<BoxLogicItem> boxLogicItemsList, BoxLogicItem item) {
        boolean isExist = false;
        if (boxLogicItemsList != null && boxLogicItemsList.size() > 0 && item != null) {
            if (boxLogicItemsList.contains(item)) {
                isExist = true;
            }
        }
        return isExist;
    }

    /**
     * @param boxId
     * @param cardId
     * @param boxDoorStatus 表示格子状态，1表示打开，0表示关闭， 2表示格子损坏
     * @param boxIsFilled   表示格子里面是否有物体，1表示有物体，0表示没有物体
     */

    private static void updateRecordDB(String boxId, String cardId, int boxDoorStatus, int boxIsFilled) {
        //prepareRecord
        final Record record = new Record();
        record.cardId = cardId;
        record.cabinetId = ConfigManager.getCabinetId();
        record.boxId = boxId;
        record.boxDoorStatus = String.valueOf(boxDoorStatus);
        record.boxIsFilled = String.valueOf(boxIsFilled);
        record.operationTime = TimeUtils.getDateTimeyyyy_MM_dd_HH_mm_ss();

        NeedleManager.getBackgroundThreadExecutorForRecord().execute(new UiRelatedTask<Object>() {
            @Override
            protected Object doWork() {
                RecordTableManager.save(record);
                return null;
            }

            @Override
            protected void thenDoUiRelatedWork(Object o) {
                reportBoxStatusToServer(record);
            }
        });
    }

    private static void reportBoxStatusToServer(Record record) {
        BaseActivity activity = (BaseActivity) ActivityStack.getInstance().currentActivity();
        if (activity == null) {
            return;
        }

        activity.executeRequest(RequestFactory.getReportBoxStatusRequest(record, new Response.Listener<BaseResultBean>() {
            @Override
            public void onResponse(BaseResultBean baseResultBean) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }));
    }

    private static String getCardId() {
        String cardId = "";
        if (boxLogicItemToOpenList != null && boxLogicItemToOpenList.size() > 0 && boxLogicItemToOpenList.get(0) != null) {
            cardId = boxLogicItemToOpenList.get(0).cardId;
        }
        return cardId;
    }

    public enum ReadPurpose {
        CHECK_OPEN, CHECK_CLOSE,
    }

    public interface OnOpenBoxProcessListener {
        void onOpenProcessStart();

        void onOpenProcessRetry();

        void onOpenProcessEnd();
    }

    public interface OnOpenLockListener {
        void onOpenStart(String boxId);

        void onOpenSuc(String boxId);

        void onOpenFail(String boxId);
    }


}
