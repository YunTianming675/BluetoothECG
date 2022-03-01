package com.example.bluetooth.adapters;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetooth.LogUtil;
import com.example.bluetooth.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BtAdapter extends RecyclerView.Adapter<BtAdapter.DeviceViewHolder>{

    static final UUID SPP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    protected List<BluetoothDevice> deviceList = new ArrayList<>();

    private static final String TAG = "BtAdapter";
    private final static BtAdapter btAdapter = new BtAdapter();
    private BluetoothSocket bluetoothSocket = null;
    private BtAdapter() {}

    public static BtAdapter getBtAdapter() {
        return btAdapter;
    }

    public BluetoothSocket getBluetoothSocket() {
        return bluetoothSocket;
    }

    public void addBluetoothDevice(BluetoothDevice bluetoothDevice) {
        if (deviceList.contains(bluetoothDevice))
            return;
        deviceList.add(bluetoothDevice);
        notifyDataSetChanged();
    }

    public void setDeviceList(List<BluetoothDevice> deviceList) {
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
        holder.getDevice_icon().setImageResource(R.drawable.bluetooth); // TODO 修改相应的资源图标
        holder.getDevice_name().setText(bluetoothDevice.getName());
        holder.getDevice_address().setText(bluetoothDevice.getAddress());
    }

    @Override
    public int getItemCount() {
        if (deviceList == null)
            return 0;
        else
            return deviceList.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder {

        private final ImageView device_icon;
        private final TextView device_name;
        private final TextView device_address;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            device_icon = itemView.findViewById(R.id.device_icon);
            device_name = itemView.findViewById(R.id.device_name);
            device_address = itemView.findViewById(R.id.device_address);

            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                LogUtil.d(TAG, "position = " + position);
                onItemClick(position);
            });
        }

        public ImageView getDevice_icon() {
            return device_icon;
        }

        public TextView getDevice_name() {
            return device_name;
        }

        public TextView getDevice_address() {
            return device_address;
        }

        private void onItemClick(int position) {
            BluetoothDevice bluetoothDevice = deviceList.get(position);

            LogUtil.d(TAG, "device name = " + bluetoothDevice.getName());
            LogUtil.d(TAG, "device address = " + bluetoothDevice.getAddress());

            try {
                bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(SPP_UUID);
                new Thread( () -> {
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    try {
                        LogUtil.d(TAG, "try begin");
                        bluetoothSocket.connect();
                        LogUtil.d(TAG, "try end");
                    }
                    catch (IOException e) {
                        LogUtil.e(TAG, "Could not connect");
                        e.printStackTrace();
                        try {
                            bluetoothSocket.close();
                        }
                        catch (IOException exception) {
                            LogUtil.e(TAG, "Could not close");
                        }
                    }
                    LogUtil.d(TAG, "connect status:" + bluetoothSocket.isConnected());
                }).start();
            }
            catch (IOException ioException) {
                LogUtil.e(TAG, "IOException");
            }
            LogUtil.d(TAG, "is connected:" + bluetoothSocket.isConnected());
        }
    }
}
