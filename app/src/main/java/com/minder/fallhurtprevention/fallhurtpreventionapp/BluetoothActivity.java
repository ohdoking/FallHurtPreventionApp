package com.minder.fallhurtprevention.fallhurtpreventionapp;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class BluetoothActivity extends Activity implements View.OnClickListener {

    // Layout
    private Button btn_Connect;
    private TextView txt_Result;

    public BluetoothAdapter mBluetoothAdapter;

    public static final int REQUEST_ENABLE_BT = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bluetooth);

        /** Main Layout **/
        btn_Connect = (Button) findViewById(R.id.btn_connect);
        txt_Result = (TextView) findViewById(R.id.txt_result);

        btn_Connect.setOnClickListener(this);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter.isEnabled()) {
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


        switch (requestCode) {


        }
    }

    public void startBluetoothSettings(Activity activity) {

        Intent settingsIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);

        activity.startActivity(settingsIntent);

    }

    public void requestBluetoothEnable(Activity activity) {

        Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        activity.startActivityForResult(bluetoothIntent, REQUEST_ENABLE_BT);

    }

}