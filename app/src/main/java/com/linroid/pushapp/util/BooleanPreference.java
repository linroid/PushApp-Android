package com.linroid.pushapp.util;

import android.content.SharedPreferences;

import timber.log.Timber;

/**
 * Created by linroid on 7/31/15.
 */
public class BooleanPreference {
    private SharedPreferences sp;
    private String key;
    private boolean value;

    public BooleanPreference(SharedPreferences sp, String key, boolean defValue) {
        this.sp = sp;
        this.key = key;
        this.value = sp.getBoolean(key, defValue);
        Timber.d("%s => %s", key, this.value);
    }


    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        Timber.d("new value: %s => %s ", key, value);
        this.value = value;
        sp.edit().putBoolean(key, value).apply();
    }
}
