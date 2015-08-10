package com.linroid.pushapp.ui.device;

import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import android.widget.TextView;

import com.linroid.pushapp.R;
import com.linroid.pushapp.model.Device;
import com.linroid.pushapp.ui.base.DataAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        Resources resources = holder.aliasTV.getResources();

        Device device = data.get(i);
        holder.aliasTV.setText(device.getAlias());
        String screenText =resources.getString(R.string.txt_device_screen,
                                                    device.getWidth(),
                                                    device.getHeight(),
                                                    device.getDpi());
        String modelText = resources.getString(R.string.txt_device_model, device.getModel());
        String systemText = resources.getString(R.string.txt_device_system, device.getOsName(), device.getSdkLevel());
        String cpuTypeText = resources.getString(R.string.txt_device_cpu_type, device.getCpuType());
        String memorySize = resources.getString(R.string.txt_device_memory_size,
                                            Formatter.formatFileSize(holder.aliasTV.getContext(), device.getMemorySize()));

        holder.modelTV.setText(modelText);
        holder.systemTV.setText(systemText);
        holder.cpuTypeTV.setText(cpuTypeText);
        holder.screenTV.setText(screenText);
        holder.memorySizeTV.setText(memorySize);
    }

    class DeviceHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.alias_tv)
        TextView aliasTV;
        @Bind(R.id.model_tv)
        TextView modelTV;
        @Bind(R.id.system_tv)
        TextView systemTV;
        @Bind(R.id.cpu_type_tv)
        TextView cpuTypeTV;
        @Bind(R.id.screen_tv)
        TextView screenTV;
        @Bind(R.id.memory_size_tv)
        TextView memorySizeTV;

        public DeviceHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
