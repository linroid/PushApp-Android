package com.linroid.pushapp.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.linroid.pushapp.ui.app.AppFragment;
import com.linroid.pushapp.ui.auth.AuthFragment;
import com.linroid.pushapp.ui.device.DeviceFragment;
import com.linroid.pushapp.ui.pack.PackageFragment;

/**
 * Created by linroid on 7/20/15.
 */
public class HomePagerAdapter extends FragmentPagerAdapter {
    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
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
                title = "推送";
                break;
            case 1:
                title = "本地";
                break;
            case 2:
            default:
                title = "授权";
                break;
        }
        return title;
    }
}
