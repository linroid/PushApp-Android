package com.linroid.pushapp;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.preference.PreferenceManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.stetho.Stetho;
import com.linroid.pushapp.module.ApiModule;
import com.linroid.pushapp.module.AppModule;
import com.linroid.pushapp.module.FileModule;
import com.linroid.pushapp.module.NetworkModule;
import com.squareup.okhttp.OkHttpClient;


import javax.inject.Inject;

import cn.jpush.android.api.JPushInterface;
import timber.log.Timber;

/**
 * Created by linroid on 7/20/15.
 */
public class App extends Application{
    AppComponent component;

    @Inject
    OkHttpClient okHttp;
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
        component.inject(this);

        configFresco();

        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);

        PreferenceManager.setDefaultValues(this, Constants.SP_FILE_NAME, Context.MODE_PRIVATE, R.xml.pref_general, false);
        if(BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    private void configFresco() {
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(this, okHttp)
                .build();
        Fresco.initialize(this, config);

    }

    public AppComponent component() {
        return component;
    }
    public static App from(Activity activity) {
        return (App) activity.getApplication();
    }

    public static App from(Context context) {
        return (App) context.getApplicationContext();
    }
    public static App from(Service service) {
        return (App) service.getApplication();
    }
}
