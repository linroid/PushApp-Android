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

package com.linroid.pushapp.service;

import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.linroid.pushapp.App;
import com.linroid.pushapp.Constants;
import com.linroid.pushapp.R;
import com.linroid.pushapp.model.Pack;
import com.linroid.pushapp.module.identifier.PackageDownloadDir;
import com.linroid.pushapp.util.AndroidUtil;
import com.linroid.pushapp.util.BooleanPreference;
import com.linroid.pushapp.util.IntentUtil;
import com.linroid.pushapp.util.MD5;
import com.linroid.pushapp.util.StringPreference;
import com.squareup.sqlbrite.BriteDatabase;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListener;
import com.thin.downloadmanager.ThinDownloadManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import hugo.weaving.DebugLog;
import timber.log.Timber;

public class DownloadService extends Service {
    public static final String EXTRA_PACKAGE = "package";
    public static final int NOTIFICATION_DOWNLOAD = 0x111;

    @Inject
    NotificationManager notificationManager;
    @Named(Constants.SP_TOKEN)
    @Inject
    StringPreference token;
    @Named(Constants.SP_AUTO_INSTALL)
    @Inject
    BooleanPreference autoInstall;
    @PackageDownloadDir
    @Inject
    File downloadDir;
    @Inject
    SharedPreferences preferences;
    @Inject
    BriteDatabase db;


    private ThinDownloadManager downloadManager;
    private Map<Integer, Pack> downloadPackageMap;

