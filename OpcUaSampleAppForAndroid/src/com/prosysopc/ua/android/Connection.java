package com.prosysopc.ua.android;



import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.transport.security.SecurityMode;

import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.SessionActivationException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.android.Logmessage.LogmessageType;
import com.prosysopc.ua.android.UINode.AttributeValuePair;
import com.prosysopc.ua.android.UINode.UINodeType;
import com.prosysopc.ua.client.AddressSpaceException;
import com.prosysopc.ua.client.ConnectException;
import com.prosysopc.ua.client.InvalidServerEndpointException;
import com.prosysopc.ua.client.UaClient;
import com.prosysopc.ua.nodes.UaNode;
import com.prosysopc.ua.nodes.UaReference;



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
	
	// return UINode made from node of the nodeID
	public UINode getNode(NodeId nodeID) throws ServiceException, AddressSpaceException, StatusException
	{
		UINode node = null;
		UINodeType type;
		String name;
		
		UaNode uanode = client.getAddressSpace().getNode(nodeID);
		
		name = uanode.getBrowseName().toString();
		
		// check whether node is folder or leaf
		UaReference[] references = uanode.getReferences();
		
		if (references.length == 1 ) 
		{
			// leaf
			type = UINodeType.leafNode;
			node = new UINode(type, name, nodeID);
		}
		else
		{
			// folder
			type = UINodeType.folderNode;
			node = new UINode(type, name, nodeID);
		}
		
		return node;
	}
	
	public List<UINode> getNodeChildren(NodeId nodeID) throws ServiceException, AddressSpaceException, StatusException
	{
		List<UINode> list = new ArrayList<UINode>();
		UaNode uanode = null;
		uanode = client.getAddressSpace().getNode(nodeID);
		
		UaReference[] references = uanode.getReferences();
		for( UaReference reference : references)
		{
			UaNode node = reference.getTargetNode();
			list.add(getNode(node.getNodeId()));
		}
		return list;
	}
	
	public List<AttributeValuePair> getNodeAttributes( NodeId nodeID)
	{
		List<AttributeValuePair> list = new ArrayList<AttributeValuePair>();
		UaNode uanode = null;
		uanode = client.getAddressSpace().getNode(nodeID);
		
		UaReference[] references = uanode.getReferences();
		for( UaReference reference : references)
		{
			UaNode node = reference.getTargetNode();
			list.add();
		}
		return list;
	}
	
}