package com.minder.fallhurtprevention.fallhurtpreventionapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;


public class BluetoothActivity extends Activity implements View.OnClickListener {

    // Layout
    private Button btn_Connect;
    private TextView txt_Result;

    public BluetoothAdapter mBluetoothAdapter;

    public static final int REQUEST_ENABLE_BT = 1;

    private BluetoothSocket mConnectSocket;
    private boolean mIsConnected = false;
    private InputStream mDataInputStream;
    private OutputStream mDataOutputStream;

    private static UUID UUID_SPP = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bluetooth);

        /** Main Layout **/
        btn_Connect = (Button) findViewById(R.id.btn_connect);
        txt_Result = (TextView) findViewById(R.id.txt_result);

        btn_Connect.setOnClickListener(this);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            requestBluetoothEnable(BluetoothActivity.this);
        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_connect:

                startBluetoothSettings(BluetoothActivity.this);

        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i("ohdoking!",requestCode+"???");
        switch (requestCode) {
            case REQUEST_ENABLE_BT:

                Set<BluetoothDevice> bluetoothDeviceList = mBluetoothAdapter.getBondedDevices();

                Log.i("ohdoking!",UUID_SPP.toString());

                for(BluetoothDevice bd: bluetoothDeviceList) {
                    Log.i("ohdoking",bd.getName()+" : "+bd.getAddress()+"");
                    if(bd.getAddress().equals("C9:50:76:97:97:00"))
                        requestConnect(bd,UUID_SPP);
                    break;
                }

        }
    }

    public void startBluetoothSettings(Activity activity) {

        Intent settingsIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);

        activity.startActivityForResult(settingsIntent,REQUEST_ENABLE_BT);

    }

    public void requestBluetoothEnable(Activity activity) {

        Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        activity.startActivity(bluetoothIntent);

    }

    public BluetoothSocket getBluetoothSocket(BluetoothDevice device, UUID uuid) throws IOException {

        return device.createInsecureRfcommSocketToServiceRecord(uuid);

    }


    public boolean requestConnect(BluetoothDevice device, UUID uuid) {

        try {

            mConnectSocket = getBluetoothSocket(device, uuid);

            if (mConnectSocket != null) {

                new Thread(new Runnable() {

                    @Override

                    public void run() {

                        try {

                            mConnectSocket.connect();
                            Log.i("ohdoking","connect complete");

                            mIsConnected = true;

                            requestDataStreaming(mConnectSocket);

                        } catch (IOException e) {

                            e.printStackTrace();

                            mIsConnected = false;

                        }

                    }

                }).start();

            }

        } catch (IOException e) {

            e.printStackTrace();

            mIsConnected = false;

        }

        return mIsConnected;

    }

    public void requestDataStreaming(BluetoothSocket bluetoothSocket) {
        Log.i("ohdoking","instance 생성 완료");
        if (mIsConnected) {

            try {
                Log.i("ohdoking","instance 생성 완료2");

                final Handler handler = new Handler();

                mDataInputStream = bluetoothSocket.getInputStream();
                mDataOutputStream = bluetoothSocket.getOutputStream();

                Log.i("ohdoking","instance 생성 완료3");


                new Thread(new Runnable() {

                    @Override

                    public void run() {

                            byte[] buffer = new byte[1024];

                            int bytes;

                            while (mIsConnected && mDataInputStream != null) {

                                try {

                                    bytes = mDataInputStream.read(buffer);
//                                System.arraycopy(buffer, 0, bytes, 0, bytes.length);
                                    final int data = bytes;

                                            Log.i("ohdoking-data", data + "");


                                } catch (IOException e) {

                                    e.printStackTrace();

                                    break;

                                }

                            }

                        }

                }).start();

            } catch (IOException e) {

                e.printStackTrace();

            }

        }

    }

}