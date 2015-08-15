package com.linroid.pushapp.util;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.webkit.MimeTypeMap;

import com.linroid.pushapp.BuildConfig;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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
     * @param apkPath apk文件路径
     */
    public static void installApk(Context context, String apkPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(apkPath)),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 卸载应用
     *
     * @param context
     * @param packageName 包名
     */
    public static void uninstallApp(Context context, String packageName) {
        Uri uri = Uri.parse("package:" + packageName);
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 判断应用是否已经安装
     *
     * @param context
     * @param packageName 包名
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info;
        try {
            info = pm.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            info = null;
        }
        return info != null;
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

    /**
     * 获取apk的icon
     *
     * @param context
     * @param apkPath apk路径
     * @return
     */
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

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static float pxToDp(int px) {
        return px / Resources.getSystem().getDisplayMetrics().density;
    }

    /**
     * 根据包名打开其他应用
     *
     * @param context
     * @param packageName 包名
     */
    public static void openApplication(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Timber.e("未找到包名为[%s]的应用", packageName);
        }

    }
    /**
     * 根据包名获得打开其他应用的Intent
     *
     * @param context
     * @param packageName 包名
     */
    public static Intent getOpenAppIntent(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            Timber.e("未找到包名为[%s]的应用", packageName);
            return null;
        }
        return intent;
    }

    /**
     * 获得屏幕真是高度
     *
     * @param display Display
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getRealHeight(Display display) {
        int height;
        try {
            Point size = new Point();
            display.getRealSize(size);
            height = size.y;
        } catch (NoSuchMethodError e) {
            height = display.getHeight();
        }
        return height;
    }

    /**
     * 获得设备的内存大小
     *
     * @return
     */
    public static long totalMemorySize() {
        String meminfoPath = "/proc/meminfo";
        String str2;
        String[] arrayOfString;
        long initial_memory = 0;
        try {
            FileReader localFileReader = new FileReader(meminfoPath);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }
            //total Memory
            initial_memory = Integer.valueOf(arrayOfString[1]) * 1024;
            localBufferedReader.close();
            return initial_memory;
        } catch (IOException e) {
            return -1;
        }
    }

    /**
     * 获得文件MimeType
     * @param filePath
     * @return
     */
    public static String getMimeType(String filePath) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
