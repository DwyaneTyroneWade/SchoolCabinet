package com.xiye.schoolcabinet.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wushuang on 6/28/16.
 */
public class CardInfoResults implements Serializable {
    public List<CardInfo> card_info;
    public String school;
    public String className;
    public String total;
    public String url;
    public String boxtype;
    //柜子型号 AF AR BF BR
    //A代表2路卡，B代表单路卡     对应不同的开锁指令
    //F代表固定柜，R代表随机柜     显示不同的内容及刷卡开箱流程
}
