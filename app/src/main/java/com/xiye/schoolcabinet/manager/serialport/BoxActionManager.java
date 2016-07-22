package com.xiye.schoolcabinet.manager.serialport;

import com.xiye.schoolcabinet.manager.ConfigManager;
import com.xiye.schoolcabinet.manager.NeedleManager;
import com.xiye.sclibrary.utils.SerialDataSendHelper;
import com.xiye.sclibrary.utils.Tools;
import com.xiye.sclibrary.utils.TypeUtil;

import java.io.OutputStream;

/**
 * Created by wushuang on 6/30/16.
 */
public class BoxActionManager {
    private OutputStream mOutputStreamLock;
    private String boxType;

    private BoxActionManager() {

    }

    public static BoxActionManager getInstance() {
        return Holder.INSTANCE;
    }

    public void setOutputStreamLock(OutputStream mOutputStreamLock) {
        this.mOutputStreamLock = mOutputStreamLock;
    }

    /**
     * 读取红外
     *
     * @param boxId 箱子编号
     */
    public void readRay(String boxId) {
        getBoxType();
        if (boxType.contains("B")) {
            sendCommand(obtainCommandForBoxTypeB(boxId, LockActionType.RAY));
        } else if (boxType.contains("A")) {
            sendCommand(obtainCommandForBoxTypeA(boxId, LockActionType.RAY));
        }
    }

    /**
     * 读取锁状态
     *
     * @param boxId
     */
    public void readLockStatus(String boxId) {
        getBoxType();
        if (boxType.contains("B")) {
            sendCommand(obtainCommandForBoxTypeB(boxId, LockActionType.READ));
        } else if (boxType.contains("A")) {
            sendCommand(obtainCommandForBoxTypeA(boxId, LockActionType.READ));
        }
    }

    /**
     * 开锁
     *
     * @param boxId
     */
    public void openLock(String boxId) {
        getBoxType();

        if (boxType.contains("B")) {
            sendCommand(obtainCommandForBoxTypeB(boxId, LockActionType.OPEN));
        } else if (boxType.contains("A")) {
            sendCommand(obtainCommandForBoxTypeA(boxId, LockActionType.OPEN));
        }
    }

    private void getBoxType() {
        if (Tools.isStringEmpty(boxType)) {
            boxType = ConfigManager.getBoxType();
            if (Tools.isStringEmpty(boxType)) {
                //default 固定箱 4*N
                boxType = "AF";
            }
        }
    }

    /**
     * 10个箱子 1+1+1+1...+1=10
     *
     * @return
     */
    private byte[] obtainCommandForBoxTypeB(String boxId, LockActionType actionType) {
        byte[] b = new byte[6];
        int boxIdInt = Integer.valueOf(boxId);
        //10-16
        b[0] = Byte.parseByte(Integer.toHexString(boxIdInt));

        switch (actionType) {
            case RAY:
                b[1] = (byte) 0xF0;
                break;
            case READ:
                b[1] = (byte) 0xF1;
                break;
            case OPEN:
                b[1] = (byte) 0xF2;
                break;
        }

        b[2] = (byte) 0x55;
        b[3] = 0x01;

        // sum
        int sum = 0;
        for (int i = 0; i < 4; i++) {
            sum += TypeUtil.byteToInt(b[i]);
        }

        if (sum > 255) {
            b[4] = (byte) (sum >> 8);
//            L.d("high:" + TypeUtil.byteToInt(b[4]));
            b[5] = (byte) ((sum << 8) >> 8);
//            L.d("low:" + TypeUtil.byteToInt(b[5]));
        } else {
            b[4] = (byte) 0x00;
            b[5] = TypeUtil.intToByte(sum);
        }

        return b;
    }

