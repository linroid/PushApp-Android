package com.linroid.pushapp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.linroid.pushapp.App;
import com.linroid.pushapp.Constants;
import com.linroid.pushapp.R;
import com.linroid.pushapp.model.InstallPackage;
import com.linroid.pushapp.module.identifier.PackageDownloadDir;
import com.linroid.pushapp.ui.AndroidUtil;
import com.linroid.pushapp.util.StringPreference;
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

    private ThinDownloadManager downloadManager;
    private Map<Integer, InstallPackage> downloadPackageMap;
    private Handler handler = new Handler(Looper.getMainLooper());

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
            Timber.e("require push info extra");
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

        DownloadRequest request = new DownloadRequest(downloadUri);
        File savedDir = new File(downloadDir, String.valueOf(pack.getId()));
        if (downloadPackageMap.containsKey(pack.getId())) {
            Timber.w("download existing... break out");
            return;
        }
        if (!savedDir.exists()) {
            savedDir.mkdir();
        }
        File savedFile = new File(savedDir, downloadUri.getLastPathSegment());
        request.setDestinationURI(Uri.fromFile(savedFile));
        request.setDownloadListener(new DownloadStatusListener() {
            @Override
            @DebugLog
            public void onDownloadComplete(int i) {
                downloadPackageMap.remove(i);
                prevProgress = -1;
            }

            @Override
            @DebugLog
            public void onDownloadFailed(int i, int i1, String s) {
                InstallPackage pack = downloadPackageMap.get(i);
                showNotification(pack, -1);
                downloadPackageMap.remove(i);
                prevProgress = -1;
            }

            @Override
            @DebugLog
            public void onProgress(int i, long l, int i1) {
                showNotification(downloadPackageMap.get(i), i1);
            }
        });
        int downloadId = downloadManager.add(request);
        downloadPackageMap.put(downloadId, pack);
    }

    int prevProgress = -1;
    private void showNotification(InstallPackage pack, int progress) {
        if (prevProgress == progress){
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
                .setProgress(100, progress, false)
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
