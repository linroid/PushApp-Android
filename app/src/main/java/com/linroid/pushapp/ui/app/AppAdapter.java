package com.linroid.pushapp.ui.app;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linroid.pushapp.R;
import com.linroid.pushapp.ui.base.DataAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by linroid on 7/20/15.
 */
public class AppAdapter extends DataAdapter<ApplicationInfo, AppAdapter.AppHolder> {

    @Override
    public AppHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_app, viewGroup, false);
        return new AppHolder(view);
    }

    @Override
    public void onBindViewHolder(AppHolder holder, int i) {
        ApplicationInfo info = data.get(i);
        PackageManager pm = holder.appIconIV.getContext().getPackageManager();
        Drawable icon = pm.getApplicationIcon(info);
        holder.appNameTV.setText(pm.getApplicationLabel(info));
        holder.appIconIV.setImageDrawable(icon);
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

        @Override
        public void onClick(View v) {
        }
    }
}
