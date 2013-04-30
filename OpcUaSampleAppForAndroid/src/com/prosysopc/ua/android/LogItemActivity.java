package com.prosysopc.ua.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;

import android.widget.TextView;


public class LogItemActivity extends Activity {

	public static OPCReader opcreader;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logitem);
		
		final TextView typeview = (TextView)findViewById(R.id.messagetypeview);
		final TextView timestampview = (TextView)findViewById(R.id.timestampview);
		final TextView messageview = (TextView)findViewById(R.id.messageview);
		
		
		Bundle b = getIntent().getExtras();
		
		typeview.setText(b.getString("type"));
		timestampview.setText(b.getString("timestamp"));
		messageview.setText(b.getString("message"));
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_pager, menu);
		return true;
	}

	//On click event for Cancel button
	public void buttonCloseLogItemOnClick(View v) {
		// return to viewpager when cancel is pressed
		finish();
	}
	

}
