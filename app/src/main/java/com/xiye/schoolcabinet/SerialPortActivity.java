/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.xiye.schoolcabinet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.sclibrary.base.L;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;

public abstract class SerialPortActivity extends BaseActivity {

//	private final int SERIAL_PORT_TYPE_IC_INSIDE = 0;
	private final int SERIAL_PORT_TYPE_IC_OUTSIDE = 1;
	private final int SERIAL_PORT_TYPE_LOCK = 2;

	protected App mApplication;

	/*---------------- IC OUT----start---------------*/
	protected SerialPort mSerialPortICOutSide;
	protected OutputStream mOutputStreamICOutSide;
	private InputStream mInputStreamICOutSide;
	/*---------------- IC OUT ----end---------------*/

	/*----------------- IC  IN ----start---------------*/
//	protected SerialPort mSerialPortICInside;
//	protected OutputStream mOutputStreamICInside;
//	private InputStream mInputStreamICInside;
	/*----------------- IC  IN ----end---------------*/

	/*----------------- LOCK ----start---------------*/
	protected SerialPort mSerialPortLock;
	protected OutputStream mOutputStreamLock;
	private InputStream mInputStreamLock;
	/*----------------  LOCK -----end--------------*/


	private Thread thread_ic_outside;
//	private Thread thread_ic_inside;
	private Thread thread_lock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mApplication = (App) getApplication();
		try {
			initSerial();
			/* Create a receiving thread */
			runThread();

		} catch (SecurityException e) {
			// DisplayError(R.string.error_security);
			L.e(R.string.error_security);
			L.wtf("serial", R.string.error_security);
		} catch (IOException e) {
			// DisplayError(R.string.error_unknown);
			L.e(R.string.error_unknown);
			L.wtf("serial", R.string.error_unknown);
		} catch (InvalidParameterException e) {
			// DisplayError(R.string.error_configuration);
			L.e(R.string.error_configuration);
			L.wtf("serial", R.string.error_configuration);
		}
	}

	protected abstract void onICOutsideDataReceived(final byte[] buffer, final int size);

//	protected abstract void onICInsideDataReceived(final byte[] buffer, final int size);

	protected abstract void onLockDataReceived(final byte[] buffer, final int size);

	// protected abstract int setParity();

	@Override
	protected void onDestroy() {

		if (thread_ic_outside != null) {
            thread_ic_outside.interrupt();
		}
//		if (thread_ic_inside != null) {
//            thread_ic_inside.interrupt();
//		}
		if (thread_lock != null) {
            thread_lock.interrupt();
		}

		closeSerial();

		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void runThread() {
        thread_ic_outside = getThread(mInputStreamICOutSide, SERIAL_PORT_TYPE_IC_OUTSIDE);
        thread_ic_outside.start();

//        thread_ic_inside = getThread(mInputStreamICInside, SERIAL_PORT_TYPE_IC_INSIDE);
//        thread_ic_inside.start();

        thread_lock = getThread(mInputStreamLock, SERIAL_PORT_TYPE_LOCK);
        thread_lock.start();
	}

    private Thread getThread(final InputStream inputStream, final int type) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {
                    try {
                        runDataReceive(inputStream, type);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        return thread;
    }

	private void runDataReceive(InputStream inputStream, int type)
			throws IOException {
		int size;
		byte[] buffer = new byte[64];
		if (inputStream == null)
			return;
		size = inputStream.read(buffer);
//		L.d("serial port size:" + size);
		if (size > 0) {
			switch (type) {
			case SERIAL_PORT_TYPE_IC_OUTSIDE:
				onICOutsideDataReceived(buffer, size);
				break;
//			case SERIAL_PORT_TYPE_IC_INSIDE:
//				onICInsideDataReceived(buffer, size);
//				break;
			case SERIAL_PORT_TYPE_LOCK:
				onLockDataReceived(buffer, size);
				break;
			default:
				break;
			}

		}
	}

	private void initSerial() throws InvalidParameterException,
			SecurityException, IOException {
		initSerialPortICOutside();
//		initSerialPortICInSide();
		initSerialPortLock();
	}

    protected void initSerialPortICOutside() throws InvalidParameterException,
            SecurityException, IOException {
        mSerialPortICOutSide = mApplication.getSerialPort(0, "/dev/ttySAC0", 9600);
        mOutputStreamICOutSide = mSerialPortICOutSide.getOutputStream();
        mInputStreamICOutSide = mSerialPortICOutSide.getInputStream();
    }

//    protected void initSerialPortICInSide() throws InvalidParameterException,
//            SecurityException, IOException {
//        //TODO
//        mSerialPortICInside = mApplication.getSerialPort(0, "", 9600);
//        mOutputStreamICInside = mSerialPortICInside.getOutputStream();
//        mInputStreamICInside = mSerialPortICInside.getInputStream();
//    }

    protected void initSerialPortLock() throws InvalidParameterException,
            SecurityException, IOException {
        mSerialPortLock = mApplication.getSerialPort(0, "/dev/ttySAC3", 9600);
        mOutputStreamLock = mSerialPortLock.getOutputStream();
        mInputStreamLock = mSerialPortLock.getInputStream();
    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private void closeSerial() {
		mApplication.closeSerialPort(mSerialPortICOutSide);
        mSerialPortICOutSide = null;

//		mApplication.closeSerialPort(mSerialPortICInside);
//        mSerialPortICInside = null;

		mApplication.closeSerialPort(mSerialPortLock);
        mSerialPortLock = null;
	}

	private void DisplayError(int resourceId) {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Error");
		b.setMessage(resourceId);
		b.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				SerialPortActivity.this.finish();
			}
		});
		b.show();
	}
}
