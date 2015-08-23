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

package com.linroid.pushapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.linroid.pushapp.Constants;
import com.linroid.pushapp.model.Device;
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
        db.execSQL(Device.DB.SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
