package com.linroid.pushapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by linroid on 7/31/15.
 */
public class DbOpenHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "pushapp.db";
    public static final int DB_VERSION = 1;

    private static final String CREATE_LIST = ""
            + "CREATE TABLE " + TodoList.TABLE + "("
            + TodoList.ID + " INTEGER NOT NULL PRIMARY KEY,"
            + TodoList.NAME + " TEXT NOT NULL,"
            + TodoList.ARCHIVED + " INTEGER NOT NULL DEFAULT 0"
            + ")";
    private static final String CREATE_ITEM = ""
            + "CREATE TABLE " + TodoItem.TABLE + "("
            + TodoItem.ID + " INTEGER NOT NULL PRIMARY KEY,"
            + TodoItem.LIST_ID + " INTEGER NOT NULL REFERENCES " + TodoList.TABLE + "(" + TodoList.ID + "),"
            + TodoItem.DESCRIPTION + " TEXT NOT NULL,"
            + TodoItem.COMPLETE + " INTEGER NOT NULL DEFAULT 0"
            + ")";
    private static final String CREATE_ITEM_LIST_ID_INDEX =
            "CREATE INDEX item_list_id ON " + TodoItem.TABLE + " (" + TodoItem.LIST_ID + ")";

    public DbOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
