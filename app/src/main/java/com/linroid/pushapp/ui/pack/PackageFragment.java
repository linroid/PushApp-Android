package com.linroid.pushapp.ui.pack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.linroid.pushapp.App;
import com.linroid.pushapp.api.PackageService;
import com.linroid.pushapp.model.Pack;
import com.linroid.pushapp.model.Pagination;
import com.linroid.pushapp.ui.base.RefreshableFragment;
import com.linroid.pushapp.util.AndroidUtil;
import com.squareup.picasso.Picasso;
import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import rx.Observer;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class PackageFragment extends RefreshableFragment implements PackageAdapter.OnActionListener {
    public static final String STATE_PACKAGE = "package";
    @Inject
    PackageService packageApi;
    @Inject
    BriteDatabase db;
    @Inject
    Picasso picasso;

    PackageAdapter adapter;


    CompositeSubscription subscriptions = new CompositeSubscription();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PackageAdapter(picasso);
        adapter.setListener(this);
//        if (savedInstanceState != null) {
//            List<Pack> savedPacks = savedInstanceState.getParcelableArrayList(STATE_PACKAGE);
//            adapter.setData(savedPacks);
//        } else {
//            refresh();
//        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putParcelableArrayList(STATE_PACKAGE, (ArrayList<? extends Parcelable>) adapter.getData());
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        App.from(activity).component().inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Subscription subscription = db.createQuery(Pack.DB.TABLE_NAME, Pack.DB.SQL_LIST_QUERY)
                .map(Pack.DB.MAP)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(adapter);
        subscriptions.add(subscription);
    }

    @Override
    public void onPause() {
        super.onPause();
        subscriptions.unsubscribe();
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
        Subscription subscription =
                AppObservable.bindSupportFragment(this, packageApi.listReceived(page))
                .map(new Func1<Pagination<Pack>, Pagination<Pack>>() {
                    @Override
                    public Pagination<Pack> call(Pagination<Pack> packPagination) {
                        return packPagination;
                    }
                })
                .subscribe(new Observer<Pagination<Pack>>() {

                    @DebugLog
                    @Override
                    public void onCompleted() {
                        loaderView.refreshLayout.setRefreshing(false);
                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                    }

                    @DebugLog
                    @Override
                    public void onNext(Pagination<Pack> packPagination) {

                    }
                });
        subscriptions.add(subscription);
    }

    @Override
    public void onInstall(Pack pack) {
        AndroidUtil.installApk(getActivity(), pack.getPath());
    }

    @Override
    public void onUninstall(Pack pack) {
        AndroidUtil.uninstallApp(getActivity(), pack.getPackageName());
    }

    @Override
    public void onSend(Pack pack) {

    }

    @Override
    public void onDownload(Pack pack) {

    }
}
