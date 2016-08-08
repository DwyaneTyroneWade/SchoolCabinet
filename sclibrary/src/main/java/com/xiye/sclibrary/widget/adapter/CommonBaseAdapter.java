package com.xiye.sclibrary.widget.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonBaseAdapter<T> extends BaseAdapter {

    protected Context context;
    protected List<T> datas;

    protected LayoutInflater inflater;

    public CommonBaseAdapter(Context context) {
        this.context = context;
        this.datas = new ArrayList<T>();
        this.inflater = LayoutInflater.from(context);
    }

    public CommonBaseAdapter(Context context, List<T> _datas) {
        this.context = context;
        this.datas = new ArrayList<T>();
        datas.addAll(_datas);
        this.inflater = LayoutInflater.from(context);
    }

    public void addDatas(List<T> _datas) {
        this.datas.addAll(_datas);
        notifyDataSetChanged();
    }

    public void addDatas(List<T> _datas, int position) {
        this.datas.addAll(position, _datas);
        notifyDataSetChanged();
    }

    public void addData(T obj) {
        this.datas.add(obj);
        notifyDataSetChanged();
    }

    public void insertDatas(List<T> _datas, int p) {
        this.datas.addAll(p, _datas);
        notifyDataSetChanged();
    }

    public void insertData(T obj, int p) {
        this.datas.add(p, obj);
        notifyDataSetInvalidated();
    }

    public void removeData(int p) {
        this.datas.remove(p);
        notifyDataSetChanged();
    }

    public void removeData(T obj) {
        this.datas.remove(obj);
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> _datas) {
        datas.clear();
        datas.addAll(_datas);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return datas.size();
    }

    public void clear() {
        datas.clear();
        notifyDataSetChanged();
    }

    @Override
    public T getItem(int position) {
        // TODO Auto-generated method stub
        if (position < datas.size()) {
            return datas.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
}