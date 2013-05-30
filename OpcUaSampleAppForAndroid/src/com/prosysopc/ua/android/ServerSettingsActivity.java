package com.prosysopc.ua.android;

import java.net.URISyntaxException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.prosysopc.ua.android.Logmessage.LogmessageType;

public class ServerSettingsActivity extends Activity {

	public static OPCReader opcreader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.serversettings);

		EditText etServer = (EditText) findViewById(R.id.editTextServerName);
		EditText etAddress = (EditText) findViewById(R.id.editTextAddress);
		EditText etIdentity = (EditText) findViewById(R.id.editTextIdentity);
		EditText etPassword = (EditText) findViewById(R.id.editTextPassword);
		EditText etTimeout = (EditText) findViewById(R.id.editTextTimeout);

		Bundle b = getIntent().getExtras();
		// set view from bundle
		etServer.setText(b.getString("name"));
		etAddress.setText(b.getString("address"));
		etIdentity.setText(b.getString("identity"));
		etPassword.setText(b.getString("password"));
		etTimeout.setText("" + b.getInt("timeout"));
	}

	
	// On click event for Cancel button
	public void buttonCancelServerSettingsEditOnClick(View v) {

		// return to viewpager when cancel is pressed
		finish();
	}

	// On click event for Save button
	public void buttonSaveServerSettingsEditOnClick(View v) {

		// read server settings from editText-boxes
		EditText etServer = (EditText) findViewById(R.id.editTextServerName);
		EditText etAddress = (EditText) findViewById(R.id.editTextAddress);
		EditText etIdentity = (EditText) findViewById(R.id.editTextIdentity);
		EditText etPassword = (EditText) findViewById(R.id.editTextPassword);
		EditText etTimeout = (EditText) findViewById(R.id.editTextTimeout);

		// save server settings
		try {
			opcreader.addModifyServer(etServer.getText().toString(), etAddress.getText().toString(), etIdentity.getText().toString(), etPassword.getText().toString(),
					Integer.parseInt(etTimeout.getText().toString()));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			opcreader.addLog(LogmessageType.WARNING, e.toString());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			opcreader.addLog(LogmessageType.WARNING, e.toString());
		}

		// log the addition
		opcreader.addLog(Logmessage.LogmessageType.INFO, "Server: " + etServer.getText().toString() + " was added");

		// return to viewpager
		finish();

	}

}
