package com.linroid.pushapp.ui.pack;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.linroid.pushapp.App;
import com.linroid.pushapp.api.PackageService;
import com.linroid.pushapp.model.Pack;
import com.linroid.pushapp.model.Pagination;
import com.linroid.pushapp.model.Push;
import com.linroid.pushapp.service.ApkAutoInstallService;
import com.linroid.pushapp.service.DownloadService;
import com.linroid.pushapp.ui.base.RefreshableFragment;
import com.linroid.pushapp.ui.push.PushActivity;
import com.linroid.pushapp.util.IntentUtil;
import com.squareup.picasso.Picasso;
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
import timber.log.Timber;

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
        subscriptions.add(db.createQuery(Pack.DB.TABLE_NAME, Pack.DB.SQL_LIST_QUERY)
                        .map(Pack.DB.MAP)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(adapter)
        );
        forceRefresh();
//        if (savedInstanceState != null) {
//            List<Pack> savedPacks = savedInstanceState.getParcelableArrayList(STATE_PACKAGE);
//            adapter.setData(savedPacks);
//        } else {
//            forceRefresh();
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

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
        subscriptions.add(packageApi.listReceived(page)
                .map(new Func1<Pagination<Pack>, List<Pack>>() {
                    @Override
                    public List<Pack> call(Pagination<Pack> pagination) {
                        return pagination.getData();
                    }
                })
                .flatMap(new Func1<List<Pack>, Observable<Pack>>() {
                    @Override
                    public Observable<Pack> call(List<Pack> packs) {
                        return Observable.from(packs);
                    }
                })
                .filter(new Func1<Pack, Boolean>() {
                    @Override
                    public Boolean call(Pack pack) {
                        Cursor cursor = db.query(Pack.DB.SQL_ITEM_QUERY, String.valueOf(pack.getId()));
                        return !cursor.moveToNext();
                    }
                })
                .toList()
                .doOnNext(new Action1<List<Pack>>() {
                    @Override
                    public void call(List<Pack> packs) {
                        db.beginTransaction();
                        for (Pack pack : packs) {
                            db.insert(Pack.DB.TABLE_NAME, pack.toContentValues());
                        }
                        db.setTransactionSuccessful();
                        db.endTransaction();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<Pack>>() {

                    @DebugLog
                    @Override
                    public void onCompleted() {
                        loaderView.refreshLayout.setRefreshing(false);
                    }

                    @DebugLog
                    @Override
                    public void onError(Throwable e) {
                        if(loaderView!=null) {
                            loaderView.refreshLayout.setRefreshing(false);
                            Snackbar.make(loaderView, e.getMessage(), Snackbar.LENGTH_SHORT).show();
                            Timber.e(e, "pack error");
                        }
                    }

                    @DebugLog
                    @Override
                    public void onNext(List<Pack> packs) {
                    }

                }));
    }

    @Override
    public void onOpen(Pack pack) {
        Intent intent = IntentUtil.openApp(getActivity(), pack.getPackageName());
        if (intent!=null) {
            startActivity(intent);
        }
    }

    @Override
    public void onInstall(Pack pack) {
        ApkAutoInstallService.addInstallPackage(pack);
        startActivity(IntentUtil.installApk(pack.getPath()));
    }

    @Override
    public void onUninstall(Pack pack) {
        Intent intent = IntentUtil.uninstallApp(pack.getPackageName());
        startActivity(intent);
    }

    @Override
    public void onSend(Pack pack) {
        Intent intent = PushActivity.createNewSelectIntent(getActivity(), pack);
        startActivityForResult(intent, PushActivity.REQUEST_PUSH);
    }

    @Override
    public void onDownload(Pack pack) {
        Intent intent = DownloadService.createNewDownloadIntent(getActivity(), pack);
        getActivity().startService(intent);
    }

    @Override
    public void onAppInfo(Pack pack) {
        Intent intent = IntentUtil.appInfo(pack.getPackageName());
        startActivity(intent);
    }
}
