package com.example.bluetooth.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetooth.BluetoothDevice;
import com.example.bluetooth.R;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>{

    private List<BluetoothDevice> deviceList;

    public DeviceAdapter(List<BluetoothDevice> deviceList) {
        this.deviceList = deviceList;
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
        holder.getDevice_icon().setImageResource(bluetoothDevice.getIcon());
        holder.getDevice_name().setText(bluetoothDevice.getName());
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {

        private ImageView device_icon;
        private TextView device_name;

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
