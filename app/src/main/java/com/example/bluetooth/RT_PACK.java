package com.example.bluetooth;

import java.io.DataInputStream;
import java.io.IOException;

public class RT_PACK {
    private final static String TAG = "RT_PACK";
    private boolean startFlag = false;

    private char dataHead;
    private byte[] heartData = new byte[64];
    private char heartRate;
    private char spo2;
    private char bk;
    private char[] rsv = new char[8];

    public void receiveData(DataInputStream dataInputStream) {
        receiveDataHead(dataInputStream);
        receiveHeartData(dataInputStream);
        receiveHeartRateAndSpo2(dataInputStream);
        receiveBk(dataInputStream);
        receiveRsv(dataInputStream);// TODO 给所有的receive添加移位操作
    }

    private void receiveDataHead(DataInputStream dataInputStream) {
        try {
            dataHead = dataInputStream.readChar();
            dataInputStream.close();
            if ((dataHead & 0xFF00) == 0xFF00) {
                startFlag = true;
                heartData[0] = (byte) (dataHead & 0x00FF);
                LogUtil.d(TAG, "--------------------start--------------------");
            }
        }
        catch (IOException e) {
            LogUtil.e(TAG, "receive dataHead IOException");
            e.printStackTrace();
        }
    }

    private void receiveHeartData(DataInputStream dataInputStream) {
        try {
            for (int i = 1; i < heartData.length; i++) {
                heartData[i] = dataInputStream.readByte();
            }
            dataInputStream.close();
        }
        catch (IOException e) {
            LogUtil.e(TAG, "receive heart data IOException");
            e.printStackTrace();
        }
    }

    private void receiveHeartRateAndSpo2(DataInputStream dataInputStream) {
        try {
            heartRate = dataInputStream.readChar();
            dataInputStream.close();
            spo2 = (char) (heartRate & 0x00FF);
            heartRate = (char) (heartRate & 0xFF00);
        }
        catch (IOException e) {
            LogUtil.e(TAG, "receive heart rate IOException");
            e.printStackTrace();
        }
    }

    private void receiveBk(DataInputStream dataInputStream) {
        try {
            bk = dataInputStream.readChar();
            dataInputStream.close();
            rsv[0] = (char) (bk & 0x00FF);
            bk = (char) (bk & 0xFF00);
        }
        catch (IOException e) {
            LogUtil.e(TAG, "receive bk IOException");
            e.printStackTrace();
        }
    }

    private void receiveRsv(DataInputStream dataInputStream) {
        char c;
        try {
            for (int i = 0; i < rsv.length-2; i+=2) {
                c = dataInputStream.readChar();
                rsv[i+1] = (char) (c & 0xFF00);
                rsv[i+2] = (char) (c & 0x00FF);
            }
            c = dataInputStream.readChar();
            rsv[rsv.length-1] = (char) (c & 0xFF00);
        }
        catch (IOException e) {
            LogUtil.e(TAG, "receive rsv IOException");
            e.printStackTrace();
        }
    }

    public char getDataHead() {
        return dataHead;
    }

    public byte[] getHeartData() {
        return heartData;
    }

    public char getHeartRate() {
        return heartRate;
    }

    public char getSpo2() {
        return spo2;
    }

    public char getBk() {
        return bk;
    }

    public char[] getRsv() {
        return rsv;
    }
}
