package com.prosysopc.ua.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;


public class LogviewFragment extends ListFragment {
	static MainPager mPager;
	
	private LogMessageAdapter adapter;
	ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public LogviewFragment() {
	}
	
	public static final LogviewFragment newInstance(MainPager pager)
	{
		mPager = pager;
		LogviewFragment f = new LogviewFragment();
		return f;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.logview, container, false);
		
		updateAdapter();
		    	   	    
	    registerForContextMenu(rootView.findViewById(android.R.id.list));
		return rootView;
	}
	
	public void onStart()
	{
		super.onStart();		
		updateAdapter();
	}
	
	private void updateAdapter() {
		List<Logmessage> messagelog =  mPager.opcreader.getMessagelog();
		adapter = new LogMessageAdapter(getActivity(), R.layout.icon_two_line_list_item, messagelog);
		setListAdapter( adapter );
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
	    
		if (v.getId() == this.getListView().getId()) {
	        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
	        
	        menu.setHeaderTitle("Select action");
	        menu.add(0, 0, 0, "Open this item");
	        menu.add(0, 0, 0, "Clear log");
	        
	    }
		
		super.onCreateContextMenu(menu, v, menuInfo);
		
	}
		
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
				
	    if (item.getTitle().equals("Open this item")) {
	        //Code To Handle open
	    	List<Logmessage> messagelog =  mPager.opcreader.getMessagelog();
	    	
	    	Logmessage message = messagelog.get((int)info.id);
	    	
	    	
	    	Intent intent = new Intent(this.getActivity(), LogItemActivity.class);
	    	Bundle b = new Bundle();
	    	b.putString("type", message.getType().toString());
	    	b.putString("message", message.getMessage());
	    	b.putString("timestamp", message.getTimestampString());
	    	intent.putExtras(b);
			startActivity(intent);
	    
	    } else if (item.getTitle().equals("Clear log")) {
	    
	    	mPager.opcreader.clearLog();
	    	
	    	// update view
		    onStart();
	    	
	    } else {	    	
	        return false;
	    }
	    	    
	    return true;
	}
}
	
	
	