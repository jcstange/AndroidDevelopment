package com.example.stange_pc.accbluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.zip.Inflater;

public class OrientationAccelerometer extends Activity {

    private SensorManager mSensorManager = null;
    String TAG = null;
    private BluetoothSocket socket;
    BluetoothConnected a = new BluetoothConnected(socket);



    float[] mGravs = new float[4];
    float[] mGeoMags = new float[4];
    float[] mRotationM = new float[16];
    float[] mInclinationM = new float[16];
    float[] mOrientation = new float[4];
    float[] mOldOrientation = new float[4];
    String[] mAccelerometer = new String[4];
    String[] mMagnetic = new String[4];
    String[] mRotation = new String[16];
    String[] mInclination = new String[16];
    String[] mOrientationString = new String[4];
    String[] mOldOrientationString = new String[4];

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientationaccelerometer);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        a.start();
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
        /* Get the Sensors */
        public void onSensorChanged(SensorEvent event) {


            switch (event.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    System.arraycopy(event.values, 0, mGravs, 0, 3);
                    break;
                case Sensor.TYPE_MAGNETIC_FIELD:
                    System.arraycopy(event.values, 0, mGeoMags, 0, 3);
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    System.arraycopy(event.values, 0, mOldOrientation, 0, 3);
                    break;

                default:
                    return;
            }
            // If mGravs and mGeoMags have values then find rotation matrix
            if (mGravs != null && mGeoMags != null) {

                // checks that the rotation matrix is found
                boolean success = SensorManager.getRotationMatrix(mRotationM, mInclinationM, mGravs, mGeoMags);
                if (success) {
                        /* getOrientation Values */
                    SensorManager.getOrientation(mRotationM, mOrientation);
                    double[] mOrientation_ = new double[4];
                    for (int i = 0; i < 3; i++) {
                        mAccelerometer[i] = String.format(Locale.US,"%.2f" ,mGravs[i]);
                        mMagnetic[i] = String.format(Locale.US,"%.2f",mGeoMags[i]);
                        mOrientation_[i] = Math.toDegrees((double)mOrientation[i]);
                        mOrientationString[i] = String.format(Locale.US,"%.2f",mOrientation_[i]);
                    //    mOldOreintationString[i] = String.format(Locale.US,"%.2f",mOldOreintation[i]);
                    }
                       /* Make everything text to show on device */
                    TextView xaxisAccelerometerText = (TextView) findViewById(R.id.xaxisAccelerometer);
                    xaxisAccelerometerText.setText("x: "+mAccelerometer[0]);
                    a.write(mAccelerometer[0]);
                    TextView yaxisAccelerometerText = (TextView) findViewById(R.id.yaxisAccelerometer);
                    yaxisAccelerometerText.setText("y: "+mAccelerometer[1]);
                    TextView zaxisAccelerometerText = (TextView) findViewById(R.id.zaxisAccelerometer);
                    zaxisAccelerometerText.setText("z: "+mAccelerometer[2]);
                    TextView xaxisMagneticText = (TextView) findViewById(R.id.xaxisMagnetic);
                    xaxisMagneticText.setText("x: "+mMagnetic[0]);
                    TextView yaxisMagneticText = (TextView) findViewById(R.id.yaxisMagnetic);
                    yaxisMagneticText.setText("y: "+mMagnetic[1]);
                    TextView zaxisMagneticText = (TextView) findViewById(R.id.zaxisMagnetic);
                    zaxisMagneticText.setText("z: "+mMagnetic[2]);
                    TextView xaxisOrientationText = (TextView) findViewById(R.id.xaxisOrientation);
                    xaxisOrientationText.setText("x: "+mOrientationString[0]);
                    TextView yaxisOrientationText = (TextView) findViewById(R.id.yaxisOrientation);
                    yaxisOrientationText.setText("y: "+mOrientationString[1]);
                    TextView zaxisOrientationText = (TextView) findViewById(R.id.zaxisOrientation);
                    zaxisOrientationText.setText("z: "+mOrientationString[2]);


                 /*   TextView xaxisOldOrientationText = (TextView) findViewById(R.id.xaxisOldOrientation);
                    xaxisOldOrientationText.setText("-Gyr_x: "+mOldOreintationString[0]);
                    TextView yaxisOldOrientationText = (TextView) findViewById(R.id.yaxisOldOrientation);
                    yaxisOldOrientationText.setText("-Gyr_y: "+mOldOreintationString[1]);
                    TextView zaxisOldOrientationText = (TextView) findViewById(R.id.zaxisOldOrientation);
                    zaxisOldOrientationText.setText("-Gyr_z: "+mOldOreintationString[2]);*/

                } else {

                         /* Make everything text to show on device even if getRotationMatrix fails*/
                    String matrixFailed = "Rotation Matrix Failed";
                    TextView xaxisAccelerometerText = (TextView) findViewById(R.id.xaxisAccelerometer);
                    xaxisAccelerometerText.setText(mAccelerometer[0]);
                    TextView yaxisAccelerometerText = (TextView) findViewById(R.id.yaxisAccelerometer);
                    yaxisAccelerometerText.setText(mAccelerometer[1]);
                    TextView zaxisAccelerometerText = (TextView) findViewById(R.id.zaxisAccelerometer);
                    zaxisAccelerometerText.setText(mAccelerometer[2]);
                    TextView xaxisMagneticText = (TextView) findViewById(R.id.xaxisMagnetic);
                    xaxisMagneticText.setText(mMagnetic[0]);
                    TextView yaxisMagneticText = (TextView) findViewById(R.id.yaxisMagnetic);
                    yaxisMagneticText.setText(mMagnetic[1]);
                    TextView zaxisMagneticText = (TextView) findViewById(R.id.zaxisMagnetic);
                    zaxisMagneticText.setText(mMagnetic[2]);
                    TextView xaxisOrientationText = (TextView) findViewById(R.id.xaxisOrientation);
                    xaxisOrientationText.setText(matrixFailed);
                    TextView yaxisOrientationText = (TextView) findViewById(R.id.yaxisOrientation);
                    yaxisOrientationText.setText(matrixFailed);
                    TextView zaxisOrientationText = (TextView) findViewById(R.id.zaxisOrientation);
                    zaxisOrientationText.setText(matrixFailed);
                   /* TextView xaxisOldOrientationText = (TextView) findViewById(R.id.xaxisOldOrientation);
                    xaxisOldOrientationText.setText(mOldOreintationString[0]);
                    TextView yaxisOldOrientationText = (TextView) findViewById(R.id.yaxisOldOrientation);
                    yaxisOldOrientationText.setText(mOldOreintationString[1]);
                    TextView zaxisOldOrientationText = (TextView) findViewById(R.id.zaxisOldOrientation);
                    zaxisOldOrientationText.setText(mOldOreintationString[2]);*/
                }
            }
        }
    };
    @Override
    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(sensorEventListener, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
    }

   @Override
    protected void onPause() {
       super.onPause();
       mSensorManager.unregisterListener(sensorEventListener);
   }

}
