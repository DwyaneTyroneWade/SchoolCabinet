package com.xiye.schoolcabinet.utils.net;

public class ServerConstants {

    //TODO
    public static String SERVER_URL = "http://121.43.168.53:8080";
    //学校内网地址
//    public static String SERVER_URL = "http://192.168.121.9:8080";

    public static int SUCCESS_CODE = 0;

    //获取所有数据（本柜子的所有信息+本学校的管理员信息+厂商的管理员信息）
    public static String URL_GET_ALL_CARD_INFO = "/SchoolChestData/schoolchest/door/getDataByChestNo/";

    //远程开箱
    public static String URL_REMOTE_FROM_BACK_STAGE = "/SchoolChestData/schoolchest/door/getDataByNothing";

    //上传数据
    public static String URL_SEND_BOX_OPERATION = "/SchoolChestData/schoolchest/record";

    //登录
    public static String URL_LOGIN = "/SchoolChestData/schoolchest/door/getResultByPassword/";

    //上传远程开箱结束
    public static String URL_REPORT_REMOTE_END = "/SchoolChestData/schoolchest/door/postDoorData/";

    //上传箱子记录(开，关，坏)
    public static String URL_REPORT_BOX_STATUS = "/SchoolChestData/schoolchest/door/postDataByChestNo/";
}
