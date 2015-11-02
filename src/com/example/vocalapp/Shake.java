package com.example.vocalapp;

import com.example.vocalapp.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Shake extends Activity implements OnClickListener {

	private Button active;
	private Button stop;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shake);

		active = (Button) findViewById(R.id.active_shake);
		stop = (Button) findViewById(R.id.end_shake);

		active.setOnClickListener(this);
		stop.setOnClickListener(this);	
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.active_shake:
			getPackageManager().setComponentEnabledSetting( 
				    new ComponentName( this,CallBroadcast.class ),
				        PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
				        PackageManager.DONT_KILL_APP );
			Toast.makeText(this, "receiver registred", Toast.LENGTH_LONG)
					.show();
			break;
		case R.id.end_shake:
			getPackageManager().setComponentEnabledSetting( 
				    new ComponentName( this, CallBroadcast.class ),
				        PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
				        PackageManager.DONT_KILL_APP );
		
			Toast.makeText(this, "receiver unregistred", Toast.LENGTH_LONG)
					.show();
			break;
		}
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
