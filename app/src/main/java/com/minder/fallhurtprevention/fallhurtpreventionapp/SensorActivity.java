package com.minder.fallhurtprevention.fallhurtpreventionapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class SensorActivity extends Activity implements SensorEventListener {
    private Sensor mAccelerometer;
    private Sensor mGyroscope;
    private Sensor mLight;
    private Sensor mGravity;
    private Sensor mStepCounter;

    private String TAG = "Sensors";
    private SensorManager mSensorManager;

    private TextView xVal;
    private Button resetBtn;

    private static final double EPSILON = 0.1f;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private double gyroscopeRotationVelocity = 0;

    private long timestamp;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);


        //1. 디바이스에서 사용 가능한 센서 정보 확인
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> deviceSensors = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mGyroscope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mGravity = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mStepCounter = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        xVal= (TextView) findViewById(R.id.xval);
        resetBtn = (Button) findViewById(R.id.resetBtn);

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xVal.setText("");
            }
        });
        //...생략
    }

    @Override
    protected void onResume() {
        super.onResume();
        //3. 이벤트 리스너 설정
        //SENSOR_DELAY_NORMAL --> 0.2 sec
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mGravity, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mStepCounter, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        synchronized (this)
        {
            //4. Sensor row data 수신
            float var0, var1, var2;

            switch (sensorEvent.sensor.getType()) {
                case Sensor.TYPE_ACCELEROMETER:
                    var0 = sensorEvent.values[0];
                    var1 = sensorEvent.values[1];
                    var2 = sensorEvent.values[2];
                    Log.d(TAG, "Accelerometer: " + "x = " + var0 + ", y = " + var1 +" , z = " + var2);


                    if(var2 > 17.0f){
//                        xVal.setText("Accelerometer: " + "x = " + var0 + ", y = " + var1 +" , z = " + var2);
                    }
                    break;
                case Sensor.TYPE_GYROSCOPE:
                    var0 = sensorEvent.values[0];
                    var1 = sensorEvent.values[1];
                    var2 = sensorEvent.values[2];
                    Log.d(TAG, "Gyroscope: " + "x = " + var0 + ", y = " + var1 +" , z = " + var2);

                    if (timestamp != 0) {
                        final float dT = (sensorEvent.timestamp - timestamp) * NS2S;
                        // Axis of the rotation sample, not normalized yet.
                        float axisX = sensorEvent.values[0];
                        float axisY = sensorEvent.values[1];
                        float axisZ = sensorEvent.values[2];

                        // Calculate the angular speed of the sample
                        gyroscopeRotationVelocity = Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

                        // Normalize the rotation vector if it's big enough to get the axis
                        if (gyroscopeRotationVelocity > EPSILON) {
                            axisX /= gyroscopeRotationVelocity;
                            axisY /= gyroscopeRotationVelocity;
                            axisZ /= gyroscopeRotationVelocity;
                        }

                        // Integrate around this axis with the angular speed by the timestep
                        // in order to get a delta rotation from this sample over the timestep
                        // We will convert this axis-angle representation of the delta rotation
                        // into a quaternion before turning it into the rotation matrix.
                        double thetaOverTwo = gyroscopeRotationVelocity * dT / 2.0f;
                        double sinThetaOverTwo = Math.sin(thetaOverTwo);
                        double cosThetaOverTwo = Math.cos(thetaOverTwo);

                        double somethingx = (float) (sinThetaOverTwo * axisX);
                        double somethingy = (float) (sinThetaOverTwo * axisX);
                        double somethingz = (float) (sinThetaOverTwo * axisZ);
                        double somethingw = (-(float) cosThetaOverTwo);

                        if(gyroscopeRotationVelocity > 5.0f)
                            xVal.setText("x = " + somethingx + ", y = " + somethingy +" , z = " + somethingz +" , w = " + somethingw + "gyroscopeRotationVelocity " + gyroscopeRotationVelocity );
                    }
                    timestamp = sensorEvent.timestamp;


                    if(var0 > 4.0f || var1 > 4.0f || var2 > 4.0f)
//                            xVal.setText("Accelerometer: " + "x = " + axisX + ", y = " + axisY +" , z = " + axisZ);


                        break;
                case Sensor.TYPE_LIGHT:
                    var0 = sensorEvent.values[0];
                    Log.d(TAG, "Light: " + "lux = " + var0);
                    break;
                case Sensor.TYPE_GRAVITY:
                    var0 = sensorEvent.values[0];
                    var1 = sensorEvent.values[1];
                    var2 = sensorEvent.values[2];
                    Log.d(TAG, "Gravity: " + "x = " + var0 + ", y = " + var1 +" , z = " + var2);
                    break;
                case Sensor.TYPE_STEP_COUNTER:
                    var0 = sensorEvent.values[0];
                    Log.d(TAG, "Step Counter: " + var0);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Log.d(TAG, "onAccuracyChanged()");
    }



}