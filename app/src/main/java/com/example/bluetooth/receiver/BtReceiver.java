package com.example.bluetooth.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.bluetooth.LogUtil;
import com.example.bluetooth.adapters.BtAdapter;

public class BtReceiver extends BroadcastReceiver {

    private final static String TAG = "BluetoothReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d("BluetoothReceiver", "---------------receive broadcast---------------");
        String action = intent.getAction();
        LogUtil.d(TAG, "action = " + action);
        BtAdapter btAdapter = BtAdapter.getBtAdapter();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        if (device != null)
            LogUtil.i(TAG, "name:" + device.getName() + " address:" + device.getAddress());
        switch (action) {
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                LogUtil.i(TAG, "start found");
                break;
            case BluetoothDevice.ACTION_FOUND:
                btAdapter.addBluetoothDevice(device);
                short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MAX_VALUE);
                LogUtil.i(TAG, "EXTRA_RSSI=" + rssi);
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                LogUtil.i(TAG, "finish found");
                break;
            default:
                break;
        }
    }
}
