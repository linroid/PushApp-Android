package com.linroid.pushapp.module;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.google.gson.Gson;
import com.linroid.pushapp.BuildConfig;
import com.linroid.pushapp.Constants;
import com.linroid.pushapp.R;
import com.linroid.pushapp.database.DbOpenHelper;
import com.linroid.pushapp.model.Error;
import com.linroid.pushapp.module.identifier.DataCacheDir;
import com.linroid.pushapp.module.identifier.HttpCacheDir;
import com.linroid.pushapp.util.StringPreference;
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

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hugo.weaving.DebugLog;
import retrofit.ErrorHandler;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
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
        return okHttp;
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
    RestAdapter provideRestAdapter(Gson gson, OkHttpClient okHttpClient,
                                   @Named(Constants.SP_TOKEN) final StringPreference token,
                                   ErrorHandler errorHandler) {
        return new RestAdapter.Builder()
                .setErrorHandler(errorHandler)
                .setClient(new OkClient(okHttpClient))
                .setConverter(new GsonConverter(gson))
                .setLogLevel(RestAdapter.LogLevel.BASIC)
                .setEndpoint(BuildConfig.ENDPOINT)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        if (!TextUtils.isEmpty(token.getValue())) {
                            request.addHeader(Constants.AUTH_HEADER, token.getValue());
                        }
                    }
                })
                .build();
    }

    @Provides
    @Singleton
    ErrorHandler provideErrorHandler(final Resources res) {
        return new ErrorHandler() {
            @Override
            @DebugLog
            public Throwable handleError(RetrofitError retrofitError) {
                Timber.e(retrofitError, "请求出现错误:%s", retrofitError.getUrl());
                RetrofitError.Kind kind = retrofitError.getKind();
                String message;
                if (retrofitError.getResponse() != null) {
                    try {
                        Error error = (Error) retrofitError.getBodyAs(Error.class);
                        message = error.getMessage();
                    } catch (RuntimeException e) {
                        message = res.getString(R.string.server_error);
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
                .memoryCache(new LruCache(am.getMemoryClass()*1024*1024 / 8))
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
