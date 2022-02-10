package com.example.bluetooth.ui.device;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bluetooth.BluetoothDevice;
import com.example.bluetooth.LogUtil;
import com.example.bluetooth.MyApplication;
import com.example.bluetooth.R;
import com.example.bluetooth.adapters.DeviceAdapter;
import com.example.bluetooth.databinding.FragmentDeviceBinding;
import com.example.bluetooth.receiver.BluetoothReceiver;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DeviceFragment extends Fragment {

    private DeviceViewModel deviceViewModel;
    private BluetoothReceiver bluetoothReceiver;
    private FragmentDeviceBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.d("DeviceFragment", "onCreateView");
        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        binding = FragmentDeviceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //向recyclerView中添加内容
        RecyclerView recyclerView = binding.deviceRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        List<BluetoothDevice> deviceList = Arrays.asList(
                new BluetoothDevice("device 01", R.drawable.bluetooth),
                new BluetoothDevice("device 02", R.drawable.bluetooth),
                new BluetoothDevice("device 03", R.drawable.bluetooth)
        );
        DeviceAdapter deviceAdapter = new DeviceAdapter(deviceList);
        recyclerView.setAdapter(deviceAdapter);

        //注册广播接收蓝牙发现的结果
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED); //蓝牙开始搜索
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED); //蓝牙搜索结束
        intentFilter.addAction(android.bluetooth.BluetoothDevice.ACTION_FOUND); //发现未配对设备
        bluetoothReceiver = new BluetoothReceiver();
        MyApplication.getContext().registerReceiver(bluetoothReceiver, intentFilter);

        //为imageButton设置监听事件
        ImageButton imageButton = binding.imageButton;
        imageButton.setOnClickListener(view -> {
            Toast.makeText(MyApplication.getContext(), "事件：点击", Toast.LENGTH_SHORT).show();
            BluetoothManager bluetoothManager = (BluetoothManager) MyApplication.getContext().getSystemService(Context.BLUETOOTH_SERVICE);
            BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

            //获得已配对蓝牙设备
            Set<android.bluetooth.BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            if (pairedDevices.size() > 0) {
                for (android.bluetooth.BluetoothDevice device : pairedDevices) {
                    String deviceName = device.getName();
                    String deviceAddress = device.getAddress();
                    LogUtil.d("DeviceFragment", "deviceName:" + deviceName);
                    LogUtil.d("DeviceFragment", "deviceAddress" + deviceAddress);
                }
            }

            //蓝牙扫描
            if (bluetoothAdapter.isDiscovering()) {
                LogUtil.d("DeviceFragment", "is discovering");
                bluetoothAdapter.cancelDiscovery();
            }
            LogUtil.d("DeviceFragment", "not discovering");
            if (bluetoothAdapter.startDiscovery()) {
                LogUtil.d("DeviceFragment", "process start");
            }
            else {
                LogUtil.d("DeviceFragment", "process not start");
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        MyApplication.getContext().unregisterReceiver(bluetoothReceiver); //注销广播接收器
        LogUtil.d("DeviceFragment", "onDestroyView");
    }
}
