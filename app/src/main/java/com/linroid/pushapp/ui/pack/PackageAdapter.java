package com.linroid.pushapp.ui.pack;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.linroid.pushapp.R;
import com.linroid.pushapp.model.Pack;
import com.linroid.pushapp.ui.base.DataAdapter;
import com.linroid.pushapp.util.AndroidUtil;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

/**
 * Created by linroid on 7/20/15.
 */
public class PackageAdapter extends DataAdapter<Pack, PackageAdapter.PackageHolder> {
    Picasso picasso;
    public PackageAdapter(Picasso picasso) {
        this.picasso = picasso;
    }

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
        Pack pack = data.get(i);
        //本地文件存在则从文件中加载图片，否则从网络中加载
        if (pack.fileExists()) {
            picasso.load(pack.getIconUrl()).centerInside().into(holder.iconIv);
        } else {
            Drawable icon = AndroidUtil.getApkIcon(holder.iconIv.getContext(), pack.getPath());
            holder.iconIv.setImageDrawable(icon);
        }
        holder.nameTv.setText(pack.getAppName());
        holder.versionTv.setText("v" + pack.getVersionName() + "(" + pack.getVersionCode() + ")");
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

        public PackageHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            actionBtn.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                @DebugLog
                public boolean onPreDraw() {
                    actionBtn.getViewTreeObserver().removeOnPreDrawListener(this);
                    Rect bounds = new Rect();
                    actionBtn.getHitRect(bounds);
                    int val = AndroidUtil.dpToPx(128);
                    bounds.top += val;
                    bounds.right += val;
                    bounds.bottom += val;
                    bounds.left += val;
                    TouchDelegate delegate = new TouchDelegate(bounds, actionBtn);
                    ((View) actionBtn.getParent()).setTouchDelegate(delegate);
                    return true;
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
