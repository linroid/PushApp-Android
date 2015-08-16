package com.linroid.pushapp.ui.setting;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;

import com.linroid.pushapp.App;
import com.linroid.pushapp.R;
import com.linroid.pushapp.model.Binding;

import javax.inject.Inject;

/**
 * Created by linroid on 8/16/15.
 */
public class SettingFragment extends PreferenceFragment {
    @Inject
    Binding binding;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        App.from(activity).component().inject(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        findPreference(getString(R.string.pref_bind_account)).setSummary(binding.getUser().getNickname());
    }
}
