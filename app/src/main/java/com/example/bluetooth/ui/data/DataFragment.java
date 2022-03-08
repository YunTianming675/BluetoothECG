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
import com.example.bluetooth.RT_PACK;
import com.example.bluetooth.adapters.BtAdapter;
import com.example.bluetooth.databinding.FragmentDataBinding;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DataFragment extends Fragment {

    private final static String TAG = "DataFragment";
    private boolean isRead = false;
    private int lastXValue = 0;
    private int circleNum = 0;

    private DataViewModel dataViewModel;
    private FragmentDataBinding binding;
    private GraphView graphView;
    private AppCompatButton buttonStart;
    private AppCompatButton buttonEnd;
    private BluetoothSocket bluetoothSocket;
    private final LineGraphSeries<DataPoint> series = new LineGraphSeries<>();

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

        graphView.addSeries(series);

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

                while (isRead) {
                    RT_PACK rt_pack = new RT_PACK();
                    try {
                        rt_pack.receiveData(new DataInputStream(bluetoothSocket.getInputStream()));
                    }
                    catch (IOException e) {
                        LogUtil.e(TAG, "bluetoothSocket IOException");
                        e.printStackTrace();
                    }
                    LogUtil.d(TAG, "HeartData:");
                    logPrintArray(rt_pack.getHeartData());
                    LogUtil.d(TAG, "HeartRate:" + Integer.toHexString(rt_pack.getHeartRate()));
                    LogUtil.d(TAG, "spo2:" + Integer.toHexString(rt_pack.getSpo2()));
                    LogUtil.d(TAG, "bk:" + Integer.toHexString(rt_pack.getBk()));
                    LogUtil.d(TAG, "rsv:");
                    logPrintArray(rt_pack.getRsv());

                    // 回到主线程更新UI
                    // TODO 实机测试
                    graphView.post(() -> {
                        byte[] bytes = rt_pack.getHeartData();
                        for (byte b:bytes) {
                            series.appendData(new DataPoint(lastXValue, b), true, 256);
                            lastXValue ++;
                            graphView.onDataChanged(true, true);
                        }
                        circleNum ++;
                        if (circleNum == 3) {
                            circleNum = 0;
                            lastXValue = 0;
                            series.resetData(new DataPoint[]{new DataPoint(lastXValue, bytes[0])});
                        }
                    });
                }
            });
            thread.start();
        });

        buttonEnd.setOnClickListener(view -> {
            LogUtil.d(TAG, "on frag_button_end Listener");
            buttonStart.setEnabled(true);
            isRead = false;

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

    public void logPrintArray(char[] ch) {
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

    public void logPrintArray(byte[] bytes) {
        StringBuilder res = new StringBuilder();
        for (byte b:bytes) {
            String hex = Integer.toHexString(b);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            res.append(hex);
            res.append(" ");
        }
        LogUtil.d(TAG, res.toString());
    }
}
