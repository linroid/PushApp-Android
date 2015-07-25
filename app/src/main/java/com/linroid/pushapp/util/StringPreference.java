package com.linroid.pushapp.util;

import android.content.SharedPreferences;

import hugo.weaving.DebugLog;
import timber.log.Timber;

/**
 * Created by linroid on 7/25/15.
 */
public class StringPreference {
    SharedPreferences sp;
    String key;
    String value;
    SharedPreferences.OnSharedPreferenceChangeListener listener;

    public StringPreference(SharedPreferences sp, String key) {
        this.sp = sp;
        this.key = key;
        fetch();
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @DebugLog
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (StringPreference.this.key.equals(key)) {
                    fetch();
                }
            }
        };
        sp.registerOnSharedPreferenceChangeListener(listener);
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void fetch() {
        value = sp.getString(key, null);
        Timber.d("new value %s", value);
    }
}
