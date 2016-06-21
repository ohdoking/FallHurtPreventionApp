package com.minder.fallhurtprevention.fallhurtpreventionapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    public Button shinBtn;
    public Button leeBtn;
    public Button ohBtn;

    public Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        shinBtn = (Button) findViewById(R.id.shinBtn);
        leeBtn = (Button) findViewById(R.id.leeBtn);
        ohBtn = (Button) findViewById(R.id.ohBtn);


        shinBtn.setOnClickListener(this);
        leeBtn.setOnClickListener(this);
        ohBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){
            case R.id.shinBtn:
                i = new Intent(MainActivity.this, ScrollingActivity.class);
                startActivity(i);
                break;
            case R.id.leeBtn:
                i = new Intent(MainActivity.this, ScrollingActivity.class);
                startActivity(i);
                break;
            case R.id.ohBtn:
                i = new Intent(MainActivity.this, BluetoothActivity.class);
                startActivity(i);
                break;
        }
    }
}
