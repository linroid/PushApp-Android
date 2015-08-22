package com.linroid.pushapp.ui.app;


import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.linroid.pushapp.BuildConfig;
import com.linroid.pushapp.ui.base.RefreshableFragment;
import com.linroid.pushapp.ui.send.SendActivity;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;


/**
 * 显示本地已安装的的App列表,Debuggable 模式的App的将会显示在前面
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 同步状态，其实没用，就初始化的时候用一下，为了防止有别的问题就同步一下吧
                adapter.setOnScroll(newState != RecyclerView.SCROLL_STATE_IDLE);

                // 如果滑动完毕了，就刷图出来，用viewholder直接调用方法比 notifyItem 来得快
                if (!adapter.onScroll) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    View firstVisibleChild = recyclerView.getChildAt(0);
                    int visibleItemCount = layoutManager.getChildCount();
                    int firstVisibleItemPosition = recyclerView.getChildPosition(firstVisibleChild);
                    AppAdapter.AppHolder holder;
                    for (int i = 0; i < visibleItemCount; i++) {
                        holder = (AppAdapter.AppHolder)
                                recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
                        holder.showImage(i + firstVisibleItemPosition);
                    }
                }
            }
        });
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
                    public Observable<ApplicationInfo> call(List<ApplicationInfo> applicationInfos) {
                        return Observable.from(applicationInfos);
                    }
                })
                .filter(new Func1<ApplicationInfo, Boolean>() {
                    @Override
                    public Boolean call(ApplicationInfo applicationInfo) {
                        return (applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM)==0
                                && !BuildConfig.APPLICATION_ID.equals(applicationInfo.packageName);

                    }
                })
                .toSortedList(new Func2<ApplicationInfo, ApplicationInfo, Integer>() {
                    @Override
                    public Integer call(ApplicationInfo applicationInfo, ApplicationInfo applicationInfo2) {
                        int flag1 = applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE;
                        int flag2 = applicationInfo2.flags & ApplicationInfo.FLAG_DEBUGGABLE;
                        if ((flag1 != 0 && flag2 != 0) || flag1 == 0 && flag2 == 0) {
                            return 0;
                        }
                        if (flag1 != 0) {
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
        Intent intent = SendActivity.createNewSelectIntent(getActivity(), info);
        startActivityForResult(intent, SendActivity.REQUEST_SEND);
    }


}
