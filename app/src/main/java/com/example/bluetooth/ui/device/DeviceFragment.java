package com.example.bluetooth.ui.device;

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

import com.example.bluetooth.BluetoothDevice;
import com.example.bluetooth.MyApplication;
import com.example.bluetooth.R;
import com.example.bluetooth.adapters.DeviceAdapter;
import com.example.bluetooth.databinding.FragmentDeviceBinding;

import java.util.Arrays;
import java.util.List;

public class DeviceFragment extends Fragment {

    private DeviceViewModel deviceViewModel;
    private FragmentDeviceBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        binding = FragmentDeviceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.deviceRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        List<BluetoothDevice> deviceList = Arrays.asList(
                new BluetoothDevice("device 01", R.drawable.bluetooth),
                new BluetoothDevice("device 02", R.drawable.bluetooth),
                new BluetoothDevice("device 03", R.drawable.bluetooth)
        );
        DeviceAdapter deviceAdapter = new DeviceAdapter(deviceList);
        recyclerView.setAdapter(deviceAdapter);

        ImageButton imageButton = binding.imageButton;

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
