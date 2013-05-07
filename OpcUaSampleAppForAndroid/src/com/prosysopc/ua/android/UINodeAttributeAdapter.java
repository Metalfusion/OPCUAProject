package com.prosysopc.ua.android;

import java.util.List;

import com.prosysopc.ua.android.UINode.AttributeValuePair;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UINodeAttributeAdapter extends ArrayAdapter<AttributeValuePair> {

	Context context;
	int layoutResource;
	List<AttributeValuePair> attributes;
	
	public UINodeAttributeAdapter(Context context, List<AttributeValuePair> attributes) {
		super(context, android.R.layout.two_line_list_item, attributes);
		
		this.layoutResource = android.R.layout.two_line_list_item;
		this.context = context;
		this.attributes = attributes;
		
	}
	
	
	 @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        UINodeAttributeHolder holder = null;
        
        if(row == null)
        {        	
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResource, parent, false);
            
            holder = new UINodeAttributeHolder();
            holder.txt1 = (TextView)row.findViewById(android.R.id.text1);
            holder.txt2 = (TextView)row.findViewById(android.R.id.text2);
            
            row.setTag(holder);
        }
        else
        {
            holder = (UINodeAttributeHolder)row.getTag();
        }
                
        AttributeValuePair attr = attributes.get(position);
        
        holder.txt1.setText(truncateString(attr.attrName));
        holder.txt2.setText(truncateString(attr.attrValue));
        
        return row;
    }
    
    static class UINodeAttributeHolder
    {
        TextView txt1;
        TextView txt2;
    }
	
    private String truncateString(String str) {
    	
    	if (str.length() > MainPager.LIST_LINE_LENGTH) {
    		return str.substring(0,MainPager.LIST_LINE_LENGTH-3) + "...";
    	}
    	
    	return str;
    	
    }
    

}
