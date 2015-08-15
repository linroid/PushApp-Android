package com.linroid.pushapp.ui.device;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.linroid.pushapp.App;
import com.linroid.pushapp.R;
import com.linroid.pushapp.api.PushService;
import com.linroid.pushapp.model.Pack;
import com.linroid.pushapp.model.Push;
import com.linroid.pushapp.ui.base.BaseActivity;
import com.linroid.pushapp.ui.home.HomeActivity;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.List;

import javax.inject.Inject;

import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SelectDeviceActivity extends BaseActivity {
    public static final String EXTRA_PACKAGE = "package";
    public static final String EXTRA_APPLICATION = "application";
    public static final int REQUEST_PUSH = 0x9999;
    private OnSelectListener selectListener;
    @Inject
    BriteDatabase db;
    @Inject
    PushService installApi;
    Pack pack;

    public static void selectForPackage(Activity source, Pack pack) {
        Intent intent = new Intent(source, SelectDeviceActivity.class);
        intent.putExtra(EXTRA_PACKAGE, pack);
        source.startActivityForResult(intent, REQUEST_PUSH);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_PACKAGE)) {
            this.pack = intent.getParcelableExtra(EXTRA_PACKAGE);
        }
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

        Callback<Push> callback = new Callback<Push>() {

            @Override
            public void success(Push push, Response response) {
                Intent intent = getIntent();
                intent.putExtra(HomeActivity.EXTRA_MESSAGE, "推送成功!");
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Snackbar.make(btn, error.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        };
        if (pack != null) {
            installApi.installPackage(TextUtils.join(",", selectedIds.toArray()), pack.getId(), callback);
        }
    }

    public interface OnSelectListener {
        void onSelectAll();

        void onSelectNone();

        List<Integer> onObtainSelectedDeviceIds();
    }
}
