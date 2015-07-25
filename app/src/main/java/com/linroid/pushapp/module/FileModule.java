package com.linroid.pushapp.module;

import android.app.Application;

import java.io.File;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by linroid on 7/24/15.
 */
@Module
public class FileModule {
    @Named("HttpCache")
    @Provides
    @Singleton
    File provideHttpCacheDir(Application app) {
        return app.getCacheDir();
    }

    @Named("DataCache")
    @Provides
    @Singleton
    File provideDataCacheDir(Application app) {
        return app.getCacheDir();
    }
}
