package com.example.stange_pc.accbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.example.stange_pc.accbluetooth.OrientationAccelerometer;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void startSensors(View v){
        Intent intent = new Intent(MainActivity.this, OrientationAccelerometer.class);
        startActivity(intent);
    }

    public void startBluetooth(View v){
        Intent intent = new Intent(MainActivity.this, Bluetooth.class);
        startActivity(intent);
    }

    public void close(View v){
        finish();
    }


}

