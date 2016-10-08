package com.example.stange_pc.accbluetooth;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;


/**
 * Created by Stange-PC on 06/10/2016.
 */


public class BluetoothConnected extends Thread {
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private Handler mHandler;
    private int MESSAGE_READ = 0;


    public BluetoothConnected(BluetoothSocket socket) {
        mmSocket = socket;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // Get the input and output streams, using temp objects because
        // member streams are final
        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {
            Log.e("TAG", "You constructed bluetooth connection");
            e.printStackTrace();
        }

        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    public void run() {
        byte[] buffer = new byte[1024];  // buffer store for the stream
        int bytes; // bytes returned from read()

        // Keep listening to the InputStream until an exception occurs
        while (true) {
            try {
                 //Read from the InputStream
                 bytes = mmInStream.read(buffer);
                 //Send the obtained bytes to the UI activity
                 mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                       .sendToTarget();
            } catch (IOException e) {
                Log.e("TAG", "You reached read");
                e.printStackTrace();
                break;
            }
        }
    }

    /* Call this from the main activity to send data to the remote device */
    public void write(String s) {
        try {
            mmOutStream.write(s.getBytes());
        } catch (IOException e) {
            Log.e("TAG", "You reached write");
        }
    }

    /* Call this from the main activity to shutdown the connection */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }


}

