package com.linroid.pushapp.ui.base;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.linroid.pushapp.model.Push;

import java.util.List;

/**
 * Created by linroid on 7/20/15.
 */
public abstract class DataAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<T> data;


    public void setData(List<T> data){
        this.data = data;
        notifyDataSetChanged();
    }
    public void insertItem(int location, T item){
        data.add(location, item);
        notifyItemInserted(location);
    }
    public void insertItem(T item){
        data.add(this.data.size(), item);
    }
    public void set(int location, T item){
        data.set(location, item);
        notifyItemChanged(location);
    }
    public void delete(int location){
        data.remove(location);
        notifyItemRemoved(location);
    }

    @Override
    public int getItemCount() {
        return data==null ? 0 : data.size();
    }

    public List<T> getData() {
        return data;
    }
}
