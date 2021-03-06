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

package com.linroid.pushapp.ui.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.linroid.pushapp.R;
import com.linroid.pushapp.ui.base.DataAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by linroid on 7/20/15.
 */
public class AppAdapter extends DataAdapter<ApplicationInfo, AppAdapter.AppHolder> {
    OnActionListener listener;
    PackageManager packageManager;
    boolean onScroll = false;

    public AppAdapter(Context context) {
        packageManager = context.getPackageManager();

    }

    public void setOnScroll(boolean onScroll) {
        this.onScroll = onScroll;
    }

    public void setListener(OnActionListener listener) {
        this.listener = listener;
    }

    @Override
    public AppHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_app, viewGroup, false);
        return new AppHolder(view);
    }

    @Override
    public void onBindViewHolder(AppHolder holder, int i) {
        ApplicationInfo info = data.get(i);
        holder.appNameTV.setText(packageManager.getApplicationLabel(info));
        if (onScroll) {
            holder.appIconIV.setImageDrawable(null);
        } else {
            Drawable icon = packageManager.getApplicationIcon(info);
            holder.appIconIV.setImageDrawable(icon);
        }
    }

    class AppHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.app_icon)
        ImageView appIconIV;
        @Bind(R.id.app_name)
        TextView appNameTV;

        public AppHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void showImage(int i) {
            Drawable icon = packageManager.getApplicationIcon(data.get(i));
            // todo 还能优化一下，如果滑动少的时候不要屏幕所见的都刷（只刷新出来的），顺便加动画
            appIconIV.setImageDrawable(icon);
        }

        @Override
        public void onClick(View v) {
            final PopupMenu popupMenu = new PopupMenu(v.getContext(), appNameTV);
            Menu menu = popupMenu.getMenu();
            popupMenu.getMenuInflater().inflate(R.menu.item_app, menu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (listener != null && item.getItemId()==R.id.action_send_app) {
                        ApplicationInfo info = data.get(getAdapterPosition());
                        listener.onSend(info);
                        return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    }
    public interface OnActionListener {
        void onSend(ApplicationInfo info);
    }
}
