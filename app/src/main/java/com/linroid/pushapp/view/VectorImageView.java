package com.linroid.pushapp.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.graphics.drawable.VectorDrawableCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.linroid.pushapp.R;
import com.telly.mrvector.MrVector;

/**
 * Created by linroid on 8/22/15.
 */
public class VectorImageView extends ImageView {
    public VectorImageView(Context context) {
        super(context);
    }

    public VectorImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttr(context, attrs);
    }

    public VectorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public VectorImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        final TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.VectorImageView);
        int vectorResId = ta.getResourceId(R.styleable.VectorImageView_src, 0);
//        VectorDrawableCompat drawable = VectorDrawableCompat.createFromResource(context.getResources(), vectorResId);
        Drawable drawable = MrVector.inflate(context.getResources(), vectorResId);
        setImageDrawable(drawable);
        ta.recycle();
    }

}
