package com.linroid.pushapp.ui.setting;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linroid.pushapp.BuildConfig;
import com.linroid.pushapp.R;
import com.linroid.pushapp.util.ShareUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AboutActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.tv_version)
    TextView mVersionTextView;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.about_header)
    LinearLayout headerLL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        initVersionName();

        mCollapsingToolbarLayout.setTitle(getString(R.string.app_name));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        GradientDrawable drawable = (GradientDrawable) headerLL.getBackground();
        drawable.setGradientRadius(getResources().getDimensionPixelOffset(R.dimen.about_header_bg_radius));
        headerLL.setBackgroundDrawable(drawable);
    }


    private void initVersionName() {
        mVersionTextView.setText("Version " + BuildConfig.VERSION_NAME);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.menu_share:
                ShareUtils.share(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
