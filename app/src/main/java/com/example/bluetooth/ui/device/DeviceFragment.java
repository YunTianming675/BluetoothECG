package com.example.bluetooth.ui.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bluetooth.databinding.FragmentDeviceBinding;

public class DeviceFragment extends Fragment {

    private DeviceViewModel deviceViewModel;
    private FragmentDeviceBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        binding = FragmentDeviceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.fragDeviceText1; //fragDeviceText1是fragment_device.xml的TextView的id
        deviceViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
