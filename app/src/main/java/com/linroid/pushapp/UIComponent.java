package com.linroid.pushapp;

import com.linroid.pushapp.ui.bind.BindActivity;
import com.linroid.pushapp.ui.home.HomeActivity;
import com.linroid.pushapp.ui.pack.PackageFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by linroid on 7/31/15.
 */
@Singleton
@Component
public interface UIComponent {
    
    void inject(PackageFragment fragment);

    void inject(BindActivity activity);

    void inject(HomeActivity activity);
}
