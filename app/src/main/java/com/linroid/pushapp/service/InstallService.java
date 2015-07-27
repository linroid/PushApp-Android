package com.linroid.pushapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.linroid.pushapp.model.InstallPackage;
import com.linroid.pushapp.util.AndroidUtil;


import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * Created by linroid on 7/27/15.
 */
public class InstallService extends Service {
    public static final String EXTRA_PACKAGE = "package";
    private Queue<InstallPackage> installQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        installQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
            Timber.e("require package info extra");
            return super.onStartCommand(intent, flags, startId);
        }
        InstallPackage pack = intent.getParcelableExtra(EXTRA_PACKAGE);
        installQueue.add(pack);
        AndroidUtil.installPackage(this, pack);
        ApkAutoInstallService.installApplication(pack.getAppName());
        return super.onStartCommand(intent, flags, startId);
    }

}
