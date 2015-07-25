package com.linroid.pushapp.ui.push;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.linroid.pushapp.ui.base.RefreshableFragment;

public class PushFragment extends RefreshableFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter() {
        return new PushAdapter();
    }
}
