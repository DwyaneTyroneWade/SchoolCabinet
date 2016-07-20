package com.xiye.schoolcabinet.manager.serialport;

import com.xiye.sclibrary.base.L;
import com.xiye.sclibrary.utils.Tools;
import com.xiye.sclibrary.utils.TypeUtil;

/**
 * Created by wushuang on 7/1/16.
 */
public class BoxLogicManager {
    public static final String TAG = BoxLogicManager.class.getSimpleName();

    public static void openBox(String boxId) {//循环3次开锁，每次延时300ms
        if (Tools.isStringEmpty(boxId)) {
            return;
        }
        //TODO
        BoxActionManager.getInstance().openLock(boxId);
    }


    public static void onSerialPortBack(byte[] buffer, int size) {
        String hexStr = TypeUtil.bytesToHex(Tools.getRealBuffer(buffer, size));
        L.d(TAG, "hexStr:" + hexStr);

        if (buffer.length > 1) {//地址＋结果码
            int resultNo = TypeUtil.byteToInt(buffer[1]);
            switch (resultNo) {
                case 0X59://开锁，成功
                    break;
                case 0X5E://开锁，失败
                    break;
                case 0X00://读取锁状态，门关闭
                    break;
                case 0X01://读取锁状态，门打开
                    break;
                case 0X02://红外，没有物体
                    break;
                case 0X03://红外，有物体
                    break;
            }
        }
    }
}
