package com.example.vocalapp;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Index extends Activity implements OnClickListener{
	
	private Button vocalButton;
	private Button mvtButton;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.index);
		initialization();
	}

	private void initialization() {
		// TODO Auto-generated method stub
		vocalButton = (Button) findViewById(R.id.vocalService);
		vocalButton.setOnClickListener(this);
		mvtButton = (Button) findViewById(R.id.mvtService);
		mvtButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.mvtService : 
			startActivity(new Intent(getApplicationContext(),Shake.class));
			break;
		case R.id.vocalService:
			startActivity(new Intent(getApplicationContext(),VocalApp.class));
			break;
		}
	}
}
