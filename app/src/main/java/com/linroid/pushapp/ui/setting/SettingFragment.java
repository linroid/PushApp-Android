package com.linroid.pushapp.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.linroid.pushapp.App;
import com.linroid.pushapp.R;
import com.linroid.pushapp.model.Account;

import javax.inject.Inject;

/**
 * Created by linroid on 8/16/15.
 * 设置界面的真正设置列
 */
public class SettingFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    @Inject
    Account account;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        App.from(activity).component().inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);

        findPreference(getString(R.string.pref_bind_account)).setSummary(account.getUser().getNickname());
        findPreference(getString(R.string.pref_about_us)).setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        if (key.equals(getString(R.string.pref_about_us))) {
            startActivity(new Intent(getActivity(), AboutActivity.class));
        }
        return false;
    }
}
