package com.prosysopc.ua.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

// Activity for modifying a single value and displaying its title/name
public class ValueWriteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.valuewrite);
		
		// Find the UI elements
		final TextView titleview = (TextView) findViewById(R.id.title);
		final EditText valueview = (EditText) findViewById(R.id.textvalue);

		Bundle b = getIntent().getExtras();
		
		// Set the UI elements with the data from the Intent's bundle
		titleview.setText(b.getString("title"));
		valueview.setText(b.getString("value"));
	}

	// On click event for OK button
	public void buttonOKWriteViewOnClick(View v) {
		
		// Get the modified value from the UI EditText widget
		String newValue = ((EditText) findViewById(R.id.textvalue)).getText().toString();

		// Send the result to the MainPager to be processed
		Intent resultData = new Intent();
		resultData.putExtra("newValue", newValue);
		setResult(Activity.RESULT_OK, resultData);
		finish();
	}

	// On click event for Cancel button
	public void buttonCloseValueWriteOnClick(View v) {

		// Return to MainPager when cancel is pressed
		finish();
	}

}
