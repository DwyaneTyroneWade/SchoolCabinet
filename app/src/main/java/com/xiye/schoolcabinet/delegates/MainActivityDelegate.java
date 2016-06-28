package com.xiye.schoolcabinet.delegates;

import android.os.Bundle;

import com.xiye.schoolcabinet.MainActivity;
import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.beans.Card;
import com.xiye.schoolcabinet.database.manager.CardTableManager;
import com.xiye.schoolcabinet.helpers.BroadCastHelper;
import com.xiye.schoolcabinet.manager.DBManager;
import com.xiye.schoolcabinet.utils.ActivityStack;
import com.xiye.schoolcabinet.utils.Dispatcher;
import com.xiye.schoolcabinet.utils.SCConstants;
import com.xiye.sclibrary.base.L;
import com.xiye.sclibrary.net.needle.Needle;
import com.xiye.sclibrary.utils.SerialDataSendHelper;
import com.xiye.sclibrary.utils.StringUtils;
import com.xiye.sclibrary.utils.Tools;
import com.xiye.sclibrary.utils.TypeUtil;

import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by wushuang on 6/6/16.
 */
public class MainActivityDelegate {
    public static final String TAG = MainActivityDelegate.class.getSimpleName();
    //TODO for test
    public static final String ADMIN_ACCOUNT_1 = "1388340633";
    private OperationType type;
    private BaseActivity activity;
    private OutputStream mOutputStreamLock;

    public MainActivityDelegate() {

    }

    public MainActivityDelegate(BaseActivity activity) {
        this.activity = activity;
    }

    public OperationType getType() {
        return type;
    }

    public void setType(OperationType type) {
        this.type = type;
    }

    public void setmOutputStreamLock(OutputStream mOutputStreamLock) {
        this.mOutputStreamLock = mOutputStreamLock;
    }

    public void dealWithIC(String str) {
        str = StringUtils.deleteLineBreaks(str);
        L.d(TAG, "dealWithIC str:" + str);
        if (ADMIN_ACCOUNT_1.equals(str)) {
            Bundle extras = new Bundle();
            extras.putString(SCConstants.BUNDLE_KEY_CARD_ID, str);
            Dispatcher.goAdmin(activity, extras);
        } else {
            if (ActivityStack.getInstance().currentActivity() instanceof MainActivity) {
                doAction(str);
            } else {
                BroadCastHelper.sendOnICOutsideDataReceived(str, activity);
            }
        }
    }

    private void doAction(String cardId) {
        String cabinetId = getCabinetIdByCardId(cardId);
        if (!Tools.isStringEmpty(cabinetId)) {
            if (type != null) {
                switch (type) {
                    case SAVE:
                    case GET:
                        openLock(cabinetId);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private String getCabinetIdByCardId(String cardId) {
        ArrayList<Card> list = DBManager.getInstance().getList();
        if (list == null || list.size() <= 0) {
            list = new CardTableManager().get();
        }
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).cardId.equals(cardId)) {
                    return list.get(i).cabinetId;
                }
            }
        }

        return "";
    }

    private void openLock(String cabinetId) {
        sendCommand(obtainCommand(cabinetId));
    }

    private byte[] obtainCommand(String cabinetId) {
        byte[] b = new byte[6];
        //TODO
        int cabinetIdInt = Integer.valueOf(cabinetId);
        int cabinetNo = 0;//柜子号
        int lockNo = 0;//锁号
        if (cabinetIdInt <= 4) {
            cabinetNo = 1;
            lockNo = cabinetIdInt - 4 * 0;
        } else if (cabinetIdInt > 4 && cabinetIdInt <= 4 * 2) {
            cabinetNo = 2;
            lockNo = cabinetIdInt - 4 * 1;
        } else if (cabinetIdInt > 4 * 2 && cabinetIdInt <= 4 * 3) {
            cabinetNo = 3;
            lockNo = cabinetIdInt - 4 * 2;
        } else if (cabinetIdInt > 4 * 3 && cabinetIdInt <= 4 * 4) {
            cabinetNo = 4;
            lockNo = cabinetIdInt - 4 * 3;
        } else if (cabinetIdInt > 4 * 4 && cabinetIdInt <= 4 * 5) {
            cabinetNo = 5;
            lockNo = cabinetIdInt - 4 * 4;
        } else if (cabinetIdInt > 4 * 5 && cabinetIdInt <= 4 * 6) {
            cabinetNo = 6;
            lockNo = cabinetIdInt - 4 * 5;
        } else if (cabinetIdInt > 4 * 6 && cabinetIdInt <= 4 * 7) {
            cabinetNo = 7;
            lockNo = cabinetIdInt - 4 * 6;
        } else if (cabinetIdInt > 4 * 7 && cabinetIdInt <= 4 * 8) {
            cabinetNo = 8;
            lockNo = cabinetIdInt - 4 * 7;
        } else if (cabinetIdInt > 4 * 8 && cabinetIdInt <= 4 * 9) {
            cabinetNo = 9;
            lockNo = cabinetIdInt - 4 * 8;
        } else if (cabinetIdInt > 4 * 9 && cabinetIdInt <= 4 * 10) {
            cabinetNo = 10;
            lockNo = cabinetIdInt - 4 * 9;
        } else if (cabinetIdInt > 4 * 10 && cabinetIdInt <= 4 * 11) {
            cabinetNo = 11;
            lockNo = cabinetIdInt - 4 * 10;
        } else if (cabinetIdInt > 4 * 11 && cabinetIdInt <= 4 * 12) {
            cabinetNo = 12;
            lockNo = cabinetIdInt - 4 * 11;
        } else if (cabinetIdInt > 4 * 12 && cabinetIdInt <= 4 * 13) {
            cabinetNo = 13;
            lockNo = cabinetIdInt - 4 * 12;
        } else if (cabinetIdInt > 4 * 13 && cabinetIdInt <= 4 * 14) {
            cabinetNo = 14;
            lockNo = cabinetIdInt - 4 * 13;
        }

        if (lockNo == 1 || lockNo == 2) {
            b[0] = (byte) ((cabinetNo - 1) * 2 + 1);
        } else if (lockNo == 3 || lockNo == 4) {
            b[0] = (byte) (cabinetNo * 2);
        }
        b[1] = (byte) 0xF2;
        b[2] = (byte) 0x55;
        if (lockNo % 2 == 0) {
            b[3] = 0x04;
        } else {
            b[3] = 0x01;
        }

        // sum
        int sum = 0;
        for (int i = 0; i < 4; i++) {
            sum += TypeUtil.byteToInt(b[i]);
        }

        if (sum > 255) {
            // TODO
            b[4] = (byte) (sum >> 8);
            L.d("high:" + TypeUtil.byteToInt(b[4]));
            b[5] = (byte) ((sum << 8) >> 8);
            L.d("low:" + TypeUtil.byteToInt(b[5]));
        } else {
            b[4] = (byte) 0x00;
            b[5] = TypeUtil.intToByte(sum);
        }

        return b;
    }

    private void sendCommand(final byte[] b) {
        Needle.onBackgroundThread().withTaskType(SCConstants.NEEDLE_TYPE_OPEN_LOCK)
                .serially().execute(new Runnable() {

            @Override
            public void run() {
                SerialDataSendHelper.getInstance().sendCommand(mOutputStreamLock, b);
            }
        });
    }


    public enum OperationType {
        SAVE,
        GET,
    }
}
