package com.prosysopc.ua.android;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;


@SuppressLint("ValidFragment")
public class ServerlistFragment extends ListFragment {
	static MainPager mPager;
	private SimpleAdapter adapter;
	ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	@SuppressLint("ValidFragment")
	public ServerlistFragment(MainPager pager) {
		mPager = pager;
	}
	
	public static final ServerlistFragment newInstance( MainPager pager )
	{
		
		ServerlistFragment f = new ServerlistFragment( pager );
		return f;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.serverlist, container, false);
		List<Server> servers =  mPager.opcreader.getServers();
		HashMap<String,String> item;
		    for( int i=0; i < servers.size(); i++ ){
		      item = new HashMap<String,String>();
		      item.put( "line1", servers.get(i).getName());
		      item.put( "line2", servers.get(i).getAddress());
		      list.add( item );
		    }
		    adapter = new SimpleAdapter(this.getActivity(), list,
		    		      android.R.layout.two_line_list_item ,
		    		      new String[] { "line1","line2" },
		    		      new int[] {android.R.id.text1, android.R.id.text2});
		    setListAdapter( adapter );

		
		return rootView;
	}
	
	// set servernames to
	
	//final ListView listview = (ListView) getView().findViewById(R.id.listViewServers);
	//final ArrayAdapter<String> adapter = new  ArrayAdapter<String>( this, 
	//		android.R.layout.simple_list_item_1, mPager.OPCreader.getServers());
	
		
	
}