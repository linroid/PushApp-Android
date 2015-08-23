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

package com.linroid.pushapp;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.preference.PreferenceManager;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.linroid.pushapp.module.ApiModule;
import com.linroid.pushapp.module.AppModule;
import com.linroid.pushapp.module.DataModule;
import com.linroid.pushapp.module.FileModule;

import cn.jpush.android.api.JPushInterface;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

/**
 * Created by linroid on 7/20/15.
 */
public class App extends Application {
    AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
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

        PreferenceManager.setDefaultValues(this, Constants.SP_NAME_PUSHAPP, Context.MODE_PRIVATE, R.xml.pref_general, false);
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
