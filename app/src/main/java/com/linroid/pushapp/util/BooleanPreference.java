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
