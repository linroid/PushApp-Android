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
import android.content.res.Resources;

/**
 * Created by linroid on 8/23/15. <br/>
 * 与原作者<a href="https://github.com/drakeet/">drakeet</a>的沟通下获得在Apache License 2.0下开源使用.<br/>
 * 如果有人需要另外使用此文件，还需遵循原作者协议或与原作者沟通.<br/>
 * See <a href="https://github.com/drakeet/Meizhi/blob/master/app/src/main/java/me/drakeet/meizhi/util/Once.java">Once.java</a>
 */
public class Once {

    private SharedPreferences preferences;
    private Resources resources;

    public Once(Resources resources, SharedPreferences preferences) {
        this.preferences = preferences;
        this.resources = resources;
    }

    public void show(String onceKey, OnceCallback callback) {
        boolean hasShown = preferences.getBoolean(onceKey, false);
        if (!hasShown) {
            callback.onOnce();
            preferences.edit().putBoolean(onceKey, true).apply();
        }
    }

    public void show(int keyResId, OnceCallback callback) {
        show(resources.getString(keyResId), callback);
    }

    public interface OnceCallback {
        void onOnce();
    }
}
