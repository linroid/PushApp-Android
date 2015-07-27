package com.linroid.pushapp.ui.home;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.linroid.pushapp.App;
import com.linroid.pushapp.BuildConfig;
import com.linroid.pushapp.Constants;
import com.linroid.pushapp.R;
import com.linroid.pushapp.ui.base.BaseActivity;
import com.linroid.pushapp.ui.bind.BindActivity;
import com.linroid.pushapp.util.StringPreference;


import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public class HomeActivity extends BaseActivity {

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;
    @Bind(R.id.viewpager)
    ViewPager pager;
    @Bind(R.id.refresh_fab)
    FloatingActionButton refreshBtn;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    @Named(Constants.SP_TOKEN)
    @Inject
    StringPreference token;

    @Inject
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.from(this).component().inject(this);
        checkBind();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ViewCompat.setElevation(toolbar, 0);
            ActionBar actionBar = getSupportActionBar();
            Intent parent = NavUtils.getParentActivityIntent(this);
            if (parent != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowHomeEnabled(true);
            }
        }
        ButterKnife.bind(this);
        pager.setAdapter(new HomePagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(pager);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "刷新", Snackbar.LENGTH_LONG)
                        .show();
            }
        });

//        if(Build.VERSION.SDK_INT >= 21) {
//            setTaskDescriptionColor();
//        }
    }

    private void checkAutoInstall() {
        boolean confirmed = preferences.getBoolean(Constants.SP_AUTO_INSTALL_CONFIRMED, false);
        if (!confirmed && Build.VERSION.SDK_INT>16) {
            new AlertDialog.Builder(this).setTitle(R.string.title_auto_install_confirm_dialog)
                    .setMessage(R.string.msg_auto_install_confirm_dialog)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor=  preferences.edit();
                            editor.putBoolean(Constants.SP_AUTO_INSTALL_CONFIRMED, true);
//                            editor.putBoolean(Constants.SP_AUTO_INSTALL, true);
                            editor.apply();

                            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                            startActivityForResult(intent, 0);
                            dialog.cancel();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            preferences.edit().putBoolean(Constants.SP_AUTO_INSTALL_CONFIRMED, true).apply();
                            Snackbar.make(refreshBtn, R.string.msg_auto_install_confirm_cancel, Snackbar.LENGTH_LONG);
                        }
                    }).show();
        }
        //TODO: 确认并开启，但是服务关了
    }

    private void checkBind() {
        if (TextUtils.isEmpty(token.getValue())) {
            Timber.w("device does not bind");
            Intent intent = new Intent(this, BindActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAutoInstall();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_home;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void setTaskDescriptionColor() {
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(getString(R.string.app_name), bm, Color.WHITE);
        setTaskDescription(taskDesc);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
