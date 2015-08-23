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

import com.linroid.pushapp.BuildConfig;
import com.linroid.pushapp.api.AuthService;
import com.linroid.pushapp.api.DeviceService;
import com.linroid.pushapp.api.FirService;
import com.linroid.pushapp.api.PackageService;
import com.linroid.pushapp.api.PushService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

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
    PushService provideInstall(RestAdapter adapter) {
        return adapter.create(PushService.class);
    }

    @Provides
    @Singleton
    FirService provideFirService(Converter converter, Client client, ErrorHandler errorHandler) {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setErrorHandler(errorHandler)
                .setClient(client)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addQueryParam("api_token", BuildConfig.FIR_API_TOKEN);
                    }
                })
                .setConverter(converter)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint("http://api.fir.im")
                .build();
        return restAdapter.create(FirService.class);
    }
}
