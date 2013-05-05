package com.prosysopc.ua.android;



import java.net.URISyntaxException;

import org.opcfoundation.ua.transport.security.SecurityMode;

import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.SessionActivationException;
import com.prosysopc.ua.client.ConnectException;
import com.prosysopc.ua.client.InvalidServerEndpointException;
import com.prosysopc.ua.client.UaClient;



public class Connection
{
	Server server;
	protected static UaClient client;
	
	public Connection( Server servervalue ) throws URISyntaxException
	{
		server = servervalue;
		client = new UaClient( server.address );
	}
	
	public boolean connect() throws InvalidServerEndpointException, ConnectException, SessionActivationException, ServiceException
	{
		client.setTimeout(server.getTimeout());
		client.setSecurityMode(SecurityMode.NONE);
		client.connect();
		
		return true;
	}
	
	public boolean disconnect()
	{
		client.disconnect();
		return true;
	}
}