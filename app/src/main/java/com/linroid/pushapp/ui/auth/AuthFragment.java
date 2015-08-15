package com.linroid.pushapp.ui.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;

import com.linroid.pushapp.App;
import com.linroid.pushapp.R;
import com.linroid.pushapp.api.AuthService;
import com.linroid.pushapp.model.Auth;
import com.linroid.pushapp.model.Pagination;
import com.linroid.pushapp.ui.base.RefreshableFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * TODO 添加数据库支持
 * Created by linroid on 8/15/15.
 */
public class AuthFragment extends RefreshableFragment implements AuthAdapter.OnActionListener {
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
        adapter.setListener(this);
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
                        loaderView.setPage(pagination.getCurrentPage(), pagination.getLastPage());
                        return pagination.getData();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter);
    }

    @Override
    public void onRevoke(final int position, final Auth auth) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(getString(R.string.msg_revoke_progress));
        dialog.setIndeterminate(true);
        dialog.show();
        authApi.revoke(auth.getId(), new Callback<Void>() {
            @Override
            public void success(Void aVoid, Response response) {
                dialog.dismiss();
                adapter.remove(position);
            }

            @Override
            public void failure(RetrofitError error) {
                dialog.dismiss();
                Snackbar.make(loaderView, error.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
