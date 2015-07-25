package com.linroid.pushapp.module;

import com.linroid.pushapp.api.DeviceService;

import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

/**
 * Created by linroid on 7/24/15.
 */
@Module
public class ApiModule {
    @Provides
    public DeviceService provideDevice(RestAdapter adapter) {
        return adapter.create(DeviceService.class);
    }
}
