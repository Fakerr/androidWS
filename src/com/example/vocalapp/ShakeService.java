package com.example.vocalapp;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.internal.telephony.ITelephony;

public class ShakeService extends Service implements SensorEventListener{
	
	private SensorManager sensorMg = null;
	private Sensor sensor = null;
	private boolean running = false;
	private boolean supported = false;
	private boolean offhook = false;
	private static float threshold  = 15.0f; 
    private static int interval     = 200;
	
	private long now = 0;
    private long timeDiff = 0;
    private long lastUpdate = 0;
    private long lastShake = 0;

    private float x = 0;
    private float y = 0;
    private float z = 0;
    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;
    private float force = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Toast.makeText(getBaseContext(), "onStartCommand Accelerometer Started", 
                Toast.LENGTH_SHORT).show();
		if (isSupported()) {
            //Start Accelerometer Listening
			startListening();
        }else{
        	Toast.makeText(getBaseContext(), "onStartCommand Accelerometer not supported", 
                    Toast.LENGTH_SHORT).show();
        }
		return START_STICKY;
	}
	
	
	private void startListening() {
		// TODO Auto-generated method stub       
		sensorMg= (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sensorMg.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (sensors.size() > 0){
            sensor = sensors.get(0);
            running = sensorMg.registerListener(this, sensor,SensorManager.SENSOR_DELAY_GAME);
        }     
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (isListening()) {
            stopListening();
            Toast.makeText(getBaseContext(), "onDestroy Accelerometer destroyed", 
                     Toast.LENGTH_SHORT).show();
        }
	}
	
	
	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		
        now = event.timestamp;

        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        if (lastUpdate == 0) {
            lastUpdate = now;
            lastShake = now;
            lastX = x;
            lastY = y;
            lastZ = z;
            Toast.makeText(getBaseContext(),"No Motion detected", 
               Toast.LENGTH_SHORT).show();
             
        } else {
            timeDiff = now - lastUpdate;
            
            if (timeDiff > 0) { 
                 
                force = Math.abs(x + y + z - lastX - lastY - lastZ);
                
                if (Float.compare(force, threshold) >0 ) {
                    if (now - lastShake >= interval) { 
                        onShake();
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(),"No Motion detected", 
                            Toast.LENGTH_SHORT).show();
                    }
                    lastShake = now;
                }
                lastX = x;
                lastY = y;
                lastZ = z;
                lastUpdate = now; 
            }
            else
            {
                Toast.makeText(getBaseContext(),"No Motion detected", Toast.LENGTH_SHORT).show();  
            }
        }
	}
	
	
	public boolean isListening() {
        return running;
    }
		
	
	public  void stopListening() {
        running = false;
        try {
            if (sensorMg != null ) {
                sensorMg.unregisterListener(this);
            }
        } catch (Exception e) {}
    }
	
		
	 public boolean isSupported() {
	        if (supported == false) {
	        	    sensorMg= (SensorManager) getSystemService(SENSOR_SERVICE);
	                List<Sensor> sensors = sensorMg.getSensorList(Sensor.TYPE_ACCELEROMETER);  
	                if(sensors.size() > 0){
	                	supported = true;
	                }
	        }
	        return supported;
	    }
	 
	 
	 private void onShake() {
			// TODO Auto-generated method stub
		 Toast.makeText(getBaseContext(),"Motion detected", Toast.LENGTH_SHORT).show();
		 if(offhook == false){
			 Toast.makeText(getBaseContext(),"offhook is false", Toast.LENGTH_SHORT).show();
			 acceptCall(this);
		 }else{
			 Toast.makeText(getBaseContext(),"offhook is true", Toast.LENGTH_SHORT).show();
			 endCall(this);
		}
}
	 
	 public void endCall(Context context) {
		 
		 TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		 Class clazz=null;
		try {
			clazz = Class.forName(telephonyManager.getClass().getName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		 Method method=null;
		try {
			method = clazz.getDeclaredMethod("getITelephony");
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		 method.setAccessible(true);
		 ITelephony telephonyService=null;
		try {
			telephonyService = (ITelephony) method.invoke(telephonyManager);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		 try {
			telephonyService.endCall();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		 stopSelf();
	}

	public void acceptCall(Context context){
		 Intent buttonUp = new Intent(Intent.ACTION_MEDIA_BUTTON);
		 buttonUp.putExtra(Intent.EXTRA_KEY_EVENT,
		 new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_HEADSETHOOK));
		 context.sendOrderedBroadcast(buttonUp, "android.permission.CALL_PRIVILEGED");	 
		 offhook = true;
	 }
	
}
