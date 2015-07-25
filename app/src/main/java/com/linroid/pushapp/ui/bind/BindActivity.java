package com.linroid.pushapp.ui.bind;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewSwitcher;

import com.linroid.pushapp.App;
import com.linroid.pushapp.R;
import com.linroid.pushapp.api.DeviceService;
import com.linroid.pushapp.model.Device;
import com.linroid.pushapp.ui.base.BaseActivity;
import com.linroid.pushapp.ui.home.HomeActivity;
import com.linroid.pushapp.util.DeviceUtil;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class BindActivity extends BaseActivity {
    public static final String ARG_TOKEN = "token";
    public static final int REQ_SCAN_QRCODE = 0x1111;
    @Bind(R.id.open_qrcode)
    Button openQrcodeBtn;
    @Bind(R.id.et_alias)
    EditText aliasET;
    @Bind(R.id.switcher)
    ViewSwitcher switcher;

    @Inject
    DeviceService deviceApi;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        aliasET.setText(getString(R.string.tpl_alias_default, Build.MODEL, Build.VERSION.RELEASE));
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

    private void showBindProgress() {
        ProgressDialog dialog = new ProgressDialog(this, R.style.Theme_AppCompat_Light_Dialog);
        dialog.setIndeterminate(true);
        dialog.setMessage("绑定中...");
        dialog.show();
    }

    @OnClick(R.id.open_qrcode)
    public void onOpenQrcodeBtnClick(Button btn) {
        Intent intent = new Intent(this, QrcodeActivity.class);
        startActivityForResult(intent, REQ_SCAN_QRCODE);
    }

    private void handleIntent(Intent intent) {
        if (intent.hasExtra(ARG_TOKEN)) {
            token = intent.getStringExtra(ARG_TOKEN);
        } else if (intent.getData() != null) {
            token = intent.getData().getQueryParameter(ARG_TOKEN);
        }
        if (!TextUtils.isEmpty(token)) {
            switcher.showNext();
            Snackbar.make(switcher, "扫描成功", Snackbar.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_bind)
    public void onBindBtnClicked(Button btn) {
        final ProgressDialog dialog = new ProgressDialog(this, R.style.Theme_AppCompat_Light_Dialog);
        dialog.setIndeterminate(true);
        dialog.setMessage(getString(R.string.msg_diaolog_bind));
        dialog.setCancelable(false);
        dialog.show();
        deviceApi.bindDevice(queryAndBuildDeviceInfo(), new Callback<Device>() {
            @Override
            @DebugLog
            public void success(Device device, Response response) {
                SharedPreferences sp = getSharedPreferences("pushapp", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("token", device.getToken());
                device.getUser().saveToFile(BindActivity.this);
                editor.apply();
                redirectToHome();
                dialog.dismiss();
            }

            @Override
            @DebugLog
            public void failure(RetrofitError error) {

                dialog.dismiss();
            }
        });
    }

    private void redirectToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            handleIntent(data);
        } else {
            switcher.reset();
        }
    }
    private Device queryAndBuildDeviceInfo(){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);

        return Device.DeviceBuilder.aDevice()
                .withModel(Build.MODEL)
                .withSdkLevel(Build.VERSION.SDK_INT)
                .withOsName(Build.VERSION.RELEASE)
                .withNetworkType(DeviceUtil.networkType(this))
                .withCpuType(Build.CPU_ABI)
                .withAlias(aliasET.getText().toString())
                .withDpi(metrics.densityDpi)
                .withHeight(metrics.heightPixels)
                .withWidth(metrics.widthPixels)
                .withMemorySize(manager.getMemoryClass())
                .withToken(token)
                .withDeviceId(DeviceUtil.id(this))
                        .build();
    }
}
