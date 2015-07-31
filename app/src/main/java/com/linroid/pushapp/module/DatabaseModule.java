package com.linroid.pushapp.module;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.linroid.pushapp.database.DbOpenHelper;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * Created by linroid on 7/31/15.
 */
@Module
public class DatabaseModule {
    @Provides
    @DebugLog
    @Singleton
    BriteDatabase provideBrite(SQLiteOpenHelper helper) {
        SqlBrite sqlBrite = SqlBrite.create(new SqlBrite.Logger() {
            @Override
            public void log(String s) {
                Timber.tag("SqlBrite").d(s);
            }
        });
        return sqlBrite.wrapDatabaseHelper(helper);
    }

    @Provides
    @Singleton
    SQLiteOpenHelper provideDBOpenHelper(Application app) {
        return new DbOpenHelper(app);
    }
}
