package com.example.bluetooth.ui.device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetooth.LogUtil;
import com.example.bluetooth.MyApplication;
import com.example.bluetooth.adapters.BtAdapter;
import com.example.bluetooth.databinding.FragmentDeviceBinding;
import com.example.bluetooth.receiver.BtReceiver;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DeviceFragment extends Fragment {

    private static final String TAG = "DeviceFragment";

    private DeviceViewModel deviceViewModel;
    private BtReceiver btReceiver;
    private FragmentDeviceBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d("DeviceFragment", "onCreateView");
        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        binding = FragmentDeviceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        BluetoothManager bluetoothManager = (BluetoothManager) MyApplication.getContext().getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
        List<BluetoothDevice> deviceList = new ArrayList<>();

        //向recyclerView中添加内容
        RecyclerView recyclerView = binding.deviceRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        //获取已配对设备并显示在RecyclerView上
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        pairedDevices.getClass();
        if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    LogUtil.i(TAG, "Device:" + device.getName());
                    try {
                        deviceList.add(device);
                    }
                    catch (NullPointerException e) {
                        LogUtil.e(TAG, "NullPointerException");
                    }
                }
            }
        BtAdapter btAdapter = BtAdapter.getBtAdapter();
        btAdapter.setDeviceList(deviceList);
        recyclerView.setAdapter(btAdapter);

        //注册广播接收蓝牙发现的结果
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //蓝牙开始搜索
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //蓝牙搜索结束
        intentFilter.addAction(android.bluetooth.BluetoothDevice.ACTION_FOUND); //发现未配对设备
        btReceiver = new BtReceiver();
        MyApplication.getContext().registerReceiver(btReceiver, intentFilter);

        //为imageButton设置监听事件
        ImageButton imageButton = binding.imageButton;
        imageButton.setOnClickListener(view -> {
            //蓝牙扫描，只扫描传统蓝牙
            if (bluetoothAdapter.isDiscovering()) {
                LogUtil.d(TAG, "is discovering");
                bluetoothAdapter.cancelDiscovery();
            }
            LogUtil.d(TAG, "not discovering");
            if (bluetoothAdapter.startDiscovery()) {
                LogUtil.d(TAG, "process start");
            }
            else {
                LogUtil.d(TAG, "process not start");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        MyApplication.getContext().unregisterReceiver(btReceiver); //注销广播接收器
        LogUtil.d(TAG, "onDestroyView");
    }
}
