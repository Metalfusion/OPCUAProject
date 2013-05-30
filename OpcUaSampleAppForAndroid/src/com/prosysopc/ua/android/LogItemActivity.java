package com.prosysopc.ua.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LogItemActivity extends Activity {

	public static OPCReader opcreader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.logitem);

		final TextView typeview = (TextView) findViewById(R.id.messagetypeview);
		final TextView timestampview = (TextView) findViewById(R.id.timestampview);
		final TextView messageview = (TextView) findViewById(R.id.messageview);

		Bundle b = getIntent().getExtras();

		typeview.setText(b.getString("type"));
		timestampview.setText(b.getString("timestamp"));
		messageview.setText(b.getString("message"));
	}

	
	// On click event for Cancel button
	public void buttonCloseLogItemOnClick(View v) {

		// return to viewpager when cancel is pressed
		finish();
	}

}
