package com.linroid.pushapp.ui.pack;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;

import com.linroid.pushapp.App;
import com.linroid.pushapp.api.PackageService;
import com.linroid.pushapp.model.InstallPackage;
import com.linroid.pushapp.model.Pagination;
import com.linroid.pushapp.ui.base.InjectableActivity;
import com.linroid.pushapp.ui.base.RefreshableFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PackageFragment extends RefreshableFragment {
    public static final String STATE_PACKAGE = "package";
    @Inject
    PackageService packageApi;
    PackageAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PackageAdapter();
        if(savedInstanceState!=null){
            List<InstallPackage> savedPacks = savedInstanceState.getParcelableArrayList(STATE_PACKAGE);
            adapter.setData(savedPacks);
        } else {
            refresh();
        }
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_PACKAGE, (ArrayList<? extends Parcelable>) adapter.getData());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        App.from(activity).component().inject(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter() {
        return adapter;
    }

    @Override
    public void loadData(int page) {
        packageApi.listReceieved(page, new Callback<Pagination<InstallPackage>>() {
            @DebugLog
            @Override
            public void success(Pagination<InstallPackage> pagination, Response response) {
                adapter.setData(pagination.getData());
                loaderView.setPage(pagination.getCurrentPage(),
                        pagination.getLastPage());
            }

            @Override
            public void failure(RetrofitError error) {
                loaderView.notifyLoadFailed(error);
            }
        });
    }

}
