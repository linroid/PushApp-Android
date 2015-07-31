package com.linroid.pushapp.ui.device;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linroid.pushapp.R;
import com.linroid.pushapp.ui.base.RefreshableFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceFragment extends RefreshableFragment {


    public DeviceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device, container, false);
    }


    @Override
    public RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter() {
        return null;
    }

    @Override
    public void loadData(int page) {

    }


}
