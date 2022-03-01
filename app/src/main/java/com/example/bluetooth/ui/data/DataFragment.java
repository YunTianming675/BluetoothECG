package com.example.bluetooth.ui.data;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bluetooth.LogUtil;
import com.example.bluetooth.adapters.BtAdapter;
import com.example.bluetooth.databinding.FragmentDataBinding;
import com.jjoe64.graphview.GraphView;

public class DataFragment extends Fragment {

    private final static String TAG = "DataFragment";

    private DataViewModel dataViewModel;
    private FragmentDataBinding binding;
    private GraphView graphView;
    private AppCompatButton buttonStart;
    private BluetoothSocket bluetoothSocket;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dataViewModel = new ViewModelProvider(this).get(DataViewModel.class);
        binding = FragmentDataBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();

//        final TextView textView = binding.fragDataText1;
//        graphView = binding.fragDataGraphview;
//        buttonStart = binding.fragButtonStart;

//        dataViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(String s) {
//                textView.setText(s);
//            }
//        });
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

        buttonStart.setOnClickListener(view -> {
            LogUtil.d(TAG, "on frag_button_start Listener");
            bluetoothSocket = BtAdapter.getBtAdapter().getBluetoothSocket();
        });
    }
}
