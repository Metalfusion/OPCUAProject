package com.prosysopc.ua.android;


import java.util.ArrayList;

import org.opcfoundation.ua.builtintypes.NodeId;
import com.prosysopc.ua.android.UINode.UINodeType;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class Nodelist_level_fragment extends ListFragment implements OnClickListener {
	
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
		
		if (rootNode == null || (rootNode.attributes == null && rootNode.childNodes.isEmpty())) {
			createTestData();
		}		
		
		ListAdapter adapter;			    
		String headerStr = "";
		
		if ( showAttributes ) {
			headerStr ="Attrib. of " + rootNode.name;
			adapter = new UINodeAttributeAdapter(getActivity(), rootNode.attributes);
		} else {
			headerStr ="Nodes of " + rootNode.name;
			adapter = new UINodeAdapter(getActivity(), R.layout.nodelistitem, rootNode);
		}
						
	    setListAdapter(adapter);
	    
	    if (headerStr.length() > MainPager.LIST_LINE_LENGTH) {
	    	headerStr = headerStr.substring(0, MainPager.LIST_LINE_LENGTH-3) + "...";
	    }
	    
	    TextView headerText= (TextView)nodelistview.findViewById(R.id.HeaderText);
	    headerText.setText(headerStr);
	    headerText.setOnClickListener(this);
	    
		return nodelistview;
		
	}
	
	@Override
	public void onListItemClick(android.widget.ListView l, View v, int position, long id) {
		
								
		if (! showAttributes) {
			
			try {
				
				// Clear highlights from others					
				for (int i = 0 ; i < l.getChildCount(); i++) {
					if (i == position) {							
						l.getChildAt(i).setBackgroundColor(Color.rgb(51, 181, 229));						
					} else	{						
						l.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
					}
				}					
									
			} catch (Exception e) {					
				//e.printStackTrace();
			}
			
			UINode node;
			try {
				
				node = (UINode)this.getListView().getChildAt(position).getTag(UINodeAdapter.NODE_KEY_ID);								
				nodebrowser.createList(this.listLevel+1, node, node.type == UINodeType.leafNode);
				
			} catch (Exception e) {}
			
			// Scroll the selected to the top				
			l.setSelectionFromTop(position, 0);
			
		} else {
			// TODO: Add attribute full text read window popup/activity
			// Optionally could open a context menu that has read and write options
		}
		
		
	}
		
	
	
	// Event handler for the header click, opens attributes list
	public void onClick(View v) {
		nodebrowser.createList(this.listLevel+1, this.rootNode, true);
	}
	
	// Dummy data generation for testing
	private void createTestData() {
		
		String name;
		
		if (rootNode == null) {
			name = "x";
		} else {
			name = rootNode.name;
		}
		
		ArrayList<UINode> childarr = new ArrayList<UINode>();
		childarr.add(new UINode(UINodeType.folderNode,"Folder node 1",new NodeId(1,2)));
		childarr.add(new UINode(UINodeType.folderNode,"Folder node 2",new NodeId(2,3)));
		childarr.add(new UINode(UINodeType.folderNode,"Folder node 3",new NodeId(4,5)));
		
		for (int i = 1; i < 8; i++) {
			childarr.add(new UINode(UINodeType.leafNode,"Data node " + i,new NodeId(i+3,i*2+5)));			
		}		
		
		rootNode = new UINode(UINodeType.folderNode,name,new NodeId(7,8), childarr);
		
		for (int i = 1; i < 5; i++) {
			rootNode.addAttribute("Attibute " + i, "Value " + i);
		}
		
		rootNode.attributesSet = rootNode.referencesSet = true;
		
		        
	}
	
	
}
