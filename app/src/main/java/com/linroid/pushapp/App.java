package com.linroid.pushapp;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.stetho.Stetho;
import com.linroid.pushapp.module.ApiModule;
import com.linroid.pushapp.module.AppModule;
import com.linroid.pushapp.module.FileModule;
import com.linroid.pushapp.module.NetworkModule;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Created by linroid on 7/20/15.
 */
public class App extends Application{
    AppComponent component;
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .apiModule(new ApiModule())
                .fileModule(new FileModule())
                .build();

        Timber.plant(new Timber.DebugTree());


    }
    public AppComponent component() {
        return component;
    }
    public static App from(Activity activity) {
        return (App) activity.getApplication();
    }

}
