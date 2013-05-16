package com.prosysopc.ua.android;



import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.utils.AttributesUtil;

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
	public UINode getNode(NodeId nodeID) throws ServiceException, AddressSpaceException, StatusException, ServiceResultException
	{
		UINode node = null;
		UINodeType type;
		String name;
		
		UaNode uanode = client.getAddressSpace().getNode(nodeID);
		
		name = uanode.getBrowseName().toString();
		
		
		if( uanode.getNodeClass() == NodeClass.Object )
		{
			// node is folder
			type = UINodeType.folderNode;
			node = new UINode(type, name, nodeID, getNodeChildren(nodeID));
		}
		else
		{
			// node is leaf
			type = UINodeType.leafNode;
			node = new UINode(type, name, nodeID);
		}

		
		// add attributes to node
		getNodeAttributes(node);
		return node;
	}
	
	public List<UINode> getNodeChildren(NodeId nodeID) throws ServiceException, AddressSpaceException, StatusException, ServiceResultException
	{
		List<UINode> list = new ArrayList<UINode>();
		List<ReferenceDescription> references = client.getAddressSpace().browse(nodeId); 
		
		for( ReferenceDescription reference : references)
		{
			// gets the nodeid from the reference and adds node by that id to the list
			list.add(getNode(client.getNamespaceTable().toNodeId(reference.getNodeId())));
			
		}
		
		return list;
	}
	
	
	public void getNodeAttributes( UINode uinode ) throws ServiceException, AddressSpaceException, StatusException
	{
		UaNode uanode = client.getAddressSpace().getNode(uinode.getNodeId());
		
		NodeAttributes attributes = uanode.getAttributes();

		for (long i = Attributes.NodeId.getValue(); i < Attributes.UserExecutable
				.getValue(); i++)
			 AttributesUtil.toString(UnsignedInteger
					.valueOf(i));
		
		uinode.addAttribute( attributes.getDisplayName().toString(), 
							attributes.getSpecifiedAttributes().toString() );
	}
	
}