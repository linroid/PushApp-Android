package com.linroid.pushapp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
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
public class AboutDependencyView extends LinearLayout {

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
        int paddingSize = getResources().getDimensionPixelSize(R.dimen.about_dependency_block_padding);
        setPadding(paddingSize, paddingSize, paddingSize, paddingSize);
        setBackgroundResource(R.drawable.bg_card_nopic);

        nameAndAuthorTx.setText(name + " - " + author);
        addressTx.setText(address);
        licenseTx.setText(License.getLicense(licenseId));
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        LayoutParams params = (LinearLayout.LayoutParams) getLayoutParams();
        params.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.about_dependency_block_margin_bottom);
        setLayoutParams(params);
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
