package com.prosysopc.ua.android;

import java.util.List;

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


	public class SublistFragment extends ListFragment {
		static MainPager mPager;
		private SubscriptionAdapter adapter;
		/**
		 * The fragment argument representing the section number for this fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
	
		public SublistFragment() {
		}
		
		public static final SublistFragment newInstance(MainPager pager)
		{
			mPager = pager;
			SublistFragment f = new SublistFragment();
			return f;
			
		}
	
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.sublist, container, false);
			//updateAdapter();
	   	    
		   // registerForContextMenu(rootView.findViewById(android.R.id.list));
			return rootView;
		}
		
		private void updateAdapter() {
			List<Logmessage> messagelog =  mPager.opcreader.getMessagelog();
			adapter = new SubscriptionAdapter(getActivity(), R.layout.icon_two_line_list_item, messagelog);
			setListAdapter( adapter );
		}
		
		
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		    
			super.onCreateContextMenu(menu, v, menuInfo);
		    
			if (v.getId() == this.getListView().getId()) {
		        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		        //HashMap item = (HashMap) getListView().getItemAtPosition(info.position); // t‰m‰ casti heitt‰‰ poikkeuksen
		        menu.setHeaderTitle("Select action");
		        menu.add(0, 0, 0, "Open");
		        // toiseksi parametriksi pit‰is tulla ilmeisesti itemin id, tyyliin HashMap.getId()
		    }
			
		}
			
		public boolean onContextItemSelected(MenuItem item) {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			
			
		    if (item.getTitle() == "Open") {
		        //Code To Handle open
		    	//List<Subscription> subscriptions =  mPager.opcreader.getSubscriptions();
		    	
		    	//Subscription subscription = subscriptions.get((int)info.id);
		    	
		    	
		    	
		    }
		    else {
		    	
		        return false;
		    }
		    // update view
		    onStart();
		    return true;
		}
		
		
	}