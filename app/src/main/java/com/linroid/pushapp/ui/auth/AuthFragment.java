package com.linroid.pushapp.ui.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.linroid.pushapp.App;
import com.linroid.pushapp.R;
import com.linroid.pushapp.api.AuthService;
import com.linroid.pushapp.model.Auth;
import com.linroid.pushapp.model.Pagination;
import com.linroid.pushapp.ui.base.RefreshableFragment;
import com.linroid.pushapp.ui.bind.QrcodeActivity;
import com.squareup.picasso.Picasso;
import static  com.linroid.pushapp.ui.bind.QrcodeActivity.ARG_BIND_TOKEN;

import java.util.List;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * TODO 添加数据库支持
 * Created by linroid on 8/15/15.
 */
public class AuthFragment extends RefreshableFragment implements AuthAdapter.OnActionListener, View.OnClickListener {
    @Inject
    Picasso picasso;
    @Inject
    AuthService authApi;
    AuthAdapter adapter;
    FloatingActionButton fab;

    CompositeSubscription subscriptions = new CompositeSubscription();

    public AuthFragment() {
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        App.from(activity).component().inject(this);
        fab = (FloatingActionButton) activity.findViewById(R.id.fab);
        fab.setOnClickListener(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AuthAdapter(picasso);
        adapter.setListener(this);
        forceRefresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        subscriptions.unsubscribe();
    }

    @Override
    public RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter() {
        return adapter;
    }

    @Override
    public void loadData(int page) {
        subscriptions.add(authApi.listAuth(page)
                .map(new Func1<Pagination<Auth>, List<Auth>>() {
                    @Override
                    public List<Auth> call(Pagination<Auth> pagination) {
                        loaderView.setPage(pagination.getCurrentPage(), pagination.getLastPage());
                        return pagination.getData();
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Snackbar.make(fab, throwable.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }));
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

    @Override
    public void onClick(View v) {
        Intent intent = QrcodeActivity.createNewScanIntent(getActivity());
        startActivityForResult(intent, QrcodeActivity.REQ_SCAN_QRCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == QrcodeActivity.REQ_SCAN_QRCODE && resultCode == Activity.RESULT_OK) {
            String token = null;
            if (data.hasExtra(ARG_BIND_TOKEN)) {
                token = data.getStringExtra(ARG_BIND_TOKEN);
            }
            if (!TextUtils.isEmpty(token)) {
                authUser(token);
                Snackbar.make(fab, R.string.msg_auth_snack, Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void authUser(String token) {
        authApi.authUser(token, new Callback<Auth>() {
            @Override
            public void success(Auth auth, Response response) {
                adapter.insertItem(auth);
                Snackbar.make(fab, getString(R.string.msg_auth_success, auth.getUser().getNickname()), Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Snackbar.make(fab, error.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
