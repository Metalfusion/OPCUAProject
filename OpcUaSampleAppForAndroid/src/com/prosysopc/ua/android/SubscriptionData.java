package com.prosysopc.ua.android;

import org.opcfoundation.ua.builtintypes.NodeId;

public class SubscriptionData
{
	private Server server;
	private String value;
	private NodeId nodeid;
	
	public SubscriptionData( Server server, NodeId nodeid )
	{
		this.server = server;
		this.nodeid = nodeid;
		value = "0";
	}
	
	public String getValue()
	{
		return value;
	}
	
	public Server getServer()
	{
		return server;
	}
	
	public NodeId getNodeId()
	{
		return nodeid;
	}
	
	public void updateValue( String newValue )
	{
		value = newValue;
	}
}