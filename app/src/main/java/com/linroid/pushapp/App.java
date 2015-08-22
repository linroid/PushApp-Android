package com.linroid.pushapp;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;
import com.linroid.pushapp.module.ApiModule;
import com.linroid.pushapp.module.AppModule;
import com.linroid.pushapp.module.DataModule;
import com.linroid.pushapp.module.FileModule;
import com.telly.mrvector.MrVector;

import cn.jpush.android.api.JPushInterface;
import timber.log.Timber;

/**
 * Created by linroid on 7/20/15.
 */
public class App extends Application {
    AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        if(BuildConfig.DEBUG) {
            Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(
                                    Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(
                                    Stetho.defaultInspectorModulesProvider(this))
                            .build());
            Timber.plant(new Timber.DebugTree());
        }
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .dataModule(new DataModule())
                .apiModule(new ApiModule())
                .fileModule(new FileModule())
                .build();
        component.inject(this);

        JPushInterface.setDebugMode(BuildConfig.DEBUG);
        JPushInterface.init(this);

        PreferenceManager.setDefaultValues(this, Constants.SP_FILE_NAME, Context.MODE_PRIVATE, R.xml.pref_general, false);
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
