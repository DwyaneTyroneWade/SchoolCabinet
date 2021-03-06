package com.xiye.sclibrary.utils;

/**
 * Created by wushuang on 6/7/16.
 */
public class StringUtilsLib {
    /**
     * delete /r/n
     *
     * @param str
     * @return
     */
    public static String deleteLineBreaks(String str) {
        if (Tools.isStringEmpty(str)) {
            return "";
        }
        str = str.replace("\r\n", " ").replace("\n", " ").trim();
        return str;
    }
}
