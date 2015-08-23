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

package com.linroid.pushapp.ui.base;

import android.os.Bundle;

import com.linroid.pushapp.AppComponent;

import hugo.weaving.DebugLog;

/**
 * Created by linroid on 7/24/15.
 */
public abstract class InjectableActivity<T extends AbstractActivityComponent> extends BaseActivity {
    protected T component;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        component = provideComponent();
        component.inject(this);
    }

    @DebugLog
    public void inject(Object target) {
    }

    public T component() {
        return component;
    }

    public abstract T provideComponent();

}
