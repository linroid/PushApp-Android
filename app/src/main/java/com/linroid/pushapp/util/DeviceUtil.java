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

package com.linroid.pushapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by linroid on 7/25/15.
 */
public class DeviceUtil {
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";
    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";

    public static String networkType(Context context) {
        String type;
        ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState();
        NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();
        if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
            //mobile
            type = "mobile";
        } else if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING) {
            //wifi
            type = "wifi";
        } else {
            type = "unknown";
        }
        return type;
    }

    public static String id(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    /**
     * @return 是否为MIUI ROM
     */
    public static boolean isMIUI() {
        try {
            BuildPropUtils prop = BuildPropUtils.newInstance();
            if (!(prop.getProperty(KEY_MIUI_VERSION_CODE, null) == null
                    && prop.getProperty(KEY_MIUI_VERSION_NAME, null) == null
                    && prop.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) == null)) {
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * @return 是否为Flyme ROM
     */
    public static boolean isFlyme() {
        try {
            if (Build.class.getMethod("hasSmartBar") != null) {
                return true;
            }
        } catch (Exception ignored) {
        }
        return false;
    }
}
