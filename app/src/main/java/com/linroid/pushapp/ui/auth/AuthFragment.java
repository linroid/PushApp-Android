package com.linroid.pushapp.ui.auth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.linroid.pushapp.App;
import com.linroid.pushapp.Constants;
import com.linroid.pushapp.R;
import com.linroid.pushapp.api.AuthService;
import com.linroid.pushapp.model.Auth;
import com.linroid.pushapp.model.Pagination;
import com.linroid.pushapp.ui.base.RefreshableFragment;
import com.linroid.pushapp.ui.bind.QrcodeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

/**
 * TODO 添加数据库支持
 * Created by linroid on 8/15/15.
 */
public class AuthFragment extends RefreshableFragment implements AuthAdapter.OnActionListener, View.OnClickListener {
    public static final int MSG_REVOKE = 0x11;
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
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
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
        adapter.remove(position);
        final Subscription subscription = authApi.revoke(auth.getId())
                .delaySubscription(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        Snackbar.make(fab, R.string.msg_revoke_success, Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(fab, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Void aVoid) {
                    }
                });
        subscriptions.add(subscription);
        Snackbar.make(fab, R.string.msg_revoke_progress, Snackbar.LENGTH_LONG)
                .setAction(R.string.btn_revoke_recall, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subscriptions.remove(subscription);
                        subscription.unsubscribe();
                        adapter.insertItem(position, auth);
                    }
                }).show();
    }
//
//    /**
//     * 撤回
//     * @param auth
//     */
//    private void recallRevoke(final int position, Auth auth) {
//        final ProgressDialog dialog = new ProgressDialog(getActivity());
//        dialog.setMessage(getString(R.string.msg_recall_revoke_progress));
//        dialog.setIndeterminate(true);
//        dialog.show();
//        authApi.recallRevoke(auth.getId(), new Callback<Auth>() {
//            @Override
//            public void success(Auth auth, Response response) {
//                adapter.insertItem(position, auth);
//                dialog.dismiss();
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                dialog.dismiss();
//                Snackbar.make(fab, error.getMessage(), Snackbar.LENGTH_SHORT).show();
//            }
//        });
//    }

    @Override
    public void onClick(View v) {
        Intent intent = QrcodeActivity.createNewScanIntent(getActivity(), Constants.QRCODE_KEY_AUTH);
        startActivityForResult(intent, QrcodeActivity.REQ_SCAN_QRCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == QrcodeActivity.REQ_SCAN_QRCODE && resultCode == Activity.RESULT_OK) {
            String token = data.getStringExtra(QrcodeActivity.EXTRA_QRCODE_VALUE);
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
