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

package com.linroid.pushapp.ui.setting;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linroid.pushapp.BuildConfig;
import com.linroid.pushapp.R;
import com.linroid.pushapp.ui.base.BaseActivity;
import com.linroid.pushapp.util.ShareUtils;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 关于
 */
public class AboutActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;
    @Bind(R.id.tv_version)
    TextView versionTV;
    @Bind(R.id.about_header)
    LinearLayout headerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ViewCompat.setElevation(toolbar, 0);
        }
        initVersionName();
//        GradientDrawable drawable = (GradientDrawable) headerContainer.getBackground();
//        drawable.setGradientRadius(getResources().getDimensionPixelOffset(R.dimen.about_header_bg_radius));
//        headerContainer.setBackgroundDrawable(drawable);
    }

    @OnClick(R.id.collapsing_toolbar)
    void onCollapsingToolbarClicked() {
        onBackPressed();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_about;
    }


    private void initVersionName() {
        versionTV.setText(getString(R.string.txt_about_version, BuildConfig.VERSION_NAME));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_share:
                ShareUtils.share(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
