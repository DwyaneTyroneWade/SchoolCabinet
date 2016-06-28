package com.xiye.schoolcabinet.helpers;

import android.content.Intent;

import com.xiye.schoolcabinet.base.BaseActivity;
import com.xiye.schoolcabinet.utils.SCConstants;

/**
 * Created by wushuang on 6/7/16.
 */
public class BroadCastHelper {
    public static void sendOnICOutsideDataReceived(String cardId, BaseActivity activity) {
        Intent broadcast = new Intent(SCConstants.ACTION_ON_CARD_OUTSIDE_READ);
        broadcast.putExtra(SCConstants.BUNDLE_KEY_CARD_ID, cardId);
        activity.sendBroadcast(broadcast);
    }
}
