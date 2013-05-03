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
	
	// This array hold the data to be displayed
	public UINode node_data[];
	
	public Nodelist_level_fragment() {
		// TODO Auto-generated constructor stub
		node_data = new UINode[0];
	}

		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nodelistview = inflater.inflate(R.layout.nodelist_level_fragment, container, false);		
		
		createTestData();
		
        UINodeAdapter adapter = new UINodeAdapter(getActivity(), R.layout.nodelistitem, node_data);
        		
		//ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);		
	    setListAdapter(adapter);
				
		return nodelistview;
		
	}
	
	
	// Dummy data generation for testing
	private void createTestData() {
				
		node_data = new UINode[]
        {
			new UINode(R.drawable.folder,"Folder node",123),
			new UINode(R.drawable.text_list_bullets,"Normal node",12123),
			new UINode(R.drawable.ic_folder,"Folder node 2",23),
			new UINode(R.drawable.ic_folder,"Folder node 3",143),
			new UINode(R.drawable.ic_list,"Normal node 2",13),			
			new UINode(R.drawable.ic_folder,"Folder node 4",23),
			new UINode(R.drawable.ic_folder,"Folder node 5",143),
			new UINode(R.drawable.ic_list,"Normal node 3",13),
			
        };
		        
	}
	
}
