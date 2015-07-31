package com.linroid.pushapp.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateUtils;

import com.linroid.pushapp.BuildConfig;
import com.linroid.pushapp.model.Pack;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import timber.log.Timber;

/**
 * Created by linroid on 7/26/15.
 */
public class AndroidUtil {
    public static final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static Date formatDate(String dateStr) {
        try {
            return formatter.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }

    }

    public static CharSequence friendlyTime(String dateStr) {
        Date date = formatDate(dateStr);
        return DateUtils.getRelativeTimeSpanString(date.getTime());
    }

    public static String sprintBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            sb.append(key);
            sb.append(" => ");
            sb.append(bundle.get(key));
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * 打开安装页面
     *
     * @param context
     * @param pack
     */
    public static void installPackage(Context context, Pack pack) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(pack.getPath())),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 检测Accessibility服务是否开启
     *
     * @param className
     * @param context
     * @return
     * @{http://stackoverflow.com/questions/18094982/detect-if-my-accessibility-service-is-enabled}
     */
    public static boolean isAccessibilitySettingsOn(Context context, String className) {
        int accessibilityEnabled = 0;
        final String service = BuildConfig.APPLICATION_ID + "/" + className;
        boolean accessibilityFound = false;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    context.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            Timber.e("Error finding setting, default accessibility to not found ", e);
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            String settingValue = Settings.Secure.getString(
                    context.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessabilityService = mStringColonSplitter.next();
                    if (accessabilityService.equalsIgnoreCase(service)) {
                        accessibilityFound = true;
                    }
                }
            }
        }


        return accessibilityFound;
    }

    /**
     * 获取Apk的应用名称
     *
     * @param apkPath
     * @return
     */
    public static CharSequence getApkLabel(Context context, String apkPath) {
        Timber.d("apk path: %s", apkPath);
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            info.applicationInfo.sourceDir = apkPath;
            info.applicationInfo.publicSourceDir = apkPath;
            ApplicationInfo applicationInfo = info.applicationInfo;
            return applicationInfo.loadLabel(pm);
        }
        return null;
    }

    public static Drawable getApkIcon(Context context, String apkPath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            info.applicationInfo.sourceDir = apkPath;
            info.applicationInfo.publicSourceDir = apkPath;
            ApplicationInfo applicationInfo = info.applicationInfo;
            return applicationInfo.loadIcon(pm);
        }
        return null;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static float pxToDp(int px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

}
