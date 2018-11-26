package com.base.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:GuosongBai
 * Date:2018/11/8 14:25
 * Description:RecyclerViwe 基础Adapter
 */
public abstract class BaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private List<T> mData;
    private Context mContext;

    public BaseRecyclerViewAdapter() {
        mData = new ArrayList<>();
    }

    public BaseRecyclerViewAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    public BaseRecyclerViewAdapter(List<T> data, Context context) {
        mData = data;
        mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(final BaseViewHolder holder, final int position) {

        T item = null;
        if (mData != null) {
            item = mData.get(position);
        }
        if (clickable()) {
            holder.getConvertView().setClickable(true);
            T finalItem = item;
            holder.getConvertView().setOnClickListener(v -> onItemClick(v, finalItem, position));
        }

        onBindView(item, holder, holder.getLayoutPosition());

    }

    public abstract void onBindView(T t, BaseViewHolder holder, int position);

    @Override
    public int getItemViewType(int position) {
        return getLayoutID(position);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }

    public abstract int getLayoutID(int position);

    public abstract boolean clickable();

    public void onItemClick(View v, T item, int position) {
    }

    /**
     * 替换全部数据
     */
    public void setData(List<T> data) {
        if (data != null) {
            mData.clear();
            mData.addAll(data);
        }
    }

    /**
     * 追加数据
     */
    public void addData(List<T> data) {
        if (data != null) {
            mData.addAll(data);
        }
    }

    public void clearData() {
        if (mData != null) {
            mData.clear();
        }
    }

    public Context getContext() {
        return mContext;
    }

}
