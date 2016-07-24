package com.xiye.schoolcabinet.utils;

/**
 * Created by wushuang on 6/6/16.
 */
public class SCConstants {
    public static final String BUNDLE_KEY_CARD_ID = "card_id";

    public static final String BUNDLE_KEY_LOGIN_TYPE = "login_type";
//    public static final String BUNDLE_KEY_OPERATION_TYPE = "operation_type";

    public enum LoginType {
        ADMIN, STUDENT, TEACHER,
    }
}
