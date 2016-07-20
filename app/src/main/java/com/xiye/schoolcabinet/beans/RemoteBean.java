package com.xiye.schoolcabinet.beans;

import com.xiye.sclibrary.net.volley.BaseResultBean;

/**
 * Created by wushuang on 7/2/16.
 */
public class RemoteBean extends BaseResultBean{
    public String results;//去除柜子编号之后，得到的箱子号，如果是00，表示不远程开箱,不是00，表示开箱
}
