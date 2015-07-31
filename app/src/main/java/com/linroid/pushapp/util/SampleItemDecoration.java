package com.linroid.pushapp.util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by linroid on 7/31/15.
 */
public class SampleItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SampleItemDecoration(int space) {
        super();
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) != 0) {
            outRect.top = space;
        }
    }
}
