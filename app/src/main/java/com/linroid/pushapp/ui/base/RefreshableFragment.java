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

package com.linroid.pushapp.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linroid.pushapp.R;
import com.linroid.pushapp.util.SimpleItemDecoration;
import com.linroid.pushapp.view.ContentLoaderView;

/**
 * Created by linroid on 7/20/15.
 */
public abstract class RefreshableFragment extends Fragment
        implements ContentLoaderView.OnRefreshListener, ContentLoaderView.OnMoreListener {
    protected RecyclerView recyclerView;
    protected ContentLoaderView loaderView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(provideViewResourceId(), container, false);

        loaderView = (ContentLoaderView) view.findViewById(R.id.content_loader);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_divider_space)));
        loaderView.setAdapter(getAdapter());
        loaderView.setOnRefreshListener(this);
        loaderView.setMoreListener(this);
        return view;
    }

    protected int provideViewResourceId() {
        return R.layout.fragment_refreshable;
    }

    public abstract RecyclerView.Adapter<? extends RecyclerView.ViewHolder> getAdapter();

    @Override
    public void onRefresh(boolean fromSwipe) {
        loadData(1);
    }

    @Override
    public void onMore(int page) {
        loadData(page);
    }

    public abstract void loadData(int page);

    /**
     * 强制刷新
     */
    protected void forceRefresh() {
        onRefresh(false);
    }
}
