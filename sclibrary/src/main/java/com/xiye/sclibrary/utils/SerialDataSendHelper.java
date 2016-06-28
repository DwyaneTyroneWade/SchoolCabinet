package com.xiye.sclibrary.utils;

import com.xiye.sclibrary.base.L;

import java.io.IOException;
import java.io.OutputStream;


public class SerialDataSendHelper {
    private SerialDataSendHelper() {

    }

    public static SerialDataSendHelper getInstance() {
        return Holder.INSTANCE;
    }

    public void sendCommand(OutputStream mOutputStream, String commandStr) {
        L.d("command", "commandStr:" + commandStr);
        if (Tools.isStringEmpty(commandStr)) {
            return;
        }

        byte[] buf = commandStr.getBytes();

        try {
            if (mOutputStream == null) {
                return;
            }
            mOutputStream.write(buf);
        } catch (IOException e) {
            L.e(e.toString());
        }
    }

    public void sendCommand(OutputStream mOutputStream, byte[] b) {
        if (b == null || b.length <= 0) {
            return;
        }

        try {
            if (mOutputStream == null) {
                return;
            }
            mOutputStream.write(b);
        } catch (IOException e) {
            L.e(e.toString());
        }
    }

    private static class Holder {
        static final SerialDataSendHelper INSTANCE = new SerialDataSendHelper();
    }
}
