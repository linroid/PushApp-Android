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
import android.os.Environment;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
/**
 * Created by linroid on 7/27/15.
 */

public class BuildPropUtils {
    private final Properties properties = new Properties();

    private BuildPropUtils() throws IOException {
        this.properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
    }

    public boolean containsKey(Object key) {
        return this.properties.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return this.properties.containsValue(value);
    }

    public Set<Entry<Object, Object>> entrySet() {
        return this.properties.entrySet();
    }

    public String getProperty(String name) {
        return this.properties.getProperty(name);
    }

    public String getProperty(String name, String defaultValue) {
        return this.properties.getProperty(name, defaultValue);
    }

    public boolean isEmpty() {
        return this.properties.isEmpty();
    }

    public Enumeration<Object> keys() {
        return this.properties.keys();
    }

    public Set<Object> keySet() {
        return this.properties.keySet();
    }

    public int size() {
        return this.properties.size();
    }

    public Collection<Object> values() {
        return this.properties.values();
    }

    public static BuildPropUtils newInstance() throws IOException {
        return new BuildPropUtils();
    }
}
