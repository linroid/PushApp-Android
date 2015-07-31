package com.linroid.pushapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.linroid.pushapp.Constants;
import com.linroid.pushapp.model.Pack;

import timber.log.Timber;

/**
 * Created by linroid on 7/31/15.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    public DbOpenHelper(Context ctx) {
        super(ctx, Constants.DB_NAME, null, Constants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Timber.d("onCreate Database");
        db.execSQL(Pack.DB.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
