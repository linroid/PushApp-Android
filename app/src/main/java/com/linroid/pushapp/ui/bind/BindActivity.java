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

package com.linroid.pushapp.ui.bind;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.linroid.pushapp.App;
import com.linroid.pushapp.BuildConfig;
import com.linroid.pushapp.Constants;
import com.linroid.pushapp.R;
import com.linroid.pushapp.api.AuthService;
import com.linroid.pushapp.model.Account;
import com.linroid.pushapp.model.Device;
import com.linroid.pushapp.model.User;
import com.linroid.pushapp.ui.base.BaseActivity;
import com.linroid.pushapp.ui.home.HomeActivity;
import com.linroid.pushapp.util.AndroidUtil;
import com.linroid.pushapp.util.DeviceUtil;
import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BindActivity extends BaseActivity {
    @Bind(R.id.btn_open_qrcode)
    Button openQrcodeBtn;
    @Bind(R.id.et_alias)
    EditText aliasET;
    @Bind(R.id.switcher)
    ViewSwitcher switcher;
    @Bind(R.id.tv_first_content)
    TextView firstContentTV;

    @Inject
    AuthService authApi;
    @Inject
    Account account;

    private String bindToken;
    private boolean showProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        aliasET.setText(getString(R.string.tpl_alias_default, Build.MODEL, Build.VERSION.RELEASE));
        firstContentTV.setText(getString(R.string.bind_fist_content, BuildConfig.HOST_URL));
        App.from(this).component().inject(this);

    }
//
//    @Override
//    public BindComponent provideComponent() {
//        return DaggerBindComponent.builder()
//                .appComponent(App.from(this).component())
//                .build();
//    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_bind;
    }

    @OnClick(R.id.btn_open_qrcode)
    public void onOpenQrcodeBtnClick(Button btn) {
        Intent intent = ScanActivity.createNewScanIntent(this,
                                                        getString(R.string.txt_scan_bind, BuildConfig.HOST_URL),
                                                        Constants.QRCODE_KEY_AUTH);
        startActivityForResult(intent, ScanActivity.REQ_SCAN_QRCODE);
    }

    private void handleIntent(Intent intent) {
        String key = intent.getStringExtra(ScanActivity.ARG_REQUIRE_KEY);
        bindToken = intent.getStringExtra(ScanActivity.EXTRA_QRCODE_VALUE);
        if (!TextUtils.isEmpty(bindToken)) {
            showSecond();
            Snackbar.make(switcher, R.string.msg_scan_auth_success, Snackbar.LENGTH_SHORT).show();
            checkToken();
        }
    }

    private void showSecond() {
        if (switcher.getDisplayedChild() == 0) {
            switcher.showNext();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void showFirst() {
        if (switcher.getDisplayedChild() == 1) {
            switcher.showNext();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    private void checkToken() {
        setProgressVisible(true);
        invalidateOptionsMenu();
        authApi.checkToken(bindToken, DeviceUtil.id(this), new Callback<Device>() {
            @Override
            @DebugLog
            public void success(Device device, Response response) {
                if (device != null && !TextUtils.isEmpty(device.getAlias())) {
                    aliasET.setText(device.getAlias());
                }
                setProgressVisible(false);
            }

            @Override
            public void failure(RetrofitError error) {
                setProgressVisible(false);
            }
        });
    }

    private void setProgressVisible(boolean show) {
        if (show != showProgress) {
            showProgress = false;
            invalidateOptionsMenu();
        }
    }

    @OnClick(R.id.btn_bind)
    public void onBindBtnClicked(Button btn) {
        final ProgressDialog dialog = new ProgressDialog(this, R.style.Theme_AppCompat_Light_Dialog);
        dialog.setIndeterminate(true);
        dialog.setMessage(getString(R.string.msg_dialog_bind));
        dialog.setCancelable(false);
        dialog.show();
        authApi.bindDevice(bindToken, queryAndBuildDeviceInfo(), new Callback<Account>() {
            @Override
            @DebugLog
            public void success(Account authInfo, Response response) {
                Device device = authInfo.getDevice();
                User user = authInfo.getUser();
                String token = authInfo.getToken();
                account.setDevice(device);
                account.setUser(user);
                account.setToken(token);
                account.saveToFile();

                dialog.dismiss();
                redirectToHome();
            }

            @Override
            @DebugLog
            public void failure(RetrofitError error) {
                Snackbar.make(switcher, error.getMessage(), Snackbar.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    private void redirectToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            handleIntent(data);
        } else {
            showFirst();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (switcher.getDisplayedChild() == 1) {
                showFirst();
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private Device queryAndBuildDeviceInfo() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return Device.DeviceBuilder.aDevice()
                .withModel(Build.MODEL)
                .withSdkLevel(Build.VERSION.SDK_INT)
                .withOsName(Build.VERSION.RELEASE)
                .withNetworkType(DeviceUtil.networkType(this))
                .withCpuType(Build.CPU_ABI)
                .withAlias(aliasET.getText().toString())
                .withDpi(metrics.densityDpi)
                .withHeight(AndroidUtil.getRealHeight(getWindowManager().getDefaultDisplay()))
                .withWidth(metrics.widthPixels)
                .withMemorySize(AndroidUtil.totalMemorySize())
                .withToken(bindToken)
                .withUniqueId(DeviceUtil.id(this))
                .withInstallId(JPushInterface.getRegistrationID(this))
                .build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.bind, menu);
        MenuItem item = menu.findItem(R.id.progressbar);
        MenuItemCompat.setActionView(item, R.layout.progressbar);
        item.setVisible(showProgress);
        return true;
    }
}
