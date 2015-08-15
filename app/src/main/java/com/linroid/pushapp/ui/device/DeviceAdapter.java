package com.linroid.pushapp.ui.device;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.linroid.pushapp.R;
import com.linroid.pushapp.model.Device;
import com.linroid.pushapp.ui.base.DataAdapter;
import com.linroid.pushapp.ui.push.PushActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by linroid on 7/20/15.
 */
public class DeviceAdapter extends DataAdapter<Device, DeviceAdapter.DeviceHolder> implements PushActivity.OnSelectListener {
    SparseArray<Boolean> deviceSelectStatus;
    boolean selectMode = false;

    public DeviceAdapter(Activity activity) {
        deviceSelectStatus = new SparseArray<>();
        if (activity instanceof PushActivity) {
            ((PushActivity) activity).setSelectListener(this);
            selectMode = true;
        }
    }

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
        String aliasText = device.getAlias();
        String screenText = resources.getString(R.string.txt_device_screen,
                device.getWidth(),
                device.getHeight(),
                device.getDpi());
        String modelText = resources.getString(R.string.txt_device_model, device.getModel());
        String systemText = resources.getString(R.string.txt_device_system, device.getOsName(), device.getSdkLevel());
        String cpuTypeText = resources.getString(R.string.txt_device_cpu_type, device.getCpuType());
        String memorySize = resources.getString(R.string.txt_device_memory_size,
                Formatter.formatFileSize(holder.aliasTV.getContext(), device.getMemorySize()));

        holder.aliasTV.setText(aliasText);
        holder.modelTV.setText(modelText);
        holder.systemTV.setText(systemText);
        holder.cpuTypeTV.setText(cpuTypeText);
        holder.screenTV.setText(screenText);
        holder.memorySizeTV.setText(memorySize);

        if (selectMode) {
            holder.checkBox.setVisibility(View.VISIBLE);
            Boolean selected = deviceSelectStatus.get(device.getId());
            if (selected != null && selected) {
                holder.checkBox.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
            }
        }
    }

    @Override
    public void onSelectAll() {
        for (Device device: data) {
            deviceSelectStatus.put(device.getId(), true);
        }
        notifyDataSetChanged();
    }

    @Override
    public void onSelectNone() {
        for (Device device: data) {
            deviceSelectStatus.put(device.getId(), false);
        }
        notifyDataSetChanged();
    }

    @Override
    public List<Integer> onObtainSelectedDeviceIds() {
        List<Integer> selectedDeviceIds = new ArrayList<>();
        for (int i = 0; i < deviceSelectStatus.size(); i++) {
            int key = deviceSelectStatus.keyAt(i);
            Boolean isChecked = deviceSelectStatus.get(key);
            if(isChecked!=null && isChecked) {
                selectedDeviceIds.add(key);
            }
        }
        return selectedDeviceIds;
    }

    class DeviceHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

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
        @Bind(android.R.id.checkbox)
        CheckBox checkBox;

        public DeviceHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            checkBox.setOnCheckedChangeListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            int position = getAdapterPosition();
            Device device = data.get(position);
            deviceSelectStatus.put(device.getId(), isChecked);
        }

        @Override
        public void onClick(View v) {
            checkBox.setChecked(!checkBox.isChecked());
        }
    }
}
