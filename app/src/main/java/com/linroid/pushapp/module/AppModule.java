package com.linroid.pushapp.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.linroid.pushapp.App;
import com.linroid.pushapp.Constants;
import com.linroid.pushapp.util.StringPreference;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

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
    SharedPreferences provideSharedPreferences(Context context) {
        return context.getSharedPreferences(Constants.SP_FILE_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    @Named(Constants.SP_TOKEN)
    StringPreference provideTokenPreference(SharedPreferences sp) {
        return new StringPreference(sp, Constants.SP_TOKEN);
    }

}
