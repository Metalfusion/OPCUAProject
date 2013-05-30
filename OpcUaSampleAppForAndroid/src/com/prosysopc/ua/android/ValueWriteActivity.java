package com.prosysopc.ua.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class ValueWriteActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.valuewrite);

		final TextView titleview = (TextView) findViewById(R.id.title);
		final EditText valueview = (EditText) findViewById(R.id.textvalue);

		Bundle b = getIntent().getExtras();

		titleview.setText(b.getString("title"));
		valueview.setText(b.getString("value"));
	}

	// On click event for OK button
	public void buttonOKWriteViewOnClick(View v) {

		String newValue = ((EditText) findViewById(R.id.textvalue)).getText().toString();

		// Send the result to the MainPager to be processed
		Intent resultData = new Intent();
		resultData.putExtra("newValue", newValue);
		setResult(Activity.RESULT_OK, resultData);
		finish();
	}

	// On click event for Cancel button
	public void buttonCloseValueWriteOnClick(View v) {

		// return to viewpager when cancel is pressed
		finish();
	}

}
