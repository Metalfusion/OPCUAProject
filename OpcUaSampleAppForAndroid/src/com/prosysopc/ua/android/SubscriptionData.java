package com.prosysopc.ua.android;

import java.util.Date;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.prosysopc.ua.client.MonitoredDataItem;

public class SubscriptionData {
	
	private Server server;
	private String value;
	private NodeId nodeid;
	private Date receiveTime;
	private MonitoredDataItem dataItem;

	public SubscriptionData(Server server, NodeId nodeid, MonitoredDataItem item) {

		this.server = server;
		this.nodeid = nodeid;
		this.dataItem = item;
		value = "Value has not been set";
	}
	
	public Date getReceiveTime() {
		return receiveTime;
	}
	
	public String getValue() {
		return value;
	}

	public Server getServer() {
		return server;
	}

	public NodeId getNodeId() {
		return nodeid;
	}

	public void updateValue(String newValue) {
		value = newValue;
		receiveTime = new Date();
	}
	
	public MonitoredDataItem getDataItem() {		
		return dataItem;
	}
	
}