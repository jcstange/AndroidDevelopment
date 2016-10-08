package com.example.stange_pc.accbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

import static android.content.ContentValues.TAG;

/**
 * Created by Stange-PC on 06/10/2016.
 */

public class ConnectThread extends Thread {
    private BluetoothSocket mmSocket = null;
    private BluetoothDevice mmDevice = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    UUID MY_UUID = UUID.fromString("00000003-0000-1000-8000-00805F9B34FB");

    public ConnectThread(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothSocket tmp = null;
        mmDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            Method m = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            mmSocket = (BluetoothSocket) m.invoke(device, 1);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.e(TAG, "Couldn't create method");
        }  catch (IllegalAccessException e) {
             e.printStackTrace();
        } catch (InvocationTargetException e) {
             e.printStackTrace();
        }
/*        try {
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
    }
        /*try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) { }
        mmSocket = tmp;
    }*/

    public void run() {
        // Cancel discovery because it will slow down the connection
        mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and get out
            Log.e(TAG, "Sorry, could't connect to mmSocket");
            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "Sorry, can't close mmSocket");
            }
            return;
        }

        // Do work to manage the connection (in a separate thread)
        BluetoothConnected bluetoothConnected = new BluetoothConnected(mmSocket);
        bluetoothConnected.start();
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
}
