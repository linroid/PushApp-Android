package com.linroid.pushapp.ui.device;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.linroid.pushapp.App;
import com.linroid.pushapp.model.Device;
import com.linroid.pushapp.ui.base.DataAdapter;
import com.linroid.pushapp.ui.base.RefreshableFragment;
import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Inject;

import rx.android.app.AppObservable;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class DeviceFragment extends RefreshableFragment {

    CompositeSubscription subscriptions = new CompositeSubscription();
    @Inject
    BriteDatabase db;
    DeviceAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new DeviceAdapter();
        subscriptions.add(AppObservable.bindSupportFragment(this, db.createQuery(Device.DB.TABLE_NAME, Device.DB.SQL_LIST_QUERY))
                        .map(Device.DB.MAP)
                        .subscribeOn(Schedulers.io())
                        .subscribe(adapter)
        );
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        App.from(activity).component().inject(this);
    }
    @Override
    public RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter() {
        return adapter;
    }

    @Override
    public void loadData(int page) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }
}
