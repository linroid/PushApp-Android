package com.linroid.pushapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.linroid.pushapp.App;
import com.linroid.pushapp.api.DeviceService;
import com.linroid.pushapp.model.Account;
import com.linroid.pushapp.model.Device;
import com.linroid.pushapp.util.DeviceUtil;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

public class StatusChangedReceiver extends BroadcastReceiver {
    @Inject
    DeviceService deviceApi;
    @Inject
    Account auth;

    public StatusChangedReceiver() {
    }

    @DebugLog
    @Override
    public void onReceive(Context context, Intent intent) {
        if (deviceApi == null) {
            App.from(context).component().inject(this);
        }
        String action = intent.getAction();
        switch (action) {
            case ConnectivityManager.CONNECTIVITY_ACTION:
                onNetworkChanged(context, intent);
                break;

        }
    }

    private void onNetworkChanged(Context context, Intent intent) {
        Map<String, String> params = new HashMap<>();
        params.put("network_type", DeviceUtil.networkType(context));
        if (auth.isValid()) {

            deviceApi.updateDevice(auth.getDevice().getId(), params, new Callback<Device>() {
                @Override
                public void success(Device device, Response response) {
                    Timber.d("设备网络状态上报成功:)");
                }

                @Override
                public void failure(RetrofitError error) {
                    Timber.d("设备网络状态上报失败:(", error);
                }
            });
        }
    }
}
