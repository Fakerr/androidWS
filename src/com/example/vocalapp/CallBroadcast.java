package com.example.vocalapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class CallBroadcast extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String msg = "Phone state changed to " + state;

        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
        	Intent i = new Intent(context,ShakeService.class);
        	context.startService(i);
        }else if(TelephonyManager.EXTRA_STATE_IDLE.equals(state)){
        	context.stopService(new Intent(context,ShakeService.class));
	  }
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show(); 
	}
}
