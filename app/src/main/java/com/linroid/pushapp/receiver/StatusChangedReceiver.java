/*
 * Copyright (c) linroid 2015.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
