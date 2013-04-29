package com.prosysopc.ua.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleAdapter;


	public class LogviewFragment extends ListFragment {
		static MainPager mPager;
		private SimpleAdapter adapter;
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
			List<Logmessage> messagelog =  mPager.opcreader.getMessagelog();
			HashMap<String,String> item;
			// clear the list so the list doesn't grow on every time it is created
			list.clear();
		    for( int i=0; i < messagelog.size(); i++ ){
		      item = new HashMap<String,String>();
		      item.put( "line1", messagelog.get(i).getType().toString());
		      String s = messagelog.get(i).getMessage();
		      if( s.length() < 40 )
		      {
		    	  item.put( "line2", s);
		      }
		      else
		      {
		    	  item.put( "line2", s.substring(0, 40));
		      }
		      list.add( item );
		    }
		    adapter = new SimpleAdapter(this.getActivity(), list,
		    		      android.R.layout.two_line_list_item ,
		    		      new String[] { "line1","line2" },
		    		      new int[] {android.R.id.text1, android.R.id.text2});
		    setListAdapter( adapter );
		    
		    registerForContextMenu(rootView.findViewById(android.R.id.list));
			return rootView;
		}
		
		public void onStart()
		{
			super.onStart();
			List<Logmessage> messagelog =  mPager.opcreader.getMessagelog();
			HashMap<String,String> item;
			// clear the list so the list doesn't grow on every time it is created
			list.clear();
		    for( int i=0; i < messagelog.size(); i++ ){
		      item = new HashMap<String,String>();
		      item.put( "line1", messagelog.get(i).getType().toString());
		      String s = messagelog.get(i).getMessage();
		      if( s.length() < 40 )
		      {
		    	  item.put( "line2", s);
		      }
		      else
		      {
		    	  item.put( "line2", s.substring(0, 40));
		      }
		      list.add( item );
		    }
		    adapter = new SimpleAdapter(this.getActivity(), list,
		    		      android.R.layout.two_line_list_item ,
		    		      new String[] { "line1","line2" },
		    		      new int[] {android.R.id.text1, android.R.id.text2});
		    setListAdapter( adapter );
		}
		
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		    super.onCreateContextMenu(menu, v, menuInfo);
		    if (v.getId() == this.getListView().getId()) {
		        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		        HashMap item = (HashMap) getListView().getItemAtPosition(info.position);
		        menu.setHeaderTitle("Context Menu Example");
		        menu.add(0, 0, 0, "Open");
		        // toiseksi parametriksi pitäis tulla ilmeisesti itemin id, tyyliin HashMap.getId()
		    }
		}
			
		public boolean onContextItemSelected(MenuItem item) {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			
			
		    if (item.getTitle() == "Open") {
		        //Code To Handle open
		    	List<Logmessage> messagelog =  mPager.opcreader.getMessagelog();
		    	
		    	Logmessage message = messagelog.get((int)info.id);
		    	
		    	
		    	Intent intent = new Intent(this.getActivity(), LogItemActivity.class);
		    	Bundle b = new Bundle();
		    	b.putString("type", message.getType().toString());
		    	b.putString("message", message.getMessage());
		    	b.putInt("timestamp", message.getTimestamp());
		    	intent.putExtras(b);
				startActivity(intent);
		    }
		    else {
		    	
		        return false;
		    }
		    // update view
		    onStart();
		    return true;
		}
	}