    /**
     * 4*N
     *
     * @param boxId
     * @param actionType
     * @return
     */
    private byte[] obtainCommandForBoxTypeA(String boxId, LockActionType actionType) {
        byte[] b = new byte[6];
        int boxIdInt = Integer.valueOf(boxId);
        int subCabinetNo = 0;//子柜号
        int lockNo = 0;//锁号

        //适配无数个箱子 4*N
        subCabinetNo = (int) Math.ceil(boxIdInt / 4f);
        lockNo = boxIdInt - 4 * (subCabinetNo - 1);

//<=14个子柜子，每个子柜4个箱子
//        if (boxIdInt <= 4) {
//            subCabinetNo = 1;
//            lockNo = boxIdInt - 4 * 0;
//        } else if (boxIdInt > 4 && boxIdInt <= 4 * 2) {
//            subCabinetNo = 2;
//            lockNo = boxIdInt - 4 * 1;
//        } else if (boxIdInt > 4 * 2 && boxIdInt <= 4 * 3) {
//            subCabinetNo = 3;
//            lockNo = boxIdInt - 4 * 2;
//        } else if (boxIdInt > 4 * 3 && boxIdInt <= 4 * 4) {
//            subCabinetNo = 4;
//            lockNo = boxIdInt - 4 * 3;
//        } else if (boxIdInt > 4 * 4 && boxIdInt <= 4 * 5) {
//            subCabinetNo = 5;
//            lockNo = boxIdInt - 4 * 4;
//        } else if (boxIdInt > 4 * 5 && boxIdInt <= 4 * 6) {
//            subCabinetNo = 6;
//            lockNo = boxIdInt - 4 * 5;
//        } else if (boxIdInt > 4 * 6 && boxIdInt <= 4 * 7) {
//            subCabinetNo = 7;
//            lockNo = boxIdInt - 4 * 6;
//        } else if (boxIdInt > 4 * 7 && boxIdInt <= 4 * 8) {
//            subCabinetNo = 8;
//            lockNo = boxIdInt - 4 * 7;
//        } else if (boxIdInt > 4 * 8 && boxIdInt <= 4 * 9) {
//            subCabinetNo = 9;
//            lockNo = boxIdInt - 4 * 8;
//        } else if (boxIdInt > 4 * 9 && boxIdInt <= 4 * 10) {
//            subCabinetNo = 10;
//            lockNo = boxIdInt - 4 * 9;
//        } else if (boxIdInt > 4 * 10 && boxIdInt <= 4 * 11) {
//            subCabinetNo = 11;
//            lockNo = boxIdInt - 4 * 10;
//        } else if (boxIdInt > 4 * 11 && boxIdInt <= 4 * 12) {
//            subCabinetNo = 12;
//            lockNo = boxIdInt - 4 * 11;
//        } else if (boxIdInt > 4 * 12 && boxIdInt <= 4 * 13) {
//            subCabinetNo = 13;
//            lockNo = boxIdInt - 4 * 12;
//        } else if (boxIdInt > 4 * 13 && boxIdInt <= 4 * 14) {
//            subCabinetNo = 14;
//            lockNo = boxIdInt - 4 * 13;
//        }

        if (lockNo == 1 || lockNo == 2) {
            b[0] = (byte) ((subCabinetNo - 1) * 2 + 1);
        } else if (lockNo == 3 || lockNo == 4) {
            b[0] = (byte) (subCabinetNo * 2);
        }

        switch (actionType) {
            case RAY:
                b[1] = (byte) 0xF0;
                break;
            case READ:
                b[1] = (byte) 0xF1;
                break;
            case OPEN:
                b[1] = (byte) 0xF2;
                break;
        }

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
            b[4] = (byte) (sum >> 8);
//            L.d("high:" + TypeUtil.byteToInt(b[4]));
            b[5] = (byte) ((sum << 8) >> 8);
//            L.d("low:" + TypeUtil.byteToInt(b[5]));
        } else {
            b[4] = (byte) 0x00;
            b[5] = TypeUtil.intToByte(sum);
        }

        return b;
    }


    private void sendCommand(final byte[] b) {
        NeedleManager.getBackgroundThreadExecutorForLock().execute(new Runnable() {

            @Override
            public void run() {
                SerialDataSendHelper.getInstance().sendCommand(mOutputStreamLock, b);
            }
        });
    }

    public enum LockActionType {
        READ, OPEN, RAY,
    }

    private static class Holder {
        static final BoxActionManager INSTANCE = new BoxActionManager();
    }

}
