package com.prosysopc.ua.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.prosysopc.ua.android.Logmessage.LogmessageType;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class SublistFragment extends ListFragment implements IUpdateable {
	static MainPager mPager;
	private SimpleAdapter adapter;

	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public SublistFragment() {

	}

	public static final SublistFragment newInstance(MainPager pager) {

		mPager = pager;
		SublistFragment f = new SublistFragment();
		return f;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.sublist, container, false);
		updateList();

		registerForContextMenu(rootView.findViewById(android.R.id.list));
		return rootView;
	}

	/*
	 * private void updateAdapter() { List<Logmessage> messagelog = mPager.opcreader.getMessagelog(); adapter = new
	 * SubscriptionAdapter(getActivity(), R.layout.icon_two_line_list_item, messagelog); setListAdapter( adapter ); }
	 */

	public void onStart() {

		super.onStart();
		updateList();
	}

	private void updateList() {

		List<SubscriptionData> subscriptions = MainPager.opcreader.getSubscriptionData();
		HashMap<String, String> item;

		// clear the list so the list doesn't grow on every time it is created
		list.clear();

		for (int i = 0; i < subscriptions.size(); i++) {
			item = new HashMap<String, String>();
			item.put("line1", subscriptions.get(i).getNodeId().toString());
			item.put("line2", subscriptions.get(i).getValue());
			list.add(item);
		}
		adapter = new SimpleAdapter(this.getActivity(), list, android.R.layout.two_line_list_item, new String[] { "line1", "line2" }, new int[] { android.R.id.text1, android.R.id.text2 });
		setListAdapter(adapter);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
	
		super.onCreateContextMenu(menu, v, menuInfo);
		
		if (v.getId() == this.getListView().getId()) { 
			  			   			  
			menu.setHeaderTitle("Select action"); 
			menu.add(0, 0, 0, "More info");
			menu.add(0, 0, 0, "Remove");
			  		  
		}
		 
	}

	public boolean onContextItemSelected(MenuItem item) {
		
		SubscriptionData subData;
		
		try {		
			
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			List<SubscriptionData> subscriptions = MainPager.opcreader.getSubscriptionData();
			subData = subscriptions.get((int)info.id);
			
		} catch (Exception e) {
			return false;
		}
		
		if (item.getTitle() == "More info") {
						
			// Use the valueReadActivity to show the subscription data			
			Intent intent = new Intent(getActivity(), ValueReadActivity.class);

			Bundle b = new Bundle();
			b.putString("title", "NodeID: " + subData.getNodeId().toString());
			b.putString("text", "Latest update received: " + subData.getReceiveTime().toString()  + "\n" + "Value: " + subData.getValue());

			intent.putExtras(b);
			startActivity(intent);
			
		} else if (item.getTitle() == "Remove") {
			
			MainPager.opcreader.removeSubscription(subData.getDataItem());
			
			// update view
			updateList();
			
		} else {
			
			return false;
		}
				
		return true;
	}

	@Override
	public void update() {

		try {
			this.updateList();
		} catch (Exception e) {
			MainPager.opcreader.addLog(LogmessageType.ERROR, "View update failed" + "\n" + e.toString());
		}

	}

}