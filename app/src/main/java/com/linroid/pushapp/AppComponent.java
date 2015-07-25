package com.linroid.pushapp;

import android.app.Activity;

import com.linroid.pushapp.module.ApiModule;
import com.linroid.pushapp.module.AppModule;
import com.linroid.pushapp.module.FileModule;
import com.linroid.pushapp.module.NetworkModule;
import com.linroid.pushapp.ui.bind.BindActivity;
import com.linroid.pushapp.ui.home.HomeActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by linroid on 7/24/15.
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, FileModule.class, ApiModule.class})
public interface AppComponent {
    void inject(App app);
    void inject(BindActivity activity);
    void inject(HomeActivity activity);
}
