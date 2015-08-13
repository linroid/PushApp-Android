package com.linroid.pushapp.module;

import com.linroid.pushapp.api.AuthService;
import com.linroid.pushapp.api.DeviceService;
import com.linroid.pushapp.api.InstallService;
import com.linroid.pushapp.api.PackageService;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by linroid on 7/24/15.
 */
@Module
public class ApiModule {
    @Provides
    DeviceService provideDevice(RestAdapter adapter) {
        return adapter.create(DeviceService.class);
    }
    @Provides
    PackageService providePackage(RestAdapter adapter) {
        return adapter.create(PackageService.class);
    }
    @Provides
    AuthService provideAuth(RestAdapter adapter) {
        return adapter.create(AuthService.class);
    }
    @Provides
    InstallService provideInstall(RestAdapter adapter) {
        return adapter.create(InstallService.class);
    }
}
