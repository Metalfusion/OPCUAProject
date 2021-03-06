package com.prosysopc.ua.android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

// Adapter for UINode children display on listview
public class UINodeAdapter extends ArrayAdapter<UINode> {

	// The tags are used for retrieving the objects from the UI items
	public static final int HOLDER_KEY_ID = R.id.TAG_UINODE_HOLDER_KEY_ID;
	public static final int NODE_KEY_ID = R.id.TAG_UINODE_NODE_KEY_ID;

	Context context;
	int layoutResourceId;
	UINode rootNode = null;

	public UINodeAdapter(Context context, int layoutResourceId, UINode rootNode) {

		super(context, layoutResourceId, rootNode.childNodes);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.rootNode = rootNode;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		UINodeHolder holder = null;
		
		// Check if we need to create the holder
		if (row == null) {
			
			// Create the UI from XML
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			// Find the UI elements
			holder = new UINodeHolder();
			holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
			holder.txtTitle = (TextView) row.findViewById(R.id.txtTitle);
			
			// Save the objects as tags
			row.setTag(HOLDER_KEY_ID, holder);
			row.setTag(NODE_KEY_ID, rootNode.childNodes.get(position));

		} else {
			holder = (UINodeHolder) row.getTag(HOLDER_KEY_ID);
		}

		// Set the data to the holder parameters
		UINode UINode = rootNode.childNodes.get(position);
		holder.txtTitle.setText(truncateString(UINode.name));
		holder.imgIcon.setImageResource(UINode.type.value);

		return row;
	}
	
	// Inner holder class for the UI elements
	static class UINodeHolder {
		ImageView imgIcon;
		TextView txtTitle;
	}
		
	@Override
	public UINode getItem(int position) {

		try {
			return rootNode.childNodes.get(position);
		} catch (Exception e) {
			return null;
		}

	}
	
	// Limits the string length to a predefined constant value
	private String truncateString(String str) {

		if (str.length() > MainPager.LIST_LINE_LENGTH) {
			return str.substring(0, MainPager.LIST_LINE_LENGTH - 3) + "...";
		}

		return str;

	}
}
