package com.linroid.pushapp.ui.device;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.linroid.pushapp.model.Device;
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
public class DeviceAdapter extends DataAdapter<Device, DeviceAdapter.DeviceHolder> {

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_device, viewGroup, false);
        return new DeviceHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int i) {
        Device device = data.get(i);
        holder.aliasTV.setText(device.getAlias());
    }

    class DeviceHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.device_alias)
        TextView aliasTV;
        public DeviceHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
