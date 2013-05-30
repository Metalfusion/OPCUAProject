package com.prosysopc.ua.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

// A simple activity for displaying a title and value string pair
public class ValueReadActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.valueread);

		// Find the UI elements
		final TextView titleview = (TextView) findViewById(R.id.title);
		final TextView valueview = (TextView) findViewById(R.id.textvalue);

		Bundle b = getIntent().getExtras();
		
		// Set the UI content from the Intent's bundle
		titleview.setText(b.getString("title"));
		valueview.setText(b.getString("text"));
	}

	// On click event for Cancel button
	public void buttonCloseValueReadOnClick(View v) {

		// return to viewpager when cancel is pressed
		finish();
	}

}
