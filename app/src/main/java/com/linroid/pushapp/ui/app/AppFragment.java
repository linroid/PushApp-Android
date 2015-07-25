package com.linroid.pushapp.ui.app;


import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.linroid.pushapp.ui.base.RefreshableFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * 显示本地的App列表,Debuggable 模式的App的将会显示在前面
 */
public class AppFragment extends RefreshableFragment {
    AppAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AppAdapter();
        PackageManager pm = getActivity().getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        List<ApplicationInfo> debuggablePackages = new ArrayList<>();
        for (int i = 0; i < packages.size(); i++) {
            if ((packages.get(i).flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                //This is for System applications
            } else {
                boolean isDebuggable = (0 != (getActivity().getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));

                if ((pm.getLaunchIntentForPackage(packages.get(i).packageName) != null) && isDebuggable) {
                    debuggablePackages.add(packages.get(i));
                }
            }
        }
        adapter.setData(debuggablePackages);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter() {
        return adapter;
    }


}
