package com.prosysopc.ua.android;


import java.util.ArrayList;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.prosysopc.ua.android.UINode.AttributeValuePair;
import com.prosysopc.ua.android.UINode.UINodeType;
import com.prosysopc.ua.android.UINodeAdapter.UINodeHolder;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

public class Nodelist_level_fragment extends ListFragment {
	
	// This node holds the data to be displayed
	private UINode rootNode;
	private int listLevel;		// Level of this list at the hierarchy of the nodebrowser
	private NodebrowserFragment nodebrowser;
	private boolean showAttributes = false;
	
	public Nodelist_level_fragment() {
		// TODO Auto-generated constructor stub
		rootNode = null;
	}
		
	public void setup(NodebrowserFragment nodebrowser, UINode rootNode, int level, boolean showAttributes) {
		this.listLevel = level;
		this.rootNode = rootNode;
		this.nodebrowser = nodebrowser;
		this.showAttributes = showAttributes;
	}	
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View nodelistview = inflater.inflate(R.layout.nodelist_level_fragment, container, false);		
		
		if (rootNode == null) {
			createTestData();
		}		
		
		ListAdapter adapter;
		
		if ( showAttributes ) {
			adapter = new UINodeAttributeAdapter(getActivity(), rootNode.attributes);
		} else {
			adapter = new UINodeAdapter(getActivity(), R.layout.nodelistitem, rootNode);
		}
						
	    setListAdapter(adapter);
				
		return nodelistview;
		
	}
	
	@Override
	public void onListItemClick(android.widget.ListView l, View v, int position, long id) {
		
	//	if (v.getId() == this.getListView().getId()) {
			
			if (! showAttributes) {
								
				UINode node = (UINode)this.getListView().getChildAt(position).getTag(UINodeAdapter.NODE_KEY_ID);
				
				// TODO: Add node browse and type (leaf/folder) investigation with the OPCUA SDK before the list creation call
				// The call to nodebrowser should then be done as a callback
				nodebrowser.createList(this.listLevel+1, node, node.type == UINodeType.leafNode);
				
			} else {
				// TODO: Add attribute full text read window popup/activity
				// Optionally could open a context menu that has read and write options
			}
			
			
		}
		
	//}
	
	
	// Dummy data generation for testing
	private void createTestData() {
			
		ArrayList<UINode> childarr = new ArrayList<UINode>();
		childarr.add(new UINode(UINodeType.folderNode,"Folder node",new NodeId(1,2)));
		childarr.add(new UINode(UINodeType.leafNode,"Normal node",new NodeId(2,3)));
		childarr.add(new UINode(UINodeType.leafNode,"Normal node 2",new NodeId(3,4)));
		childarr.add(new UINode(UINodeType.leafNode,"Normal node 3",new NodeId(5,6)));
		
		rootNode = new UINode(UINodeType.folderNode,"Node x",new NodeId(7,8), childarr);
		
		for (int i = 1; i < 5; i++) {
			rootNode.addAttribute("Attibute " + i, "Value " + i);
		}
		
		        
	}
	
}
