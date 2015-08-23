/*
 * Copyright (c) linroid 2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linroid.pushapp.ui.device;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.linroid.pushapp.App;
import com.linroid.pushapp.R;
import com.linroid.pushapp.api.DeviceService;
import com.linroid.pushapp.model.Device;
import com.linroid.pushapp.model.Pagination;
import com.linroid.pushapp.ui.base.RefreshableFragment;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class DeviceFragment extends RefreshableFragment {

    CompositeSubscription subscriptions = new CompositeSubscription();
    @Inject
    BriteDatabase db;
    @Inject
    DeviceService deviceApi;

    DeviceAdapter adapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new DeviceAdapter(getActivity());
        subscriptions.add(db.createQuery(Device.DB.TABLE_NAME, Device.DB.SQL_LIST_QUERY)
                        .map(Device.DB.MAP)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(adapter)
        );
        forceRefresh();
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
    protected int provideViewResourceId() {
        return R.layout.fragment_device;
    }

    @Override
    public void loadData(int page) {
        subscriptions.add(deviceApi.listDevice(page)
                .map(new Func1<Pagination<Device>, List<Device>>() {
                    @Override
                    public List<Device> call(Pagination<Device> pagination) {
                        return pagination.getData();
                    }
                })
                .flatMap(new Func1<List<Device>, Observable<Device>>() {
                    @Override
                    public Observable<Device> call(List<Device> devices) {
                        return Observable.from(devices);
                    }
                })
                .filter(new Func1<Device, Boolean>() {
                    @Override
                    public Boolean call(Device pack) {
                        Cursor cursor = db.query(Device.DB.SQL_ITEM_QUERY, String.valueOf(pack.getId()));
                        return !cursor.moveToNext();
                    }
                })
                .toList()
                .doOnNext(new Action1<List<Device>>() {
                    @Override
                    public void call(List<Device> devices) {
                        db.beginTransaction();
                        for (Device device : devices) {
                            db.insert(Device.DB.TABLE_NAME, device.toContentValues());
                        }
                        db.setTransactionSuccessful();
                        db.endTransaction();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Device>>() {

                    @DebugLog
                    @Override
                    public void onCompleted() {
                        loaderView.refreshLayout.setRefreshing(false);
                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                        loaderView.refreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @DebugLog
                    @Override
                    public void onNext(List<Device> devices) {
                    }

                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }
}
