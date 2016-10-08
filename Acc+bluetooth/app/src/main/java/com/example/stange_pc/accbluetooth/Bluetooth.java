package com.example.stange_pc.accbluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static android.R.id.list;
import static android.content.ContentValues.TAG;
import static com.example.stange_pc.accbluetooth.R.id.lv;


public class Bluetooth extends Activity {
    public View v;
    public BluetoothAdapter mBluetoothAdapter;
    public Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_DEVICE_ADDRESS = "device_address";
    ListView lv;
    ListView lvn;
    public Thread BT;

    private ArrayAdapter<String> mNewDevicesArrayAdapter;
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bluetooth);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        lv = (ListView) findViewById(R.id.lv);
        lvn = (ListView) findViewById(R.id.lvn);
        on(v);
    }

    @Override
    protected void onResume() {
        super.onResume();
        list(v);
    }

    @Override
    protected void onStop(){
        super.onStop();
        unregisterReceiver(mReceiver);
    }


    public void on(View v) {

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            Toast.makeText(getApplicationContext(), "BT Turned on", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "BT Already on", Toast.LENGTH_LONG).show();
        }
    }

    public void list(View v) {

        pairedDevices = mBluetoothAdapter.getBondedDevices();

        ArrayAdapter<String> pairedDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);
        mNewDevicesArrayAdapter = new ArrayAdapter<String>(this, R.layout.device_name);

        // Find and set up the ListView for paired devices
        ListView pairedListView = (ListView) findViewById(R.id.lv);
        pairedListView.setAdapter(pairedDevicesArrayAdapter);
        pairedListView.setOnItemClickListener(mDeviceClickListener);

        // Find and set up the ListView for newly discovered devices
        ListView newDevicesListView = (ListView) findViewById(R.id.lvn);
        newDevicesListView.setAdapter(mNewDevicesArrayAdapter);
        newDevicesListView.setOnItemClickListener(mDeviceClickListener);

        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mReceiver, filter);

        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mReceiver, filter);

        // Get the local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        // If there are paired devices, add each one to the ArrayAdapter
        if (pairedDevices.size() > 0) {
            findViewById(R.id.lvn).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : pairedDevices) {
                pairedDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = "none paired";
            pairedDevicesArrayAdapter.add(noDevices);
        }
    }


    public void off(View v) {

        mBluetoothAdapter.disable();
        Toast.makeText(getApplicationContext(), "Turned off", Toast.LENGTH_LONG).show();
    }

    private void doDiscovery() {
        Log.d(TAG, "doDiscovery()");

        // Indicate scanning in the title
        Toast.makeText(getApplicationContext(), "Scanning", Toast.LENGTH_LONG).show();

        // Turn on sub-title for new devices
        findViewById(R.id.lv).setVisibility(View.VISIBLE);

        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();

        }

        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
    }

    public void startBluetooth(View v) {
        doDiscovery();
        v.setVisibility(View.GONE);

    }


    private AdapterView.OnItemClickListener mDeviceClickListener
            = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> ln, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            mBluetoothAdapter.cancelDiscovery();

            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            final String address = info.substring(info.length() - 17);
            Log.i(info, "i'm here");

            BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

            ConnectThread connectThread = new ConnectThread(device);
            connectThread.start();

            finish();
        }


            /*     Method m = null;
                    try {
                        m = device.getClass().getMethod("createRfcommSocket", new Class[] {int.class});
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                        Log.e("TAG", "Couldn't create method");
                    }
                    BluetoothSocket socket = null;
                    try {
                        socket = (BluetoothSocket) m.invoke(device, 1);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();

                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.connect();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("TAG", "Couldn't connect");
                        try {
                            socket.close();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }*/

            };






    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed already
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    mNewDevicesArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                }
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle("selected device");
                if (mNewDevicesArrayAdapter.getCount() == 0) {
                    String noDevices = "no devices";
                    mNewDevicesArrayAdapter.add(noDevices);
                }
            }
        }
    };

};

