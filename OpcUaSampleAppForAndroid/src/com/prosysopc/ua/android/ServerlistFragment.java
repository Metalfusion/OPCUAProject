package com.prosysopc.ua.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class ServerlistFragment extends ListFragment {
	static MainPager mPager;
	
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public ServerlistFragment() {
	}
	
	public static final ServerlistFragment newInstance( MainPager pager)
	{
		mPager = pager;
		ServerlistFragment f = new ServerlistFragment();
		return f;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.serverlist, container, false);
		
		return rootView;
	}
	
	// set servernames to
	
	final ListView listview = (ListView) getView().findViewById(R.id.listViewServers);
	//final ArrayAdapter<String> adapter = new  ArrayAdapter<String>( this, 
	//		android.R.layout.simple_list_item_1, mPager.OPCreader.getServers());
	
		
	
}