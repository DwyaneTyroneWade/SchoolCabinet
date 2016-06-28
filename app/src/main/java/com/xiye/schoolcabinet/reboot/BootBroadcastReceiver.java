package com.xiye.schoolcabinet.reboot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xiye.schoolcabinet.MainActivity;

public class BootBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent != null) {
			// if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			Intent boot = new Intent(context, MainActivity.class);
			boot.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(boot);
			// }
		}
	}

}