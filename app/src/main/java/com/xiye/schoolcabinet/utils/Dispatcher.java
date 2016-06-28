package com.xiye.schoolcabinet.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.xiye.schoolcabinet.MainActivity;
import com.xiye.schoolcabinet.modules.admin.AdminActivity;
import com.xiye.schoolcabinet.modules.login.LoginActivity;

/**
 * Created by wushuang on 6/7/16.
 */
public class Dispatcher {
    public static void goAdmin(Context context, Bundle extras) {
        Intent intent = new Intent(context, AdminActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void goMain(Context context, Bundle extras) {
        Intent intent = new Intent(context, MainActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    public static void goLogin(Context context, Bundle extras) {
        Intent intent = new Intent(context, LoginActivity.class);
        if (extras != null) {
            intent.putExtras(extras);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }
}
