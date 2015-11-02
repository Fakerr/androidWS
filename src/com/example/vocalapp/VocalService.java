package com.example.vocalapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class VocalService extends Service implements OnSignalsDetectedListener{
	
	private DetectorThread detectorThread;
	private RecorderThread recorderThread;
	private static final int NOTIFICATION_Id = 001;
	
	public static final int DETECT_NONE = 0;
	public static final int DETECT_WHISTLE = 1;
	public static int selectedDetection = DETECT_NONE;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		initNotification();
		startDetection();
		return START_STICKY;
	}
	
	public void initNotification(){
		NotificationCompat.Builder mBuilder =
			    new NotificationCompat.Builder(this)
			    .setSmallIcon(R.drawable.whistle)
			    .setContentTitle("Whistle detection")
			    .setContentText("Whistle detection is on");
		
		Intent resultIntent = new Intent(this, VocalApp.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity( this,0,
				resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);

		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotifyMgr = 
		        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotifyMgr.notify(NOTIFICATION_Id, mBuilder.build());
	}
	
	public void startDetection(){
		selectedDetection = DETECT_WHISTLE;
		recorderThread = new RecorderThread();
		recorderThread.start();
		detectorThread = new DetectorThread(recorderThread);
		detectorThread.setOnSignalsDetectedListener(this);
		detectorThread.start();
		Toast.makeText(this, "Service started", Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (recorderThread != null) {
			recorderThread.stopRecording();
			recorderThread = null;
		}
		if (detectorThread != null) {
			detectorThread.stopDetection();
			detectorThread = null;
		}
		selectedDetection = DETECT_NONE;
		Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
		stopNotification();
		//android.os.Process.killProcess(android.os.Process.myPid());
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onWhistleDetected() {
		Intent intent = new Intent(this,VocalAlert.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivity(intent);
	    Toast.makeText(this, "Whisthle detected", Toast.LENGTH_LONG).show();
        this.stopSelf();
		
	}
	
	public void stopNotification(){
		NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotifyMgr.cancel(NOTIFICATION_Id);
	}

}
