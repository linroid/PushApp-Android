package com.linroid.pushapp.ui.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.linroid.pushapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import timber.log.Timber;

/**
 * Created by linroid on 7/23/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideContentViewId());
        ButterKnife.bind(this);
    }

    protected abstract int provideContentViewId();


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            NavUtils.navigateUpTo(this, NavUtils.getParentActivityIntent(this));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.i("onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.i("onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        Timber.i("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        Timber.i("onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.i("onStart");
    }
}
