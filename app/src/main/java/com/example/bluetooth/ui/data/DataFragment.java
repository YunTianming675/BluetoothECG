package com.example.bluetooth.ui.data;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.bluetooth.LogUtil;
import com.example.bluetooth.adapters.BtAdapter;
import com.example.bluetooth.databinding.FragmentDataBinding;
import com.jjoe64.graphview.GraphView;

import java.io.DataOutputStream;
import java.io.IOException;

public class DataFragment extends Fragment {

    private final static String TAG = "DataFragment";

    private DataViewModel dataViewModel;
    private FragmentDataBinding binding;
    private GraphView graphView;
    private AppCompatButton buttonStart;
    private AppCompatButton buttonEnd;
    private BluetoothSocket bluetoothSocket;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        binding = FragmentDataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        init();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void init() {
        boolean isRead = false;

        graphView = binding.fragDataGraphview;
        buttonStart = binding.fragButtonStart;
        buttonEnd = binding.fragButtonEnd;

        buttonStart.setOnClickListener(view -> {
            LogUtil.d(TAG, "on frag_button_start Listener");
            buttonStart.setEnabled(false);
            bluetoothSocket = BtAdapter.getBtAdapter().getBluetoothSocket();

            try {
                DataOutputStream outputStream = new DataOutputStream(bluetoothSocket.getOutputStream());
                outputStream.writeByte(0x8A);
                LogUtil.d(TAG, "has been sent 0x8A");
            }
            catch (IOException e) {
                LogUtil.e(TAG, "IOException:");
                e.printStackTrace();
            }

            // 在子线程中接收数据
            Thread thread = new Thread(() -> {
                LogUtil.d(TAG, "on Thread");
//                while (isRead) {
//                }
            });
            thread.start();
        });

        buttonEnd.setOnClickListener(view -> {
            LogUtil.d(TAG, "on frag_button_end Listener");
            buttonStart.setEnabled(true);

            try {
                DataOutputStream outputStream = new DataOutputStream(bluetoothSocket.getOutputStream());
                outputStream.writeByte(0x88);
                LogUtil.d(TAG, "has been sent 0x88");
            }
            catch (IOException e) {
                LogUtil.e(TAG, "IOException");
                e.printStackTrace();
            }
        });
    }
}
