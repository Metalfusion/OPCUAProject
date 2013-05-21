package com.prosysopc.ua.android;



import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReadResponse;
import org.opcfoundation.ua.core.ReadValueId;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.core.TimestampsToReturn;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.utils.AttributesUtil;

import android.os.StrictMode;

import com.prosysopc.ua.DataTypeConverter;
import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.SessionActivationException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.UaApplication.a;
import com.prosysopc.ua.android.Logmessage.LogmessageType;
import com.prosysopc.ua.android.UINode.AttributeValuePair;
import com.prosysopc.ua.android.UINode.UINodeType;
import com.prosysopc.ua.client.AddressSpace;
import com.prosysopc.ua.client.AddressSpaceException;
import com.prosysopc.ua.client.ConnectException;
import com.prosysopc.ua.client.InvalidServerEndpointException;
import com.prosysopc.ua.client.UaClient;
import com.prosysopc.ua.nodes.UaDataType;
import com.prosysopc.ua.nodes.UaNode;
import com.prosysopc.ua.nodes.UaReference;
import com.prosysopc.ua.nodes.UaVariable;



public class Connection
{
	
	
	Server server;
	public UaClient client;
	NodeId nodeId = Identifiers.RootFolder;
	AddressSpace addressspace;
	NamespaceTable namespacetable;
	
	public Connection( Server servervalue ) throws URISyntaxException
	{
		server = servervalue;
		client = new UaClient( server.address );
	}
	
	public boolean connect() throws InvalidServerEndpointException, ConnectException, SessionActivationException, ServiceException, StatusException
	{
		//TODO: rest of server settings, if needed
		client.setTimeout(server.getTimeout()*1000);
		client.setSecurityMode(SecurityMode.NONE);
		client.connect();
		addressspace = client.getAddressSpace();
		namespacetable = client.getNamespaceTable();
		
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
	public UINode getNode(NodeId nodeID, Boolean getChildren) throws ServiceException, AddressSpaceException, StatusException, ServiceResultException
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
						
			List<UINode> children = null;
			
			if (getChildren) {
				children = getNodeChildren(nodeID);
			}
			
			node = new UINode(type, name, nodeID, children);
			
			if (children != null) {
				node.referencesSet = true;
			}
			
		}
		else
		{
			// node is leaf
			type = UINodeType.leafNode;
			node = new UINode(type, name, nodeID);
			node.referencesSet = true;
		}
		
		// add attributes to node
		getNodeAttributes(node);
		node.attributesSet = true;
		return node;
	}
	
	public List<UINode> getNodeChildren(NodeId nodeID) throws ServiceException, AddressSpaceException, StatusException, ServiceResultException
	{
		List<UINode> list = new ArrayList<UINode>();
		List<ReferenceDescription> references = addressspace.browse(nodeID); 
		
		for( ReferenceDescription reference : references)
		{
			// gets the nodeid from the reference and adds node by that id to the list
			list.add(getNode(namespacetable.toNodeId(reference.getNodeId()), false ));
			
		}
		
		return list;
	}
	
	
	public void getNodeAttributes( UINode uinode ) throws ServiceException, AddressSpaceException, StatusException
	{
		ReadValueId nodesToRead = new ReadValueId( uinode.getNodeId(), Attributes.Value, null, null);
		ReadResponse rr = client.read(UaClient.MAX_CACHE_AGE,TimestampsToReturn.Both, nodesToRead);
		DataValue[] value = rr.getResults();
		
		
		
		uinode.addAttribute(value[0].toString(), value[0].getValue().toString());
		
		
	}
	
	// writes the value of the node
	public void writeAttribute( NodeId nodeid, String value ) throws ServiceException, StatusException
	{
		
		client.writeValue(nodeid, new Variant((Object)value) );
	}
	
}