package com.linroid.pushapp;

import com.linroid.pushapp.module.ApiModule;
import com.linroid.pushapp.module.AppModule;
import com.linroid.pushapp.module.DataModule;
import com.linroid.pushapp.module.FileModule;
import com.linroid.pushapp.receiver.PushReceiver;
import com.linroid.pushapp.service.ApkAutoInstallService;
import com.linroid.pushapp.service.DownloadService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by linroid on 7/24/15.
 */
@Singleton
@Component(modules = {AppModule.class, DataModule.class, FileModule.class, ApiModule.class},
        dependencies = {UIComponent.class})
public interface AppComponent {
    void inject(App app);

    void inject(PushReceiver receiver);

    void inject(DownloadService service);

    void inject(ApkAutoInstallService service);
}
