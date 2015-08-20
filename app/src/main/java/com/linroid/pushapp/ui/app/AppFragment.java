package com.linroid.pushapp.ui.app;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.linroid.pushapp.BuildConfig;
import com.linroid.pushapp.ui.base.RefreshableFragment;
import com.linroid.pushapp.ui.push.PushActivity;

import java.util.List;

import hugo.weaving.DebugLog;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;


/**
 * 显示本地的App列表,Debuggable 模式的App的将会显示在前面
 */
public class AppFragment extends RefreshableFragment implements AppAdapter.OnActionListener {
    AppAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AppAdapter(getActivity());
        adapter.setListener(this);
        forceRefresh();
    }

    @Override
    public RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter() {
        return adapter;
    }

    @Override
    public void loadData(int page) {

        Observable.create(new Observable.OnSubscribe<List<ApplicationInfo>>() {
            @Override
            public void call(Subscriber<? super List<ApplicationInfo>> subscriber) {
                PackageManager pm = getActivity().getPackageManager();
                List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
                subscriber.onNext(packages);
                subscriber.onCompleted();
            }
        })
        .subscribeOn(Schedulers.computation())
        .flatMap(new Func1<List<ApplicationInfo>, Observable<ApplicationInfo>>() {
            @Override
            @DebugLog
            public Observable<ApplicationInfo> call(List<ApplicationInfo> applicationInfos) {
                return Observable.from(applicationInfos);
            }
        })
        .filter(new Func1<ApplicationInfo, Boolean>() {
            @Override
            public Boolean call(ApplicationInfo applicationInfo) {
                return !BuildConfig.APPLICATION_ID.equals(applicationInfo.packageName);
            }
        })
        .toSortedList(new Func2<ApplicationInfo, ApplicationInfo, Integer>() {
            @Override
            public Integer call(ApplicationInfo applicationInfo, ApplicationInfo applicationInfo2) {
                Timber.d(applicationInfo.packageName+"  " + applicationInfo2.packageName);
                int flag1 = applicationInfo.flags&ApplicationInfo.FLAG_DEBUGGABLE;
                int flag2 = applicationInfo2.flags&ApplicationInfo.FLAG_DEBUGGABLE;
                if ((flag1!=0 && flag2!=0) || flag1==0&&flag2==0) {
                    return 0;
                }
                if (flag1!=0) {
                    return -1;
                }
                return 1;
            }
        })
//                .toList()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(adapter);

    }


    @Override
    public void onSend(ApplicationInfo info) {
        Intent intent = PushActivity.createNewSelectIntent(getActivity(), info);
        startActivityForResult(intent, PushActivity.REQUEST_PUSH);
    }
}
