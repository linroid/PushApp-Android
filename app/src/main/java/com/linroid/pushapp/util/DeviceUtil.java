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
