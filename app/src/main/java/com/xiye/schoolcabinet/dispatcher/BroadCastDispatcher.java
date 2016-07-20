package com.xiye.schoolcabinet.dispatcher;

import android.content.Intent;

import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.utils.SCConstants;

/**
 * Created by wushuang on 6/7/16.
 */
public class BroadCastDispatcher {
    public static final String ACTION_ON_CARD_OUTSIDE_READ = "com.xiye.schoolcabinet.ON_CARD_OUTSIDE_READ";

    public static final String ACTION_OPEN_BOX = "com.xiye.schoolcabinet.OPEN_BOX";

    public static void sendOnICOutsideDataReceived(String cardId, BaseActivity activity) {
        Intent broadcast = new Intent(ACTION_ON_CARD_OUTSIDE_READ);
        broadcast.putExtra(SCConstants.BUNDLE_KEY_CARD_ID, cardId);
        activity.sendBroadcast(broadcast);
    }
}
