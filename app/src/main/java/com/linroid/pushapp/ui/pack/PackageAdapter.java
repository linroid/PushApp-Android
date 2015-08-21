package com.linroid.pushapp.ui.pack;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.linroid.pushapp.R;
import com.linroid.pushapp.model.Pack;
import com.linroid.pushapp.ui.base.DataAdapter;
import com.linroid.pushapp.util.AndroidUtil;
import com.linroid.pushapp.util.MD5;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

/**
 * Created by linroid on 7/20/15.
 */
public class PackageAdapter extends DataAdapter<Pack, PackageAdapter.PackageHolder> {
    private Picasso picasso;
    private OnActionListener listener;

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
        final Pack pack = data.get(i);
        //本地文件存在则从文件中加载图片，否则从网络中加载
        if (pack.fileExists()) {
            Drawable icon = AndroidUtil.getApkIcon(holder.iconIv.getContext(), pack.getPath());
            holder.iconIv.setImageDrawable(icon);
        } else {
            picasso.load(pack.getIconUrl()).into(holder.iconIv);
        }
        holder.nameTv.setText(pack.getAppName());
        holder.versionTv.setText("v" + pack.getVersionName() + "(" + pack.getVersionCode() + ")");
        holder.packageTv.setText(pack.getPackageName());
        holder.timeTv.setText(pack.getFriendlyTime());
        holder.pack = pack;
    }

    public void setListener(OnActionListener listener) {
        this.listener = listener;
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
        public Pack pack;

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
                    int val = AndroidUtil.dpToPx(8);
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
            actionBtn.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            final PopupMenu popupMenu = new PopupMenu(v.getContext(), actionBtn);
            Menu menu = popupMenu.getMenu();
            popupMenu.getMenuInflater().inflate(R.menu.item_pack, menu);
            if (pack.fileExists()) {
                menu.findItem(R.id.action_download).setVisible(false);
            }
            if (AndroidUtil.isInstalled(v.getContext(), pack.getPackageName())
                    && !TextUtils.isEmpty(pack.getMD5())
                    && MD5.checkMD5(pack.getMD5(), AndroidUtil.appPath(v.getContext(), pack.getPackageName()))) {
                menu.findItem(R.id.action_install).setVisible(false);
            } else {
                if (!pack.fileExists()) {
                    menu.findItem(R.id.action_install).setVisible(false);
                }
                menu.findItem(R.id.action_app_info).setVisible(false);
                menu.findItem(R.id.action_open).setVisible(false);
                menu.findItem(R.id.action_uninstall).setVisible(false);
            }
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    if (listener != null) {
                        switch (item.getItemId()) {
                            case R.id.action_open:
                                listener.onOpen(pack);
                                break;
                            case R.id.action_install:
                                listener.onInstall(pack);
                                break;
                            case R.id.action_app_info:
                                listener.onAppInfo(pack);
                                break;
                            case R.id.action_uninstall:
                                listener.onUninstall(pack);
                                break;
                            case R.id.action_send_package:
                                listener.onSend(pack);
                                break;
                            case R.id.action_download:
                                listener.onDownload(pack);
                                break;
                        }
                        return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    }

    public interface OnActionListener {
        void onOpen(Pack pack);

        void onInstall(Pack pack);

        void onUninstall(Pack pack);

        void onSend(Pack pack);

        void onDownload(Pack pack);

        void onAppInfo(Pack pack);
    }
}
