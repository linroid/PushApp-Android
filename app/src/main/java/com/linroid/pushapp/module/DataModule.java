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

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.gson.Gson;
import com.linroid.pushapp.BuildConfig;
import com.linroid.pushapp.Constants;
import com.linroid.pushapp.R;
import com.linroid.pushapp.database.DbOpenHelper;
import com.linroid.pushapp.model.Account;
import com.linroid.pushapp.model.Error;
import com.linroid.pushapp.module.identifier.DataCacheDir;
import com.linroid.pushapp.module.identifier.HttpCacheDir;
import com.linroid.pushapp.ui.bind.BindActivity;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.internal.DiskLruCache;
import com.squareup.okhttp.internal.io.FileSystem;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hugo.weaving.DebugLog;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;
import timber.log.Timber;

/**
 * Created by linroid on 7/24/15.
 */
@Module
public class DataModule {
    @Provides
    @Singleton
    OkHttpClient provideOkHttp(Cache cache) {
        OkHttpClient okHttp = new OkHttpClient();
        okHttp.setCache(cache);
        okHttp.setConnectTimeout(30, TimeUnit.SECONDS);
        okHttp.setReadTimeout(30, TimeUnit.SECONDS);
        okHttp.setWriteTimeout(30, TimeUnit.SECONDS);
        okHttp.networkInterceptors().add(new StethoInterceptor());
        return  okHttp;
    }
    @Provides
    @Singleton
    Client provideClient(OkHttpClient okHttp) {
        return new OkClient(okHttp);
    }

    @Provides
    @Singleton
    Cache provideHttpCache(@HttpCacheDir File httpCacheDir) {
        //100M;
        int cacheSize = 1024 * 1024 * 100;
        try {
            return new Cache(httpCacheDir, cacheSize);
        } catch (Exception e) {
            Timber.e("install http cache false");
        }

        return null;
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new Gson();
    }
    @Provides
    @Singleton
    Converter provideConverter(Gson gson) {
        return new GsonConverter(gson);
    }

    @Provides
    @Singleton
    DiskLruCache provideDataCache(@DataCacheDir File cacheDir) {
        //10M
        return DiskLruCache.create(FileSystem.SYSTEM,
                cacheDir,
                BuildConfig.VERSION_CODE,
                1,
                1024 * 1024 * 10);
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter(Converter converter,
                                   Client client,
                                   ErrorHandler errorHandler,
                                   RequestInterceptor interceptor) {
        return new RestAdapter.Builder()
                .setErrorHandler(errorHandler)
                .setClient(client)
                .setConverter(converter)
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setEndpoint(BuildConfig.ENDPOINT)
                .setRequestInterceptor(interceptor)
                .build();
    }

    @Provides
    RequestInterceptor provideRequestInterceptor(final Account auth) {
        return new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                if (auth.isValid()) {
                    request.addHeader(Constants.AUTH_HEADER, auth.getToken());
                }
            }
        };
    }

    @Provides
    @Singleton
    ErrorHandler provideErrorHandler(final Resources res, final Context context) {
        return new ErrorHandler() {
            @Override
            @DebugLog
            public Throwable handleError(RetrofitError retrofitError) {
                Timber.e(retrofitError, "请求出现错误:%s", retrofitError.getUrl());
                RetrofitError.Kind kind = retrofitError.getKind();
                String message;
                if (retrofitError.getResponse() != null) {
                    if (retrofitError.getResponse().getStatus() == 401) {
                        Intent intent = new Intent(context, BindActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
//                        Toast.makeText(context, R.string.error_unauthorized, Toast.LENGTH_LONG).show();
                        return retrofitError;
                    }
                    try {
                        Error error = (Error) retrofitError.getBodyAs(Error.class);
                        message = error.getMessage();
                    } catch (RuntimeException e) {
                        int status = retrofitError.getResponse().getStatus();
                        if (status / 100 == 5) {
                            message = res.getString(R.string.server_error, status);
                        } else {
                            message = retrofitError.getMessage();
                        }
                    }
                } else {
                    if (RetrofitError.Kind.NETWORK.equals(kind)) {
                        message = res.getString(R.string.network_error);
                    } else if (RetrofitError.Kind.HTTP.equals(kind)) {
                        message = res.getString(R.string.http_error);
                    } else if (RetrofitError.Kind.CONVERSION.equals(kind)) {
                        message = res.getString(R.string.conversion_error);
                    } else {
                        message = res.getString(R.string.unexpected_error);
                    }
                }
                return new Exception(message);
            }
        };
    }


    @Provides
    @Singleton
    Picasso providePicasso(OkHttpClient okHttp, Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        return new Picasso.Builder(context)
                .downloader(new OkHttpDownloader(okHttp))
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Timber.e("Image load failed: %s\n%s", uri, exception.getMessage());
                    }
                })
                        //30M
                .memoryCache(new LruCache(am.getMemoryClass() * 1024 * 1024 / 8))
                .build();
    }

    @Provides
    @Singleton
    SQLiteOpenHelper provideOpenHelper(Application application) {
        return new DbOpenHelper(application);
    }

    @Provides
    @Singleton
    SqlBrite provideSqlBrite() {
        return SqlBrite.create(new SqlBrite.Logger() {
            @Override
            public void log(String message) {
                Timber.tag("SqlBrite").v(message);
            }
        });
    }

    @Provides
    @Singleton
    BriteDatabase provideDatabase(SqlBrite sqlBrite, SQLiteOpenHelper helper) {
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(helper);
        db.setLoggingEnabled(true);
        return db;
    }

}
