package com.prosysopc.ua.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ServerSettingsActivity extends Activity {

	public static OPCReader opcreader;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.serversettings);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_pager, menu);
		return true;
	}

	//On click event for Cancel button
	public void buttonCancelServerSettingsEditOnClick(View v) {
		// return to viewpager when cancel is pressed
		finish();
	}
	
	//On click event for Save button
	public void buttonSaveServerSettingsEditOnClick(View v) {
		// read server settings from editText-boxes
		EditText etServer = (EditText)findViewById(R.id.editTextServerName);
		EditText etAddress = (EditText)findViewById(R.id.editTextAddress);
		EditText etIdentity = (EditText)findViewById(R.id.editTextIdentity);
		EditText etPassword = (EditText)findViewById(R.id.editTextPassword);
		EditText etTimeout = (EditText)findViewById(R.id.editTextTimeout);
		
	
		// save server settings
		opcreader.addModifyServer(etServer.getText().toString(), etAddress.getText().toString(), 
								etIdentity.getText().toString(), etPassword.getText().toString(), 
								Integer.parseInt(etTimeout.getText().toString()));
		
		// return to viewpager
		finish();
		
		
	}

}
