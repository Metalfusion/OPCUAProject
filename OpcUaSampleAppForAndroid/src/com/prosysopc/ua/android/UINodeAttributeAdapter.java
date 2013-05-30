package com.prosysopc.ua.android;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.prosysopc.ua.android.UINode.AttributeValuePair;

// An adapter class for displaying UINode's attributes and their values in a listView
public class UINodeAttributeAdapter extends ArrayAdapter<AttributeValuePair> {

	Context context;
	int layoutResource;
	List<AttributeValuePair> attributes; // The data to display

	public UINodeAttributeAdapter(Context context, List<AttributeValuePair> attributes) {

		super(context, android.R.layout.two_line_list_item, attributes);

		this.layoutResource = android.R.layout.two_line_list_item;
		this.context = context;
		this.attributes = attributes;

	}

	// Creates a view for the given position in the list
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		UINodeAttributeHolder holder = null;
		
		// Check if we need to create the holder
		if (row == null) {
			
			// Create the UI from XML
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResource, parent, false);
			
			// Fill the holder
			holder = new UINodeAttributeHolder();
			holder.txt1 = (TextView) row.findViewById(android.R.id.text1);
			holder.txt2 = (TextView) row.findViewById(android.R.id.text2);
			
			// Save the holder as tag
			row.setTag(holder);
			
		} else {
			holder = (UINodeAttributeHolder) row.getTag();
		}

		AttributeValuePair attr = attributes.get(position);
		
		// Update the holder with current data
		holder.txt1.setText(truncateString(attr.attrName));
		holder.txt2.setText(truncateString(attr.attrValue));

		return row;
	}

	// Inner holder class for the UI elements
	static class UINodeAttributeHolder {
		TextView txt1;
		TextView txt2;
	}
	
	// Limits the string length to a predefined constant value
	private String truncateString(String str) {

		if (str.length() > MainPager.LIST_LINE_LENGTH) {
			return str.substring(0, MainPager.LIST_LINE_LENGTH - 3) + "...";
		}

		return str;

	}

}
