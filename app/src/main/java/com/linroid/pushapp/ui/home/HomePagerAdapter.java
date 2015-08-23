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

package com.linroid.pushapp.ui.home;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.linroid.pushapp.R;
import com.linroid.pushapp.ui.app.AppFragment;
import com.linroid.pushapp.ui.auth.AuthFragment;
import com.linroid.pushapp.ui.pack.PackageFragment;

/**
 * Created by linroid on 7/20/15.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {
    Resources resources;
    public HomePagerAdapter(FragmentManager fm, Resources resources) {
        super(fm);
        this.resources = resources;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new PackageFragment();
                break;
            case 1:
                fragment = new AppFragment();
                break;
            case 2:
            default:
                fragment = new AuthFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title;
        switch (position) {
            case 0:
                title = resources.getString(R.string.title_fragment_push);
                break;
            case 1:
                title = resources.getString(R.string.title_fragment_app);
                break;
            case 2:
            default:
                title = resources.getString(R.string.title_fragment_auth);
                break;
        }
        return title;
    }
}
