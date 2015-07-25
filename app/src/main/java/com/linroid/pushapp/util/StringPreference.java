package com.linroid.pushapp.util;

import android.content.SharedPreferences;

import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * Created by linroid on 7/25/15.
 */
public class StringPreference {
    private SharedPreferences sp;
    private String key;
    private String value;

    public StringPreference(SharedPreferences sp, String key) {
        this.sp = sp;
        this.key = key;
        this.value = sp.getString(key, null);
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        Timber.d("new value: %s => %s ", key, value);
        this.value = value;
        sp.edit().putString(key, value).apply();
    }
}
