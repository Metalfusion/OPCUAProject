package com.prosysopc.ua.android;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class UINodeAdapter extends ArrayAdapter<UINode> {
	
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
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new UINodeHolder();
            holder.imgIcon = (ImageView)row.findViewById(R.id.imgIcon);
            holder.txtTitle = (TextView)row.findViewById(R.id.txtTitle);
            
            row.setTag(holder);
        }
        else
        {
            holder = (UINodeHolder)row.getTag();
        }
        
        UINode UINode = rootNode.childNodes.get(position);
        holder.txtTitle.setText(UINode.name);
        holder.imgIcon.setImageResource(UINode.type.value);
        
        return row;
    }
    
    static class UINodeHolder
    {
        ImageView imgIcon;
        TextView txtTitle;
    }
}
	
