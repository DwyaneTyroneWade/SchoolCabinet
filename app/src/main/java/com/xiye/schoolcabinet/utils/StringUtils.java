package com.xiye.schoolcabinet.utils;

import com.xiye.sclibrary.utils.Tools;

import java.util.List;

/**
 * Created by wushuang on 8/2/16.
 */
public class StringUtils {
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
        //判断是不是这个柜子要远程开箱
        String cabinetCheck = boxIdFake.substring(0, cabinetId.length());

        if (cabinetId.equals(cabinetCheck)) {
            boxId = boxIdFake.substring(cabinetId.length(), boxIdFake.length());
        }

        return boxId;
    }

    public static boolean isStringExistInList(List<String> list, String str) {
        boolean isExist = false;
        if (list != null && list.size() > 0 && !Tools.isStringEmpty(str)) {
            if (list.contains(str)) {
                isExist = true;
            }
        }
        return isExist;
    }
}
