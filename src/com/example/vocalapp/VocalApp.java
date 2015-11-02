package com.example.vocalapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class VocalApp extends Activity{
	
	private Button start,stop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.vocalapp);
		
		start = (Button) findViewById(R.id.startVocalService);
		stop = (Button) findViewById(R.id.stopVocalService);
		
		start.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				startService(new Intent(getBaseContext(),VocalService.class));
			}
			
		});
		
		stop.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	
				stopService(new Intent(getBaseContext(),VocalService.class));
			}
			
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, 0, "Quit demo");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			finish();
			break;
		default:
		}
		return super.onOptionsItemSelected(item);
	}

}
