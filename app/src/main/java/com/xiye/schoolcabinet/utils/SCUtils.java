package com.xiye.schoolcabinet.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wushuang on 6/6/16.
 */
public class SCUtils {
    public static Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<String, String>();

        headers.put("Host", ServerConstants.SERVER_URL.substring(
                "http://".length(), ServerConstants.SERVER_URL.length()));

        return headers;
    }
}
