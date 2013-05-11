package com.prosysopc.ua.android;



import java.net.URISyntaxException;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.transport.security.SecurityMode;

import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.SessionActivationException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.client.ConnectException;
import com.prosysopc.ua.client.InvalidServerEndpointException;
import com.prosysopc.ua.client.UaClient;



public class Connection
{
	Server server;
	public UaClient client;
	NodeId nodeId = Identifiers.RootFolder;
	
	public Connection( Server servervalue ) throws URISyntaxException
	{
		server = servervalue;
		client = new UaClient( server.address );
	}
	
	public boolean connect() throws InvalidServerEndpointException, ConnectException, SessionActivationException, ServiceException
	{
		//TODO: rest of server settings, if needed
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
	
	public DataValue readNode(NodeId nodeId ) throws ServiceException, StatusException
	{
		DataValue value = client.readValue(nodeId);
		return value;
	}
}