package com.prosysopc.ua.android;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.prosysopc.ua.android.Logmessage.LogmessageType;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.Toast;

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

	public static OPCReader opcreader;
	public static final int LIST_LINE_LENGTH = 100; 		
	public static MainPager pager;
	
	
	public MainPager () {
		opcreader = new OPCReader();
		
		try {
			opcreader.addModifyServer("Simulator", "opc.tcp://10.0.2.2:4841", "", "", 20);
			opcreader.addModifyServer("Public test server", "opc.tcp://opcua.info:62949/Advosol/uaSimUC", "ua", "test", 20);
			opcreader.addModifyServer("Ascolab", "opc.tcp://demo.ascolab.com:4841", "", "", 20);
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			opcreader.addLog(LogmessageType.WARNING, e.toString() );
		}
		opcreader.addLog( Logmessage.LogmessageType.INFO, "Program start");
		opcreader.addLog( Logmessage.LogmessageType.WARNING, "Warning message example");
		opcreader.addLog( Logmessage.LogmessageType.ERROR, "Error message example");
		ServerSettingsActivity.opcreader = opcreader;
		
		pager = this;
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		// purkkaratkaisu android.os.NetworkOnMainThreadExceptioniin
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
		
		
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
	
	 @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
	 
	        super.onOptionsItemSelected(item);
	 
	        switch(item.getItemId()){
	           
	            case R.id.disconnect:	                
	                
	                try {
						opcreader.updateConnection(null);
					} catch (URISyntaxException e) {						 
						opcreader.addLog(LogmessageType.ERROR, e.toString() );
					}
	                
	                Toast.makeText(getBaseContext(), "Disconnected", Toast.LENGTH_SHORT).show();
	                
	                break;
	 
	            case R.id.exit:
	            	
	            	try {
						opcreader.updateConnection(null);
					} catch (URISyntaxException e) {						 
						opcreader.addLog(LogmessageType.WARNING, e.toString() );
					}
	            	
	                Toast.makeText(getBaseContext(), "Exiting", Toast.LENGTH_SHORT).show();	                
	                finish();
	                
	                break;	 
	        }
	        return true;
	 
	    }

	private List<Fragment> getFragments(){

		  List<Fragment> fList = new ArrayList<Fragment>();
		  
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
		Bundle b = new Bundle();
    	b.putString("name", "ExampleServer");
    	b.putString("address", "opc.tcp://");
    	b.putString("identity","");
    	b.putString("password","");
    	b.putInt("timeout",5);
    	intent.putExtras(b);
		startActivity(intent);
	}

	
	/**
	 * Computes the widest view in an adapter, best used when you need to wrap_content on a ListView, please be careful
	 * and don't use it on an adapter that is extremely numerous in items or it will take a long time.
	 *
	 * @param context Some context
	 * @param adapter The adapter to process
	 * @return The pixel width of the widest View
	 */
	public static int getWidestView(Context context, Adapter adapter) {
	    int maxWidth = 0;
	    View view = null;
	    FrameLayout fakeParent = new FrameLayout(context);
	    for (int i=0, count=adapter.getCount(); i<count; i++) {
	        view = adapter.getView(i, view, fakeParent);
	        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
	        int width = view.getMeasuredWidth();
	        if (width > maxWidth) {
	            maxWidth = width;
	        }
	    }
	    return maxWidth;
	}
	
	
	
	public void onActivityResult( int requestCode, int resultCode, Intent data)
	{
		if( resultCode == Activity.RESULT_OK)
		{
			Bundle b = data.getExtras();
			// and writes the value to server
			opcreader.writeAttributes( b.getString("newValue") );
			
		}
	}
	
}
