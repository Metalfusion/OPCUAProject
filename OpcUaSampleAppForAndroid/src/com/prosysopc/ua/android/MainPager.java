package com.prosysopc.ua.android;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.View;

public class MainPager extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	MainPagerAdapter mPagerAdapter;	

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	public OPCReader opcreader;
	
	
	
	
	public MainPager () {
		opcreader = new OPCReader();
		opcreader.addModifyServer("localhost", 
				"opc.tcp://10.0.2.2:52520/OPCUA/SampleConsoleServer", "", "", 5);
		opcreader.addLog( Logmessage.LogmessageType.INFO, "Program start");
		ServerSettingsActivity.opcreader = opcreader;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_pager);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		List<Fragment> fragments = getFragments();

		mPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),fragments);
		//mViewPager.setAdapter(new MainPagerAdapter(getChildFragmentManager(), fragments));
		
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mPagerAdapter);
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_pager, menu);
		return true;
	}

	private List<Fragment> getFragments(){

		  List<Fragment> fList = new ArrayList<Fragment>();

		  //fList.add(DummySectionFragment.newInstance());
		  
		  fList.add(ServerlistFragment.newInstance(this));
	
		  fList.add(NodebrowserFragment.newInstance(this));
	
		  fList.add(SublistFragment.newInstance(this));
		  
		  fList.add(LogviewFragment.newInstance(this));
		  
		  return fList;

		}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class MainPagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> fragments;
		
		public MainPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);	
			this.fragments = fragments;

		}

		@Override
		public Fragment getItem(int position) {
			
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			//Fragment fragment = new DummySectionFragment();
			//Bundle args = new Bundle();
			//args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			//fragment.setArguments(args);
			//return fragment;
			return this.fragments.get(position);

		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			//return 3;
			return this.fragments.size();

		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.servers).toUpperCase(l);
			case 1:
				return getString(R.string.node_browser).toUpperCase(l);
			case 2:
				return getString(R.string.subscriptions).toUpperCase(l);
			case 3:
				return getString(R.string.log_browser).toUpperCase(l);
			}
			return null;
		}
	}
	
	//On click event for button1
	public void buttonServerEditOnClick(View v) {
		Intent intent = new Intent(this, ServerSettingsActivity.class);
		startActivity(intent);
	}

}
