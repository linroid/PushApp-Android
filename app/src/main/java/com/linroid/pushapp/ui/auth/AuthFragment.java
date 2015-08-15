package com.linroid.pushapp.ui.auth;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.linroid.pushapp.App;
import com.linroid.pushapp.api.AuthService;
import com.linroid.pushapp.model.Auth;
import com.linroid.pushapp.model.Pagination;
import com.linroid.pushapp.ui.base.RefreshableFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * TODO 添加数据库支持
 * Created by linroid on 8/15/15.
 */
public class AuthFragment extends RefreshableFragment {
    @Inject
    Picasso picasso;
    @Inject
    AuthService authApi;

    AuthAdapter adapter;
    public AuthFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        App.from(activity).component().inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AuthAdapter(picasso);
        forceRefresh();
    }

    @Override
    public RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter() {
        return adapter;
    }

    @Override
    public void loadData(int page) {
        authApi.listAuth(page)
                .map(new Func1<Pagination<Auth>, List<Auth>>() {
                    @Override
                    public List<Auth> call(Pagination<Auth> pagination) {
                        return pagination.getData();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter);
    }
}
