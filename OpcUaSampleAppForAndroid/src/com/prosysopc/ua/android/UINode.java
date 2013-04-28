package com.prosysopc.ua.android;

public class UINode {
	
	public int iconRes;
	public String name;
	public int nodeID;
	
	public UINode(){
	    super();
	}
	
	public UINode(int iconRes, String name, int nodeID) {
	    super();
	    this.iconRes = iconRes;
	    this.name = name;
	    this.nodeID = nodeID;
	}

}
