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


import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
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
        Timber.tag(this.getLocalClassName()).d("onCreate");
        setContentView(provideContentViewId());
        ButterKnife.bind(this);

        if(Build.VERSION.SDK_INT >= 21) {
            setTaskDescriptionColor();
        }
    }

    protected abstract int provideContentViewId();

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        Intent parent = NavUtils.getParentActivityIntent(this);
        if (parent != null && actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (NavUtils.getParentActivityIntent(this) != null) {
                finish();
                return true;
            }
            NavUtils.navigateUpTo(this, NavUtils.getParentActivityIntent(this));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Timber.tag(this.getLocalClassName()).i("onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.tag(this.getLocalClassName()).i("onDestroy");
    }

    @Override
    public void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        Timber.tag(this.getLocalClassName()).i("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        Timber.tag(this.getLocalClassName()).i("onResume");
    }

    @Override
    public void onStart() {
        super.onStart();
        Timber.tag(this.getLocalClassName()).i("onStart");
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTaskDescriptionColor() {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(getString(R.string.app_name),
                bm, getResources().getColor(R.color.task_color));
        setTaskDescription(taskDesc);
    }

}
