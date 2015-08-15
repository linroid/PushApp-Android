package com.linroid.pushapp.ui.base;

import android.support.v7.widget.RecyclerView;

import java.util.List;

import rx.functions.Action1;
import timber.log.Timber;

/**
 * Created by linroid on 7/20/15.
 */
public abstract class DataAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements Action1<List<T>> {
    protected List<T> data;


    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void insertItem(int location, T item) {
        data.add(location, item);
        notifyItemInserted(location);
    }

    public void insertItem(T item) {
        data.add(this.data.size(), item);
    }

    public void set(int location, T item) {
        data.set(location, item);
        notifyItemChanged(location);
    }

    public void remove(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void removeItem(T item) {
        for (int i=0; i<=this.data.size(); i++) {
            if(data.get(i)==item) {
                data.remove(i);
                notifyItemRemoved(i);
            }
        }
    }

    @Override
    public void call(List<T> ts) {
        Timber.d("数据发生变化");
        this.data = ts;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public List<T> getData() {
        return data;
    }
}
