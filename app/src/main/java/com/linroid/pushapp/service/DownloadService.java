package com.linroid.pushapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.linroid.pushapp.App;
import com.linroid.pushapp.Constants;
import com.linroid.pushapp.R;
import com.linroid.pushapp.database.DbOpenHelper;
import com.linroid.pushapp.model.InstallPackage;
import com.linroid.pushapp.module.identifier.PackageDownloadDir;
import com.linroid.pushapp.util.AndroidUtil;
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
    @PackageDownloadDir
    @Inject
    File downloadDir;
    @Inject
    SharedPreferences preferences;

    @Inject
    BriteDatabase db;


    private ThinDownloadManager downloadManager;
    private Map<Integer, InstallPackage> downloadPackageMap;
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onCreate() {
        super.onCreate();
        App.from(this).component().inject(this);
        downloadManager = new ThinDownloadManager();
        downloadPackageMap = new HashMap<>();
        db.insert()
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
        InstallPackage pack = intent.getParcelableExtra(EXTRA_PACKAGE);
        newDownloadTask(pack);
        return super.onStartCommand(intent, flags, startId);
    }


    private void newDownloadTask(InstallPackage pack) {
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
        pack.setPath(savedFile.getAbsolutePath());
        request.setDestinationURI(Uri.fromFile(savedFile));
        request.setDownloadListener(new DownloadStatusListener() {
            @Override
            @DebugLog
            public void onDownloadComplete(int i) {
                InstallPackage pack = downloadPackageMap.remove(i);
                prevProgress = -1;
                if (preferences.getBoolean(Constants.SP_AUTO_INSTALL, true)) {
                    parsePackage(pack);
                }
            }

            @Override
            @DebugLog
            public void onDownloadFailed(int i, int i1, String s) {
                InstallPackage pack = downloadPackageMap.get(i);
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
    }

    private void parsePackage(InstallPackage pack) {
        CharSequence label = AndroidUtil.getApkLabel(this, pack.getPath());
        pack.setAppName(label != null ? label.toString() : pack.getAppName());
        installPackage(pack);
    }

    private void installPackage(InstallPackage pack) {
        if (AndroidUtil.isAccessibilitySettingsOn(this, ApkAutoInstallService.class.getCanonicalName())) {
            ApkAutoInstallService.installPackage(pack);
        } else {
            AndroidUtil.installPackage(this, pack);
        }
    }

    /**
     * 防止进度更新太快
     */
    int prevProgress = -1;

    private void showNotification(InstallPackage pack, int progress) {
        if (prevProgress == progress) {
            return;
        }
        prevProgress = progress;
        String titleText;
        if (progress == 100) {
//            handler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    notificationManager.cancel(NOTIFICATION_DOWNLOAD);
//                }
//            }, 5 * 1000);
            titleText = getString(R.string.msg_download_complete, pack.getAppName());
        } else if (progress < 0) {
            titleText = getString(R.string.msg_download_fail, pack.getAppName());
        } else {
            titleText = getString(R.string.msg_downloading, pack.getAppName());
        }
        Notification notification = new NotificationCompat.Builder(this)
                .setProgress(100, Math.max(progress, 0), false)
                .setContentTitle(titleText)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText(pack.getVersionName())
                .setContentInfo(getString(R.string.msg_download_progress, progress))
                .build();
//        ImageRequest request = ImageRequest.fromUri(pack.getIcon());
//        ImagePipeline pipeline = Fresco.getImagePipeline();
//        pipeline.prefetchToDiskCache(request, this);
        notificationManager.notify(pack.getId(), notification);
    }
}
