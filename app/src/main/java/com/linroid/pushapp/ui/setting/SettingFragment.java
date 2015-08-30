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

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.linroid.pushapp.App;
import com.linroid.pushapp.R;
import com.linroid.pushapp.model.Account;
import com.linroid.pushapp.model.Pack;
import com.linroid.pushapp.ui.bind.BindActivity;
import com.squareup.sqlbrite.BriteDatabase;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.ButterKnife;

/**
 * Created by linroid on 8/16/15.
 * 设置界面的真正设置列
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    @Inject
    Account account;
    @Inject
    BriteDatabase db;

//    @BindString(R.string.pref_about_us)
    String aboutUS;
//    @BindString(R.string.pref_auto_install)
    String autoInstall;
//    @BindString(R.string.pref_auto_open)
    String autoOpen;
//    @BindString(R.string.pref_bind_account)
    String bindAccount;
//    @BindString(R.string.pref_accessibility_service)
    String accessibilityService;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        App.from(activity).component().inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        ButterKnife.bind(this, getActivity());

        //
        this.aboutUS = getString(R.string.pref_about_us);
        this.autoInstall = getString(R.string.pref_auto_install);
        this.autoOpen = getString(R.string.pref_auto_open);
        this.bindAccount = getString(R.string.pref_bind_account);
        this.accessibilityService = getString(R.string.pref_accessibility_service);

        Preference bindAccountPreference = findPreference(bindAccount);
        bindAccountPreference.setSummary(account.getUser().getNickname());
        bindAccountPreference.setOnPreferenceClickListener(this);

        findPreference(aboutUS).setOnPreferenceClickListener(this);
        findPreference(accessibilityService).setOnPreferenceClickListener(this);

    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(aboutUS)) {
            startActivity(new Intent(getActivity(), AboutActivity.class));
            return true;
        } else if (key.equals(bindAccount)) {
            showLogoutConfirmDialog();
            return true;
        } else if (key.equals(accessibilityService)) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivityForResult(intent, 0);
            return true;
        }
        return false;
    }

    private void showLogoutConfirmDialog() {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.title_logout_account, account.getUser().getNickname()))
                .setMessage(R.string.msg_logout_account)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        account.invalidate();
                        db.delete(Pack.DB.TABLE_NAME, "1");
                        Intent intent = new Intent(getActivity(), BindActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }).show();
    }
}
