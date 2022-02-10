package com.example.bluetooth.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.bluetooth.LogUtil;

public class BtReceiver extends BroadcastReceiver {

    private final static String TAG = "BluetoothReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d("BluetoothReceiver", "---------------receive broadcast---------------");
        String action = intent.getAction();
        LogUtil.d(TAG, "action = " + action);
    }
}
