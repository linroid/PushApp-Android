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