    public static Intent createNewDownloadIntent(Context context, Pack pack) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra(DownloadService.EXTRA_PACKAGE, pack);
        return intent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        App.from(this).component().inject(this);
        downloadManager = new ThinDownloadManager();
        downloadPackageMap = new HashMap<>();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @DebugLog
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null || !intent.hasExtra(EXTRA_PACKAGE)) {
//            throw new IllegalArgumentException("require push info extra");
            Timber.e("require package info extra");
            return super.onStartCommand(intent, flags, startId);
        }
        Timber.d("Bundle: %s", AndroidUtil.sprintBundle(intent.getExtras()));
        Pack pack = intent.getParcelableExtra(EXTRA_PACKAGE);
        newDownloadTask(pack);
        return super.onStartCommand(intent, flags, startId);
    }


    private void newDownloadTask(Pack pack) {
        if (pack == null || TextUtils.isEmpty(pack.getDownloadUrl())) {
            Timber.e("invalid package download url");
            return;
        }
        Timber.d("Download url: %s", pack.getDownloadUrl());
        Uri downloadUri = Uri.parse(pack.getDownloadUrl());

        File savedDir = new File(downloadDir, String.valueOf(pack.getId()));
        File savedFile = new File(savedDir, downloadUri.getLastPathSegment());
        DownloadRequest request = new DownloadRequest(downloadUri);
        if (downloadPackageMap.containsKey(pack.getId())) {
            Timber.w("download existing... break out");
            return;
        }
        if (!savedDir.exists()) {
            savedDir.mkdir();
        }
        //如果已经存在则不需要下载
        Cursor cursor = db.query(Pack.DB.SQL_ITEM_QUERY, String.valueOf(pack.getId()));
        if (cursor.moveToNext()) {
            Pack saved = Pack.fromCursor(cursor);
            if (!TextUtils.isEmpty(saved.getPath()) && savedFile.exists()) {
                onDownloadComplete(saved);
                return;
            }
        } else {
            db.insert(Pack.DB.TABLE_NAME, pack.toContentValues());
        }
        pack.setPath(savedFile.getAbsolutePath());
        request.setDestinationURI(Uri.fromFile(savedFile));
        request.setDownloadListener(new DownloadStatusListener() {
            @Override
            @DebugLog
            public void onDownloadComplete(int i) {
                prevProgress = -1;
                Pack pack = downloadPackageMap.remove(i);
                CharSequence label = AndroidUtil.getApkLabel(DownloadService.this, pack.getPath());
                pack.setAppName(label != null ? label.toString() : pack.getAppName());
                //TODO 临时解决办法（使用ThinDownloadManager下载APK后MD5改变)
                String md5 = MD5.calculateFile(new File(pack.getPath()));
                pack.setMD5(md5);
                db.update(Pack.DB.TABLE_NAME, pack.toContentValues(), Pack.DB.WHERE_ID, String.valueOf(pack.getId()));
                DownloadService.this.onDownloadComplete(pack);
            }

            @Override
            @DebugLog
            public void onDownloadFailed(int i, int i1, String s) {
                Pack pack = downloadPackageMap.get(i);
                Timber.e("%s 下载失败:( %d %s", pack.getAppName(), i1, s);
                showNotification(pack, -1);
                downloadPackageMap.remove(i);
                prevProgress = -1;
            }

            @Override
            public void onProgress(int i, long l, int i1) {
                showNotification(downloadPackageMap.get(i), i1);
            }
        });
        int downloadId = downloadManager.add(request);
        downloadPackageMap.put(downloadId, pack);
        showNotification(pack, 0);
    }

    /**
     * 下载完成
     * @param pack 下载的安装包
     */
    private void onDownloadComplete(Pack pack) {
        Timber.d("%s 下载完成,保存到:%s", pack.getAppName(), pack.getPath());
        int toastResId = R.string.toast_download_complete;
        if (autoInstall.getValue()) {
            startActivity(IntentUtil.installApk(pack.getPath()));
            if (AndroidUtil.isAccessibilitySettingsOn(this, ApkAutoInstallService.class.getCanonicalName())) {
                PowerManager powermanager = ((PowerManager) getSystemService(Context.POWER_SERVICE));
                PowerManager.WakeLock wakeLock = powermanager.newWakeLock(
                        (PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                                | PowerManager.FULL_WAKE_LOCK
                                | PowerManager.ACQUIRE_CAUSES_WAKEUP), "Install Worker, FULL WAKE LOCK");
                wakeLock.acquire();
                wakeLock.release();
                KeyguardManager keyguardManager = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
//                if(keyguardManager.isDeviceLocked()) {
                    final KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
                    keyguardLock.disableKeyguard();
//                }
                ApkAutoInstallService.addInstallPackage(pack);
                toastResId = R.string.toast_start_install;
            } else {
                toastResId = R.string.toast_download_complete;
            }
        }
        Toast.makeText(this, getString(toastResId, pack.getAppName()), Toast.LENGTH_SHORT).show();

    }

    /**
     * 防止进度更新太快
     */
    int prevProgress = -1;

    private void showNotification(Pack pack, int progress) {
        if (pack == null) {
            return;
        }
        if (prevProgress == progress) {
            return;
        }
        prevProgress = progress;
        String contentText;
        if (progress == 100) {
            contentText = getString(R.string.msg_download_complete);
        } else if (progress < 0) {
            contentText = getString(R.string.msg_download_failed);
            Toast.makeText(this, getString(R.string.toast_download_failed, pack.getAppName()), Toast.LENGTH_SHORT).show();
        } else {
            if(progress == 0) {
                Toast.makeText(this, getString(R.string.toast_start_download, pack.getAppName()), Toast.LENGTH_LONG).show();
            }
            contentText = getString(R.string.msg_downloading);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.msg_download_title, pack.getAppName(), "v"+pack.getVersionName()))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setContentText(contentText);
        if (progress > 0) {
            builder.setProgress(100, Math.max(progress, 0), false)
                    .setContentInfo(getString(R.string.msg_download_progress, progress));
        } else if (progress == 0) {
            builder.setProgress(100, 0, true);
        }
        if (progress == 100) {
            builder.setSmallIcon(R.drawable.ic_stat_complete);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(new File(pack.getPath())),
                    "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pi = PendingIntent.getActivity(this, 0x1, intent, 0);
            builder.setContentIntent(pi);
        }
//        ImageRequest request = ImageRequest.fromUri(pack.getIconUrl());
//        ImagePipeline pipeline = Fresco.getImagePipeline();
//        pipeline.prefetchToDiskCache(request, this);
        notificationManager.notify(pack.getId(), builder.build());
    }
}
