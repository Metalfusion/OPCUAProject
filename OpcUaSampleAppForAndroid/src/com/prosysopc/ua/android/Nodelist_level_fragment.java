package com.prosysopc.ua.android;


import java.util.ArrayList;

import com.prosysopc.ua.android.UINode.UINodeType;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Nodelist_level_fragment extends ListFragment {
	
	// This array hold the data to be displayed
	public UINode rootNode;
	
	public Nodelist_level_fragment() {
		// TODO Auto-generated constructor stub
		rootNode = null;
	}

		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View nodelistview = inflater.inflate(R.layout.nodelist_level_fragment, container, false);		
		
		createTestData();
		
        UINodeAdapter adapter = new UINodeAdapter(getActivity(), R.layout.nodelistitem, rootNode);
        		
		//ArrayAdapter<String> adapter= new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, listItems);		
	    setListAdapter(adapter);
				
		return nodelistview;
		
	}
	
	@Override
	public void onListItemClick(android.widget.ListView l, View v, int position, long id) {
		
		v.getTag();
		
	}
	
	
	// Dummy data generation for testing
	private void createTestData() {
			
		ArrayList<UINode> childarr = new ArrayList<UINode>();
		childarr.add(new UINode(UINodeType.folderNode,"Folder node",123));
		childarr.add(new UINode(UINodeType.leafNode,"Normal node",12123));
		childarr.add(new UINode(UINodeType.leafNode,"Normal node 2",121312323));
		childarr.add(new UINode(UINodeType.leafNode,"Normal node 3",23));
		
		rootNode = new UINode(UINodeType.folderNode,"Node x",343, childarr);
		        
	}
	
}
