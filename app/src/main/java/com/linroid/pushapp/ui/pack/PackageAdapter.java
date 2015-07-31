package com.linroid.pushapp.ui.pack;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.linroid.pushapp.R;
import com.linroid.pushapp.model.InstallPackage;
import com.linroid.pushapp.ui.base.DataAdapter;
import com.linroid.pushapp.util.AndroidUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by linroid on 7/20/15.
 */
public class PackageAdapter extends DataAdapter<InstallPackage, PackageAdapter.PackageHolder> {

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public PackageHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_package, viewGroup, false);
        return new PackageHolder(view);
    }

    @Override
    public void onBindViewHolder(PackageHolder holder, int i) {
        InstallPackage pack = data.get(i);
//        Drawable icon = AndroidUtil.getApkIcon(holder.iconIv.getContext(), pack.getPath());
        Drawable icon = holder.iconIv.getContext().getResources().getDrawable(R.mipmap.ic_launcher);
        holder.iconIv.setImageDrawable(icon);
        holder.nameTv.setText(pack.getAppName());
        holder.versionTv.setText("v"+pack.getVersionName()+"("+pack.getVersionCode()+")");
        holder.packageTv.setText(pack.getPackageName());
        holder.timeTv.setText(pack.getFriendlyTime());
    }


    class PackageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.icon_iv)
        ImageView iconIv;
        @Bind(R.id.name_tv)
        TextView nameTv;
        @Bind(R.id.package_tv)
        TextView packageTv;
        @Bind(R.id.time_tv)
        TextView timeTv;
        @Bind(R.id.version_tv)
        TextView versionTv;
        @Bind(R.id.action_btn)
        ImageButton actionBtn;
        @Bind(R.id.push_card)
        CardView pushCard;

        public PackageHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
