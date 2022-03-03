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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DataFragment extends Fragment {

    private final static String TAG = "DataFragment";
    private boolean isRead = false;

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
        graphView = binding.fragDataGraphview;
        buttonStart = binding.fragButtonStart;
        buttonEnd = binding.fragButtonEnd;

        buttonStart.setOnClickListener(view -> {
            LogUtil.d(TAG, "on frag_button_start Listener");
            buttonStart.setEnabled(false);
            bluetoothSocket = BtAdapter.getBtAdapter().getBluetoothSocket();
            isRead = true;

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

                char[] c = new char[78];
                boolean startFlag = false;

                while (isRead) {
                    try {
                        DataInputStream inputStream = new DataInputStream(bluetoothSocket.getInputStream());
                        if (inputStream.readChar() == 0xFF) {
                            startFlag = true;
                        }
                        for (int i = 0; i < c.length && startFlag; i++) {
                            c[i] = inputStream.readChar();
                        }
                        startFlag = false;
                        logPrintChar(c);
                        
                    }
                    catch (IOException e) {
                        LogUtil.e(TAG, "Thread IOException error");
                        e.printStackTrace();
                    }
                }
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

    public void logPrintChar(char[] ch) {
        StringBuilder res = new StringBuilder();
        for (char c:ch) {
            String hex = Integer.toHexString(c);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            res.append(hex);
            res.append(" ");
        }
        LogUtil.d(TAG, res.toString());
    }
}
