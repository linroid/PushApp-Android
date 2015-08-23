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

package com.linroid.pushapp.module;

import android.app.Application;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.linroid.pushapp.App;
import com.linroid.pushapp.Constants;
import com.linroid.pushapp.model.Account;
import com.linroid.pushapp.module.identifier.AccountSavedFile;
import com.linroid.pushapp.util.BooleanPreference;
import com.linroid.pushapp.util.Once;
import com.linroid.pushapp.util.StringPreference;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import timber.log.Timber;

/**
 * Created by linroid on 7/24/15.
 */
@Module
public class AppModule {
    private final App app;

    public AppModule(App app) {
        this.app = app;
    }

    @Provides
    @Singleton
    App provideApp() {
        return app;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return app;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return app;
    }

    @Provides
    @Singleton
    Resources provideResources(Context context) {
        return context.getResources();
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(Constants.SP_NAME_PUSHAPP, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    RefWatcher provideRefWatcher(Application application) {
        Timber.d("start memory leak detection..");
        return LeakCanary.install(application);
    }

    @Provides
    @Singleton
    @Named(Constants.SP_TOKEN)
    StringPreference provideTokenPreference(SharedPreferences sp) {
        return new StringPreference(sp, Constants.SP_TOKEN, null);
    }

    @Provides
    @Singleton
    @Named(Constants.SP_AUTO_INSTALL)
    BooleanPreference provideAutoInstallPreference(SharedPreferences sp) {
        return new BooleanPreference(sp, Constants.SP_AUTO_INSTALL, true);
    }

    @Provides
    @Singleton
    @Named(Constants.SP_AUTO_OPEN)
    BooleanPreference provideAutoOpenPreference(SharedPreferences sp) {
        return new BooleanPreference(sp, Constants.SP_AUTO_OPEN, true);
    }

    @Provides
    @Singleton
    Account provideAuthorization(@AccountSavedFile File accountFile) {
        Account account = Account.readFromFile(accountFile);
        if (account == null) {
            account = new Account();
        }
        account.setFile(accountFile);
        return account;
    }

    @Provides
    @Singleton
    NotificationManager provideNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Provides
    Once provideOnce(Context context, Resources resources) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.SP_NAME_PUSHAPP, Context.MODE_PRIVATE);
        return new Once(resources, preferences);
    }
}
