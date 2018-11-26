package com.base.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:GuosongBai
 * Date:2018/11/8 14:22
 * Description:listView  GridView的baseAdapter
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {

    private List<T> mItems;
    private LayoutInflater mInflater;
    private Context mContext;

    public BaseListAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mItems = new ArrayList<>();
    }

    public BaseListAdapter(List<T> items, Context context) {
        mItems = items;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public T getItem(int position) {
        if (mItems != null && position > 0 && position < mItems.size()) {
            return mItems.get(position);
        }
        return null;
    }

    /**
     * 在OnItemClick时间中，为了避免HeaderView对position 造成的偏移，请用此方法获取Item
     */
    public T getItem(AdapterView<?> parent, int position) {
        if (parent.getAdapter() != null) {
            try {
                return (T) parent.getAdapter().getItem(position);
            } catch (ClassCastException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getItems() {
        return mItems;
    }

    public T getFirstItem() {
        return mItems != null && !mItems.isEmpty() ? mItems.get(0) : null;
    }

    public T getLastItem() {
        return mItems != null && !mItems.isEmpty() ? mItems.get(mItems.size() - 1) : null;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getView(mItems.get(position), position, convertView, parent);
    }

    public abstract View getView(T item, int position, View convertView, ViewGroup parent);

    public LayoutInflater getLayoutInflater() {
        return mInflater;
    }

    public CharSequence getText(int resId) {
        if (mContext == null) {
            return null;
        }
        return mContext.getText(resId);
    }

    public String getString(int resId) {
        if (mContext == null) {
            return null;
        }
        return mContext.getString(resId);
    }

    public void setItems(List<T> data) {
        if (data != null) {
            mItems.clear();
            mItems.addAll(data);
        }
    }

    public void clearData(){
        if (mItems != null){
            mItems.clear();
        }
    }

    public Context getContext() {
        return mContext;
    }
}

