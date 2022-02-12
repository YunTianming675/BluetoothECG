package com.example.bluetooth.adapters;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetooth.LogUtil;
import com.example.bluetooth.R;

import java.util.List;

public class BtAdapter extends RecyclerView.Adapter<BtAdapter.DeviceViewHolder>{

    private static final String TAG = "BtAdapter";
    private List<BluetoothDevice> deviceList;

    public BtAdapter(List<BluetoothDevice> deviceList) {
        this.deviceList = deviceList;
        if (deviceList == null)
            LogUtil.e(TAG, "deviceList == null");
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_item, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        BluetoothDevice bluetoothDevice = deviceList.get(position);
        holder.getDevice_icon().setImageResource(R.drawable.bluetooth); // TODO 修改相应的资源图标
        holder.getDevice_name().setText(bluetoothDevice.getName());
    }

    @Override
    public int getItemCount() {
        if (deviceList == null)
            return 0;
        else
            return deviceList.size();
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {

        private final ImageView device_icon;
        private final TextView device_name;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            device_icon = itemView.findViewById(R.id.device_icon);
            device_name = itemView.findViewById(R.id.device_name);
        }

        public ImageView getDevice_icon() {
            return device_icon;
        }

        public TextView getDevice_name() {
            return device_name;
        }
    }
}
