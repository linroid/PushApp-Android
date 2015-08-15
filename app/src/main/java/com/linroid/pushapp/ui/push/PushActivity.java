package com.linroid.pushapp.ui.push;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.util.Pair;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;

import com.linroid.pushapp.App;
import com.linroid.pushapp.R;
import com.linroid.pushapp.api.PushService;
import com.linroid.pushapp.model.Pack;
import com.linroid.pushapp.model.Push;
import com.linroid.pushapp.ui.base.BaseActivity;
import com.linroid.pushapp.ui.device.DeviceFragment;
import com.linroid.pushapp.ui.home.HomeActivity;
import com.linroid.pushapp.util.CountingTypedFile;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedString;
import rx.Subscriber;
import rx.Subscription;

public class PushActivity extends BaseActivity
        implements Callback<Push> {
    public static final String EXTRA_PACKAGE = "package";
    public static final String EXTRA_APPLICATION_INFO = "application";
    public static final int REQUEST_PUSH = 0x9999;
    private OnSelectListener selectListener;
    @Inject
    PushService installApi;
    @Bind(R.id.fab_complete)
    FloatingActionButton completeBtn;

    Pack pack;
    ApplicationInfo appInfo;

    ProgressDialog dialog;
    Subscription subscription;

    public static void selectForPackage(Activity source, Pack pack) {
        Intent intent = new Intent(source, PushActivity.class);
        intent.putExtra(EXTRA_PACKAGE, pack);
        source.startActivityForResult(intent, REQUEST_PUSH);
    }

    public static void selectForPackage(Activity source, ApplicationInfo info) {
        Intent intent = new Intent(source, PushActivity.class);
        intent.putExtra(EXTRA_APPLICATION_INFO, info);
        source.startActivityForResult(intent, REQUEST_PUSH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_PACKAGE)) {
            this.pack = intent.getParcelableExtra(EXTRA_PACKAGE);
        } else if (intent.hasExtra(EXTRA_APPLICATION_INFO)) {
            this.appInfo = intent.getParcelableExtra(EXTRA_APPLICATION_INFO);
        } else {
            throw new IllegalArgumentException("EXTRA_PACKAGE or EXTRA_APPLICATION_INFO extra data required");
        }
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.msg_upload_prepare));
        dialog.setMax(100);
        dialog.setCancelable(false);
        dialog.setIndeterminate(true);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, new DeviceFragment())
                .commit();

        App.from(this).component().inject(this);
    }

    public void setSelectListener(OnSelectListener selectListener) {
        this.selectListener = selectListener;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_select_device;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.select_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (this.selectListener != null) {
            if (id == R.id.action_select_all) {
                selectListener.onSelectAll();
                return true;
            } else if (id == R.id.action_select_none) {
                selectListener.onSelectNone();
                return false;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.fab_complete)
    void onCompleteBtnClicked(final FloatingActionButton btn) {
        if (selectListener == null) {
            throw new IllegalStateException();
        }
        List<Integer> selectedIds = selectListener.onObtainSelectedDeviceIds();
        if (selectedIds.size() == 0) {
            Snackbar.make(btn, R.string.error_not_select_any_device, Snackbar.LENGTH_SHORT).show();
            return;
        }
        Snackbar.make(btn, getString(R.string.msg_push_install_package, selectedIds.size()), Snackbar.LENGTH_SHORT).show();
        String deviceIds = TextUtils.join(",", selectedIds.toArray());
        if (pack != null) {
            installApi.installPackage(deviceIds, pack.getId(), this);
        } else {
            File apkFile = new File(appInfo.sourceDir);
            CountingTypedFile typedFile = new CountingTypedFile(apkFile);
            dialog.show();
            typedFile.subscribe(new Subscriber<Pair<Long, Long>>() {
                @Override
                public void onCompleted() {
                    dialog.setMessage(getString(R.string.msg_upload_complete));
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(Pair<Long, Long> pair) {
                    dialog.setMessage(getString(R.string.msg_upload_progress,
                            Formatter.formatShortFileSize(PushActivity.this, pair.second),
                            Formatter.formatShortFileSize(PushActivity.this, pair.first)));
                }
            });
            installApi.installLocal(new TypedString(deviceIds), typedFile, this);
        }
    }

    @Override
    public void success(Push push, Response response) {
        Intent intent = getIntent();
        intent.putExtra(HomeActivity.EXTRA_MESSAGE, getString(R.string.msg_push_success));
        setResult(RESULT_OK, intent);
        dialog.dismiss();
        finish();
    }

    @Override
    public void failure(RetrofitError error) {
        Snackbar.make(completeBtn, error.getMessage(), Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(subscription!=null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    public interface OnSelectListener {
        void onSelectAll();

        void onSelectNone();

        List<Integer> onObtainSelectedDeviceIds();
    }
}
