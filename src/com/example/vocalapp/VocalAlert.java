package com.example.vocalapp;

import com.example.vocalapp.R;
import com.example.vocalapp.VocalService;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VocalAlert extends Activity implements View.OnClickListener
    ,OnCompletionListener{

	private MediaPlayer mySong;
    private Button bReturn;
    private Button bRestart;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.alertsong);
	    stopService(new Intent(getBaseContext(),VocalService.class));
		initialize();
		
		mySong = MediaPlayer.create(this,R.raw.alarm);
		mySong.setOnCompletionListener(this);
		mySong.start();
		
	}

	private void initialize() {
		bReturn = (Button) findViewById(R.id.quitButton);
		bRestart = (Button) findViewById(R.id.restartButton);
		bReturn.setOnClickListener(this);
		bRestart.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.restartButton :
		     mySong.release();
		     Intent i = new Intent(getBaseContext(),VocalService.class);
		     startService(i);
		     finish();
		    break;
		case R.id.quitButton:
			mySong.release();
			finish();
			break;
		}
	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		arg0.start();
	}
	
}	