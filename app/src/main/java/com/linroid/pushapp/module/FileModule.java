package com.linroid.pushapp.module;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.linroid.pushapp.module.identifier.AccountSavedFile;
import com.linroid.pushapp.module.identifier.PackageDownloadDir;
import com.linroid.pushapp.module.identifier.DataCacheDir;
import com.linroid.pushapp.module.identifier.HttpCacheDir;

import java.io.File;
import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

/**
 * Created by linroid on 7/24/15.
 */
@Module
public class FileModule {
    @HttpCacheDir
    @Provides
    @Singleton
    File provideHttpCacheDir(Application app) {
        return app.getCacheDir();
    }

    @DataCacheDir
    @Provides
    @Singleton
    File provideDataCacheDir(Application app) {
        return app.getCacheDir();
    }

    @PackageDownloadDir
    @Provides
    @Singleton
    File providePackageDownloadDir(Context context) {
        String state = Environment.getExternalStorageState();
        File rootDir;
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            rootDir = new File(context.getExternalFilesDir(null), "apk/");
        } else {
            rootDir = new File(context.getFilesDir(), "apk/");

        }
        if(!rootDir.exists()){
            rootDir.mkdirs();
        }
        return rootDir;
    }
    @AccountSavedFile
    @Provides
    @Singleton
    File provideAccountFile(Context context) {
        File file = new File(context.getFilesDir(), "account.json");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Timber.e("创建account.json失败");
            }
        }
        return file;
    }
}
