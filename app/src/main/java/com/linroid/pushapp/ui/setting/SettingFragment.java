package com.linroid.pushapp.ui.setting;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.linroid.pushapp.App;
import com.linroid.pushapp.R;
import com.linroid.pushapp.model.Account;

import javax.inject.Inject;

/**
 * Created by linroid on 8/16/15.
 */
public class SettingFragment extends PreferenceFragment {
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
    }
}
