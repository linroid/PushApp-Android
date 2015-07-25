package com.linroid.pushapp.module;

import android.app.Activity;

import com.linroid.pushapp.ui.home.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by linroid on 7/24/15.
 */
@Module
public class ActivityModule {
    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }
    @PerActivity @Provides Activity activity(){
        return activity;
    }

}
