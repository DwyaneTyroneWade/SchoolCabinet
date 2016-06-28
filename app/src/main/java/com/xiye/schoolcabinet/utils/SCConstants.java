package com.xiye.schoolcabinet.utils;

/**
 * Created by wushuang on 6/6/16.
 */
public class SCConstants {
    public static final String BUNDLE_KEY_CARD_ID = "card_id";
    public static final String BUNDLE_KEY_IS_CABINET_ID_VERIFIED = "is_cabinet_id_verified";
    public static final String BUNDLE_KEY_LOGIN_TYPE = "login_type";
    public static final String BUNDLE_KEY_OPERATION_TYPE = "operation_type";

    public static final String ACTION_ON_CARD_OUTSIDE_READ = "com.xiye.schoolcabinet.ON_CARD_OUTSIDE_READ";

    public static final String NEEDLE_TYPE_OPEN_LOCK = "needle_open_lock";

    public static final String PREFERENCE_KEY_CABINET_ID = "cabinet_id";


    public enum LoginType {
        ADMIN, STUDENT,
    }
}
