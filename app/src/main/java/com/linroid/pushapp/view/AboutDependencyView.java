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

package com.linroid.pushapp.view;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linroid.pushapp.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 2bab on 15/8/22.
 * <p/>
 * 关于界面的开源库介绍的View
 */
public class AboutDependencyView extends LinearLayout implements View.OnClickListener {

    @Bind(R.id.about_dependency_name_author)
    TextView nameAndAuthorTx;
    @Bind(R.id.about_dependency_address)
    TextView addressTx;
    @Bind(R.id.about_dependency_license)
    TextView licenseTx;

    private String name;
    private String author;
    private String address;
    private int licenseId;


    public AboutDependencyView(Context context) {
        super(context);
        initViews(context);
    }

    public AboutDependencyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initViews(context);
    }

    public AboutDependencyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        initViews(context);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.AboutDependencyView);
        name = ta.getString(R.styleable.AboutDependencyView_name);
        author = ta.getString(R.styleable.AboutDependencyView_author);
        address = ta.getString(R.styleable.AboutDependencyView_address);
        licenseId = ta.getInt(R.styleable.AboutDependencyView_license, 0);
        ta.recycle();
    }

    private void initViews(Context context) {
        inflate(context, R.layout.item_about_dependency, this);
        ButterKnife.bind(this);

        setOrientation(VERTICAL);
        setBackgroundResource(R.drawable.about_license_bg);

        nameAndAuthorTx.setText(name + " - " + author);
        addressTx.setText(address);
        licenseTx.setText(License.getLicense(licenseId));
        setClickable(true);
        setOnClickListener(this);
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
        params.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.about_dependency_block_margin_bottom);
        setLayoutParams(params);

        int paddingSize = getResources().getDimensionPixelSize(R.dimen.about_dependency_block_padding);
        setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(address));
        v.getContext().startActivity(Intent.createChooser(intent, null));
    }

    private enum License {
        APACHE_2("Apache License 2.0"),
        GPL_2("GNU General Public License v2.0"),
        GPL_3("GNU General Public License v3.0"),
        MIT("The MIT License"),
        NO_LICENSE("The Unlicense"),
        BSD_2("BSD 2-clause \"Simplified\" License"),
        BSD_3("BSD 3-clause \"New\" or \"Revised\" License"),
        BSD("BSD License");

        License(String licenseContent) {
            this.licenseContent = licenseContent;
        }

        private static String getLicense(int id) {
            switch (id) {
                case 0:
                    return APACHE_2.getLicenseContent();

                case 1:
                    return GPL_2.getLicenseContent();

                case 2:
                    return GPL_3.getLicenseContent();

                case 3:
                    return MIT.getLicenseContent();

                case 4:
                    return NO_LICENSE.getLicenseContent();

                case 5:
                    return BSD_2.getLicenseContent();

                case 6:
                    return BSD_3.getLicenseContent();

                case 7:
                    return BSD.getLicenseContent();

                default:
                    return NO_LICENSE.getLicenseContent();
            }
        }

        private String licenseContent;

        public String getLicenseContent() {
            return licenseContent;
        }
    }
}
