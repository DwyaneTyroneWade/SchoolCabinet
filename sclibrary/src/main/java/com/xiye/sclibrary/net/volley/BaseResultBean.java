package com.xiye.sclibrary.net.volley;


import com.xiye.sclibrary.serializable.Key;

public class BaseResultBean {

    public int err_no = -1;
	public String err_msg;
	@Key(key="err_no")
	public String errNoString;
}
