package com.prosysopc.ua.android;

import java.util.ArrayList;

import org.opcfoundation.ua.builtintypes.NodeId;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.prosysopc.ua.android.Logmessage.LogmessageType;
import com.prosysopc.ua.android.UINode.AttributeValuePair;
import com.prosysopc.ua.android.UINode.UINodeType;

@SuppressLint("ValidFragment")
public class Nodelist_level_fragment extends ListFragment implements OnClickListener {

	// This node holds the data to be displayed
	private UINode rootNode = null;
	private int listLevel; // Level of this list at the hierarchy of the nodebrowser
	private NodebrowserFragment nodebrowser;
	private boolean showAttributes = false;
	private AttributeValuePair selectedItem;

	public static OPCReader opcreader;

	// id for write attribute call
	static final int WRITE_ATTRIBUTE_CALL = 1;
	static final int RESULT = 2;

	public Nodelist_level_fragment() {

	}

	public Nodelist_level_fragment(OPCReader opcreader) {

		rootNode = null;
		Nodelist_level_fragment.opcreader = opcreader;
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

		if (showAttributes) {
			headerStr = "Attrib. of " + rootNode.name;
			adapter = new UINodeAttributeAdapter(getActivity(), rootNode.attributes);
		} else {
			headerStr = "Nodes of " + rootNode.name;
			adapter = new UINodeAdapter(getActivity(), R.layout.nodelistitem, rootNode);
		}

		setListAdapter(adapter);

		if (headerStr.length() > MainPager.LIST_LINE_LENGTH) {
			headerStr = headerStr.substring(0, MainPager.LIST_LINE_LENGTH - 3) + "...";
		}

		TextView headerText = (TextView) nodelistview.findViewById(R.id.HeaderText);
		headerText.setText(headerStr);
		headerText.setOnClickListener(this);
		
		// Scroll the new list onto view
		nodebrowser.scrollViewToRight();
		
		return nodelistview;

	}

	// Handler for the listview item click events
	@Override
	public void onListItemClick(android.widget.ListView l, View v, int position, long id) {

		// Test if we are displaying nodes or attributes
		if (!showAttributes) {

			try {

				// Clear highlights from others
				for (int i = 0; i < getListAdapter().getCount(); i++) {

					View child = getListAdapter().getView(i, null, null);

					if (i == position) {
						child.setBackgroundColor(Color.rgb(51, 181, 229));
					} else {
						child.setBackgroundColor(Color.TRANSPARENT);
					}
				}

				getView().findViewById(R.id.HeaderText).setBackgroundColor(Color.TRANSPARENT);

			} catch (Exception e) {
				// e.printStackTrace();
			}

			UINode node;
			try {

				node = (UINode) (getListAdapter().getItem(position));

				// node = (UINode)this.getListView().getChildAt(position).getTag(UINodeAdapter.NODE_KEY_ID);
				nodebrowser.createList(this.listLevel + 1, node, node.type == UINodeType.leafNode);

			} catch (Exception e) {
				MainPager.opcreader.addLog(LogmessageType.ERROR, e.toString() + "\n" + e.getMessage());
			}

			// Scroll the selected to the top
			// l.setSelectionFromTop(position, 0);

		} else {

			selectedItem = (AttributeValuePair) l.getAdapter().getItem(position);

			registerForContextMenu(l);
			l.showContextMenu();
			unregisterForContextMenu(l);

		}

	}

	// Event handler for the attribute context menu
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

		super.onCreateContextMenu(menu, v, menuInfo);

		Boolean hasValue = false;

		for (AttributeValuePair attr : rootNode.attributes) {

			if (attr.attrName == "Value") {
				hasValue = true;
				break;
			}

		}

		// AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		menu.setHeaderTitle("Select action");
		menu.add(0, 0, 0, "Open attribute");

		if (hasValue) {
			menu.add(0, 0, 0, "Edit node value");
			menu.add(0, 0, 0, "Subscribe to node");
		}

	}

	// Event handler for context menu click
	public boolean onContextItemSelected(MenuItem item) {

		// AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

		if (selectedItem == null) {
			return false;
		}

		if (item.getTitle() == "Open attribute") {

			Intent intent = new Intent(getActivity(), ValueReadActivity.class);

			Bundle b = new Bundle();
			b.putString("title", selectedItem.attrName);
			b.putString("text", selectedItem.attrValue);

			intent.putExtras(b);
			startActivity(intent);
			selectedItem = null;

		} else if (item.getTitle() == "Edit node value") {

			Intent intent = new Intent(getActivity(), ValueWriteActivity.class);

			AttributeValuePair valueAttr = null;

			for (AttributeValuePair attr : rootNode.attributes) {

				if (attr.attrName == "Value") {
					valueAttr = attr;
					break;
				}
			}

			Bundle b = new Bundle();
			b.putString("title", valueAttr.attrName);
			b.putString("value", valueAttr.attrValue);

			intent.putExtras(b);

			// set nodeid in opcreader for the write
			opcreader.setNodeIdtoBeWritten(rootNode.getNodeId());

			// 1 is the id for the result
			startActivityForResult(intent, WRITE_ATTRIBUTE_CALL);
			selectedItem = null;

		} else if (item.getTitle() == "Subscribe to node") {

			opcreader.subscribe(rootNode.getNodeId());
			selectedItem = null;

		} else {

			return false;
		}

		return true;
	}

	// reads the return value from ValueWriteActivity
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == WRITE_ATTRIBUTE_CALL) {
			if (resultCode == Activity.RESULT_OK) {
				Bundle b = data.getExtras();
				// and writes the value to server
				opcreader.writeAttributes(b.getString("newValue"));

			}
		}

	}

	// Event handler for the header click, opens attributes list
	public void onClick(View v) {
		
		if (!showAttributes) {
		
			v.setBackgroundColor(Color.rgb(51, 181, 229));
			nodebrowser.createList(this.listLevel + 1, this.rootNode, true);
			
		}
	}

	public UINode getNode() {

		return rootNode;
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
		childarr.add(new UINode(UINodeType.folderNode, "Folder node 1", new NodeId(1, 2)));
		childarr.add(new UINode(UINodeType.folderNode, "Folder node 2", new NodeId(2, 3)));
		childarr.add(new UINode(UINodeType.folderNode, "Folder node 3", new NodeId(4, 5)));

		for (int i = 1; i < 8; i++) {
			childarr.add(new UINode(UINodeType.leafNode, "Data node " + i, new NodeId(i + 3, i * 2 + 5)));
		}

		rootNode = new UINode(UINodeType.folderNode, name, new NodeId(7, 8), childarr);

		for (int i = 1; i < 5; i++) {
			rootNode.addAttribute("Attribute " + i, "Value " + i);
		}

		rootNode.attributesSet = rootNode.referencesSet = true;

	}

}
