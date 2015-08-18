package com.linroid.pushapp.ui.push;

import java.util.List;

/**
 * Created by linroid on 8/19/15.
 */
public interface OnSelectActionListener {

    void onSelectAll();

    void onSelectNone();

    List<Integer> provideSelectedDeviceIds();

    int provideSelectedCount();
}
