package com.linroid.pushapp.ui.base;

import android.app.Activity;

import com.linroid.pushapp.AppComponent;
import com.linroid.pushapp.module.ActivityModule;
import com.linroid.pushapp.ui.home.PerActivity;

import dagger.Component;

/**
 * Created by linroid on 7/25/15.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface AbstractActivityComponent {
//    Activity activity();

    void inject(Activity activity);
}
