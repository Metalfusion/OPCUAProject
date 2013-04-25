package com.prosysopc.ua.android;


import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class Nodelist_level_fragment extends ListFragment {

	List<String> listItems;
	
	public Nodelist_level_fragment() {
		// TODO Auto-generated constructor stub
				
	}

		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nodelistview = inflater.inflate(R.layout.nodelist_level_fragment, container, false);		
		
		
		listItems = new ArrayList<String>();
		
		for (int i = 0; i<20;i++) {
			
			if ( i % 2 == 0) {
				listItems.add("Short item");
			} else {
				listItems.add("Long item, item number: " + i);
			}		
			
			
		}
		
		listItems.add("item 1");
		
		ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);		
	    setListAdapter(adapter);
			
		
		return nodelistview;
		
		//return super.onCreateView(inflater, container, savedInstanceState);
	}
}
