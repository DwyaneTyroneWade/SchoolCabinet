package com.xiye.schoolcabinet.beans;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wushuang on 6/28/16.
 */
public class CardInfoResults implements Serializable{
    public List<CardInfo> card_info;
    public String school;
    public String className;
    public String total;
    public String url;
}
