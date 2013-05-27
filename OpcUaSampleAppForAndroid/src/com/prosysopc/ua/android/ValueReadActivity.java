package com.prosysopc.ua.android;

import android.app.Activity;
import android.os.Bundle;

import android.view.Menu;
import android.view.View;

import android.widget.TextView;

public class ValueReadActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.valueread);

		final TextView titleview = (TextView) findViewById(R.id.title);
		final TextView valueview = (TextView) findViewById(R.id.textvalue);

		Bundle b = getIntent().getExtras();

		titleview.setText(b.getString("title"));
		valueview.setText(b.getString("text"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_pager, menu);
		return true;
	}

	// On click event for Cancel button
	public void buttonCloseValueReadOnClick(View v) {

		// return to viewpager when cancel is pressed
		finish();
	}

}
