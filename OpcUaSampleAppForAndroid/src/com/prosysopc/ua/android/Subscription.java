package com.prosysopc.ua.android;

public class Subscription
{
	private Server server;
	private String nodeID;
	private String name;
	private String value;
	private String AttributeID;
	
	public Subscription( Server servervalue, String nodeIDvalue, String namevalue )
	{
		server = servervalue;
		nodeID = nodeIDvalue;
		name = namevalue;
		value = "0";
		AttributeID = "0";
	}
}