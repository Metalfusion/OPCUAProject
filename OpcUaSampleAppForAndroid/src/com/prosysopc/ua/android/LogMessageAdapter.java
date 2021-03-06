package com.prosysopc.ua.android;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

// Adapter for displaying Logmessage-objects in a ListView
public class LogMessageAdapter extends ArrayAdapter<Logmessage> {
	
	// Different icons for the different log message types
	static int[] resArr = { R.drawable.error, R.drawable.exclamation, R.drawable.information };

	Context context;
	int layoutResourceId;
	List<Logmessage> data;

	public LogMessageAdapter(Context context, int layoutResourceId, List<Logmessage> data) {

		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View row = convertView;
		LogmessageHolder holder = null;

		// Check if we need to create the Holder for this item
		if (row == null) {
			
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new LogmessageHolder();
			holder.imgIcon = (ImageView) row.findViewById(R.id.imgIcon);
			holder.txt1 = (TextView) row.findViewById(R.id.text1);
			holder.txt2 = (TextView) row.findViewById(R.id.text2);

			row.setTag(holder);
			
		} else {
			holder = (LogmessageHolder) row.getTag();
		}

		Logmessage logmsg = data.get(position);
		String msg = logmsg.getMessage();
		
		// Truncate the message if it is too long
		if (msg.length() > MainPager.LIST_LINE_LENGTH) {
			msg = msg.substring(0, MainPager.LIST_LINE_LENGTH - 3) + "...";
		}
		
		// Fill the UI elements with data
		holder.txt1.setText(logmsg.getTimestampString());
		holder.txt2.setText(msg);
		holder.imgIcon.setImageResource(resArr[logmsg.getType().ordinal()]);

		return row;
	}

	// Inner class for holding the UI elements for a single item
	static class LogmessageHolder {
		ImageView imgIcon;
		TextView txt1;
		TextView txt2;
	}
}
