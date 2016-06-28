package com.xiye.schoolcabinet.manager;

import com.xiye.schoolcabinet.beans.Card;

import java.util.ArrayList;

/**
 * Created by wushuang on 6/7/16.
 */
public class DBManager {
    private ArrayList<Card> list;

    private DBManager() {

    }

    public static DBManager getInstance() {
        return Holder.INSTANCE;
    }

    public ArrayList<Card> getList() {
        return list;
    }

    public void setList(ArrayList<Card> list) {
        this.list = list;
    }

    private static class Holder {
        static final DBManager INSTANCE = new DBManager();
    }
}
