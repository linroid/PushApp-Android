package com.linroid.pushapp.ui.bind;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.CompoundBarcodeView;
import com.linroid.pushapp.R;
import com.linroid.pushapp.ui.base.BaseActivity;

import java.util.List;

import butterknife.Bind;
import hugo.weaving.DebugLog;
import timber.log.Timber;

public class QrcodeActivity extends BaseActivity {
    public static final String STATE_TORCH = "torch";
    @Bind(R.id.scanner)
    public CompoundBarcodeView scannerView;
    private boolean isTorchOn = false;
    CaptureManager capture;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        if (state != null) {
            isTorchOn = state.getBoolean(STATE_TORCH);
        }
        scannerView.setStatusText(getString(R.string.msg_scanner, getString(R.string.host_url)));
        capture = new CaptureManager(this, this.scannerView);
        capture.initializeFromIntent(getIntent(), state);
        capture.decode();
        scannerView.decodeContinuous(new BarcodeCallback() {
            @DebugLog
            @Override
            public void barcodeResult(BarcodeResult barcodeResult) {
                Uri uri = Uri.parse(barcodeResult.getText());
                String token = uri.getQueryParameter("token");
                if(!TextUtils.isEmpty(token) && token.length()==64) {
                    onScanSuccess(token);
                } else {
                    handleUnknownBarcode(barcodeResult.getText());
                }
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> list) {

            }
        });
    }
    private void handleUnknownBarcode(String text) {
        Timber.e("Unknown barcode content: %s", text);
    }
    private void onScanSuccess(String token) {
        Intent intent = getIntent();
        intent.putExtra(BindActivity.ARG_BIND_TOKEN, token);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_qrcode;
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
        getMenuInflater().inflate(R.menu.qrcode, menu);
        MenuItem menuItem = menu.findItem(R.id.action_flash);
        if (isTorchOn) {
            menuItem.setTitle(R.string.action_torch_on);
        } else {
            menuItem.setTitle(R.string.action_torch_off);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_flash:
                if (!isTorchOn) {
                    scannerView.setTorchOn();
                } else {
                    scannerView.setTorchOff();
                }
                isTorchOn = !isTorchOn;
                item.setTitle( isTorchOn ? R.string.action_torch_on : R.string.action_torch_off);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}