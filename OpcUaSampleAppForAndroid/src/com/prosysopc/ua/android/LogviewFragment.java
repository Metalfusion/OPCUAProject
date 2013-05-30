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
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.prosysopc.ua.android.Logmessage.LogmessageType;

// A fragment for displaying a list of log objects
public class LogviewFragment extends ListFragment implements IUpdateable {
	static MainPager mPager;

	private LogMessageAdapter adapter;
	ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public LogviewFragment() {

	}

	public static final LogviewFragment newInstance(MainPager pager) {

		mPager = pager;
		LogviewFragment f = new LogviewFragment();
		return f;

	}
	
	// Creates the UI
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.logview, container, false);

		updateAdapter();

		registerForContextMenu(rootView.findViewById(android.R.id.list));
		return rootView;
	}

	public void onStart() {

		super.onStart();
		updateAdapter();
	}
	
	// Re-creates the adapter, effectively resetting the whole fragment
	private void updateAdapter() {

		List<Logmessage> messagelog = MainPager.opcreader.getMessagelog();
		adapter = new LogMessageAdapter(getActivity(), R.layout.icon_two_line_list_item, messagelog);
		setListAdapter(adapter);
	}

	// Handles the context menu creation
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
		
		// Verify that the sender view is our listview
		if (v.getId() == this.getListView().getId()) {

			menu.setHeaderTitle("Select action");
			menu.add(0, 0, 0, "Open this item");
			menu.add(0, 0, 0, "Clear log");

		}

		super.onCreateContextMenu(menu, v, menuInfo);

	}
	
	// Handle the context menu item selection event
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		// User wants to open the item
		if (item.getTitle().equals("Open this item")) {
			
			// Get the most up to date Logmessage object
			List<Logmessage> messagelog = MainPager.opcreader.getMessagelog();
			Logmessage message = messagelog.get((int) info.id);
			
			// Create an Intent to call the LogItemActivity
			Intent intent = new Intent(this.getActivity(), LogItemActivity.class);
			Bundle b = new Bundle();
			b.putString("type", message.getType().toString());
			b.putString("message", message.getMessage());
			b.putString("timestamp", message.getTimestampString());
			intent.putExtras(b);
			startActivity(intent);

		} else if (item.getTitle().equals("Clear log")) {
			
			// Remove all log messages
			MainPager.opcreader.clearLog();

			// update view
			onStart();

		} else {
			return false;
		}

		return true;
	}
	
	// Update implementation resets the fragment
	@Override
	public void update() {

		try {
			this.updateAdapter();
		} catch (Exception e) {
			MainPager.opcreader.addLog(LogmessageType.ERROR, "View update failed" + "\n" + e.toString());
		}

	}

}
