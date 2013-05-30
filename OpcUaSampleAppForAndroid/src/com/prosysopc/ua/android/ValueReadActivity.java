package com.prosysopc.ua.android;

import android.app.Activity;
import android.os.Bundle;
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

	// On click event for Cancel button
	public void buttonCloseValueReadOnClick(View v) {

		// return to viewpager when cancel is pressed
		finish();
	}

}
