package com.xiye.sclibrary.utils;

/**
 * Created by wushuang on 6/7/16.
 */
public class StringUtils {
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

    /**
     * 箱子编号＝柜子编号＋箱子号
     *
     * @param boxIdFake
     * @param cabinetId
     * @return
     */
    public static String getRealBoxId(String boxIdFake, String cabinetId) {
        String boxId = "";
        if (Tools.isStringEmpty(cabinetId) || Tools.isStringEmpty(boxIdFake)) {
            return boxId;
        }

        boxId = boxIdFake.substring(cabinetId.length(), boxIdFake.length());
        return boxId;
    }

}
