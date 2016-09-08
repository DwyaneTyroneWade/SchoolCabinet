package com.xiye.schoolcabinet;

import android.app.Application;

import com.android.volley.VolleyLog;
import com.xiye.schoolcabinet.utils.CrashHandler;
import com.xiye.sclibrary.base.C;
import com.xiye.sclibrary.base.Env;
import com.xiye.sclibrary.base.L;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;


public class App extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		C.set(this);
		loadEnv();

//		 TODO
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());
	}

	private void loadEnv(){
		VolleyLog.DEBUG = Env.LOGABLE;
	}


	public SerialPort getSerialPort(int parity, String path, int baudrate)
			throws SecurityException, IOException, InvalidParameterException {
		SerialPort mSerialPort = null;
		if (mSerialPort == null) {
			// String path = "/dev/ttyS4";
			// int baudrate = 9600;

			/* Check parameters */
			if ((path.length() == 0) || (baudrate == -1)) {
				throw new InvalidParameterException();
			}

			/* Open the serial port */
			L.d("App:parity=" + parity);
			mSerialPort = new SerialPort(new File(path), baudrate, 0, parity);// 2
																				// even
																				// 0
																				// none
		}
		return mSerialPort;
	}

	public void closeSerialPort(SerialPort mSerialPort) {
		if (mSerialPort != null) {
			mSerialPort.close();
			mSerialPort = null;
		}
	}
}
