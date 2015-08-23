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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.linroid.pushapp.BuildConfig;
import com.linroid.pushapp.Constants;
import com.linroid.pushapp.R;
import com.linroid.pushapp.ui.base.BaseActivity;

import java.util.List;

import butterknife.Bind;
import hugo.weaving.DebugLog;
import timber.log.Timber;

public class ScanActivity extends BaseActivity {
    public static final String STATE_TORCH = "torch";
    public static final int REQ_SCAN_QRCODE = 0x1111;
    public static final String ARG_REQUIRE_KEY = "require_key";
    public static final String ARG_TIP = "tip";
    public static final String EXTRA_QRCODE_KEY = "key";
    public static final String EXTRA_QRCODE_VALUE = "value";
    @Bind(R.id.scanner)
    public CompoundBarcodeView scannerView;
    @Bind(R.id.tip_tv)
    public TextView tipTV;
    private boolean isTorchOn = false;
    private CaptureManager capture;
    private String requireKey;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        if (state != null) {
            isTorchOn = state.getBoolean(STATE_TORCH);
        }
        Intent intent = getIntent();
        if (intent.hasExtra(ARG_REQUIRE_KEY)) {
            requireKey = intent.getStringExtra(ARG_REQUIRE_KEY);
        }
        String tip;
        if (intent.hasExtra(ARG_TIP)) {
            tip = intent.getStringExtra(ARG_TIP);
        } else {
            tip = getString(R.string.msg_scanner, BuildConfig.HOST_URL);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        scannerView.setStatusText(null);
        tipTV.setText(tip);
        capture = new CaptureManager(this, this.scannerView);
        capture.initializeFromIntent(getIntent(), state);
        capture.decode();
        scannerView.decodeContinuous(new BarcodeCallback() {
            @DebugLog
            @Override
            public void barcodeResult(BarcodeResult barcodeResult) {
                Timber.d("扫描到信息:%s", barcodeResult.getText());
                Uri uri = Uri.parse(barcodeResult.getText());

                List<String> segments = uri.getPathSegments();
                if(BuildConfig.HOST.equals(uri.getHost()) && segments.size()==3) {
                    if (Constants.QRCODE.equals(segments.get(0))) {
                        String key = segments.get(1);
                        String value = segments.get(2);
                        Timber.d("%s => %s", key, value);
                        if (!TextUtils.isEmpty(requireKey) && requireKey.equals(key)) {
                            onScanSuccess(key, value);
                            return;
                        }
                    }

                }
                handleUnknownQrcode(barcodeResult.getText());
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> list) {

            }
        });
    }

    /**
     * 处理未知的二维码扫描结果
     * @param text
     */
    private void handleUnknownQrcode(final String text) {
        Timber.e("Unknown qrcode content: %s", text);
        Uri uri = Uri.parse(text);
        // 内容为Uri
        if (!TextUtils.isEmpty(uri.getScheme())) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            PackageManager pm = getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(intent, 0);
            if (activities.size() > 0) {
                startActivity(Intent.createChooser(intent, getString(R.string.title_choose_app)));
            } else {
                handleResultAsPlainText(text);
            }
        } else {
            handleResultAsPlainText(text);
        }
    }

    /**
     * 当作纯文本处理
     * @param text
     */
    private void handleResultAsPlainText(final String text) {
        capture.onPause();
        new AlertDialog.Builder(this)
                .setTitle(R.string.title_dialog_unsupport_qrcode)
                .setMessage(text)
                .setPositiveButton(R.string.btn_dialog_copy, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        cm.setPrimaryClip(ClipData.newPlainText("qrcode", text));
                        Snackbar.make(scannerView, "复制成功!", Snackbar.LENGTH_SHORT).show();
                        capture.onResume();
                    }
                })
                .show();
    }

    private void onScanSuccess(String key, String value) {
        Intent intent = getIntent();
        intent.putExtra(EXTRA_QRCODE_KEY, key);
        intent.putExtra(EXTRA_QRCODE_VALUE, value);
        setResult(RESULT_OK, intent);
        finish();
    }


    public static Intent createNewScanIntent(Activity activity, String tip) {
        Intent intent = new Intent(activity, ScanActivity.class);
        intent.putExtra(ARG_TIP, tip);
        return intent;
    }
    public static Intent createNewScanIntent(Activity activity, String tip, String requireKey) {
        Intent intent = new Intent(activity, ScanActivity.class);
        intent.putExtra(ARG_REQUIRE_KEY, requireKey);
        intent.putExtra(ARG_TIP, tip);
        return intent;
    }
    @Override
    protected int provideContentViewId() {
        return R.layout.activity_scan;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_TORCH, isTorchOn);
    }

    @Override
    public void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        capture.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return scannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.scan, menu);
//        MenuItem menuItem = menu.findItem(R.id.action_flash);
//        if (isTorchOn) {
//            menuItem.setTitle(R.string.action_torch_on);
//        } else {
//            menuItem.setTitle(R.string.action_torch_off);
//        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_flash:
//                if (!isTorchOn) {
//                    scannerView.setTorchOn();
//                } else {
//                    scannerView.setTorchOff();
//                }
//                isTorchOn = !isTorchOn;
//                item.setTitle(isTorchOn ? R.string.action_torch_on : R.string.action_torch_off);
//                return true;
//        }
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}