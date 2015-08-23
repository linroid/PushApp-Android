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

import com.linroid.pushapp.module.ApiModule;
import com.linroid.pushapp.module.AppModule;
import com.linroid.pushapp.module.DataModule;
import com.linroid.pushapp.module.FileModule;
import com.linroid.pushapp.receiver.PushReceiver;
import com.linroid.pushapp.receiver.StatusChangedReceiver;
import com.linroid.pushapp.service.ApkAutoInstallService;
import com.linroid.pushapp.service.DownloadService;
import com.linroid.pushapp.ui.auth.AuthFragment;
import com.linroid.pushapp.ui.bind.BindActivity;
import com.linroid.pushapp.ui.device.DeviceFragment;
import com.linroid.pushapp.ui.home.DeviceTokenDialog;
import com.linroid.pushapp.ui.send.SendActivity;
import com.linroid.pushapp.ui.home.HomeActivity;
import com.linroid.pushapp.ui.pack.PackageFragment;
import com.linroid.pushapp.ui.setting.SettingFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by linroid on 7/24/15.
 */
@Singleton
@Component(modules = {AppModule.class, DataModule.class, FileModule.class, ApiModule.class})
public interface AppComponent {
    void inject(App app);

    void inject(PushReceiver receiver);

    void inject(StatusChangedReceiver receiver);

    void inject(DownloadService service);

    void inject(ApkAutoInstallService service);

    void inject(PackageFragment fragment);

    void inject(DeviceFragment fragment);

    void inject(BindActivity activity);

    void inject(HomeActivity activity);

    void inject(SendActivity activity);

    void inject(AuthFragment fragment);

    void inject(SettingFragment fragment);

    void inject(DeviceTokenDialog fragment);
}
