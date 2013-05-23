package com.prosysopc.ua.android;



import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DiagnosticInfo;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.NodeAttributes;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.NotificationData;
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
import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.MonitoredDataItemListener;
import com.prosysopc.ua.client.MonitoredEventItem;
import com.prosysopc.ua.client.MonitoredItem;
import com.prosysopc.ua.client.Subscription;
import com.prosysopc.ua.client.SubscriptionNotificationListener;
import com.prosysopc.ua.client.UaClient;
import com.prosysopc.ua.nodes.UaDataType;
import com.prosysopc.ua.nodes.UaNode;
import com.prosysopc.ua.nodes.UaReference;
import com.prosysopc.ua.nodes.UaVariable;


public class Connection
{
	
	static OPCReader opcreader;
	Server server;
	UaClient client;
	NodeId nodeId = Identifiers.RootFolder;
	AddressSpace addressspace;
	NamespaceTable namespacetable;
	
	Subscription subscription;
	
	public Connection( Server server, OPCReader opcreader ) throws URISyntaxException
	{
		this.server = server;
		this.opcreader = opcreader;
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
		
		Date nodeGetStart = new Date();
		
		UINode node = null;
		UINodeType type;
		String name;
		
		UaNode uanode = client.getAddressSpace().getNode(nodeID);		
		name = uanode.getDisplayName().getText();
		
		
		if( uanode.getNodeClass() == NodeClass.Object )
		{
			// node is folder
			type = UINodeType.folderNode;
						
			List<UINode> children = null;
			
			if (getChildren) {				
				children = getNodeChildren(uanode);				
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
		getNodeAttributes(uanode,node);
		node.attributesSet = true;
				
		if (getChildren == true) {
			opcreader.addLog(LogmessageType.INFO,"Getting the whole '" + node.name +  "' node took: " + (new Date().getTime()-nodeGetStart.getTime()) + " milliseconds");
		}
		
		return node;
		
	}
	
	// Get minimal info about the node's children without polling the server too much
	public List<UINode> getNodeChildren(UaNode node) throws ServiceException, AddressSpaceException, StatusException, ServiceResultException
	{
		// List for the child UINodes
		List<UINode> list = new ArrayList<UINode>();
		List<ReferenceDescription> references = addressspace.browse(node.getNodeId());

		// Loop through the references
		for( ReferenceDescription reference : references)
		{
			
			// Node class defines whether it is a folder or leaf
			UINodeType type = reference.getNodeClass() == NodeClass.Object ? UINodeType.folderNode : UINodeType.leafNode;
			
			// Get NodeId and display name
			NodeId id = namespacetable.toNodeId(reference.getNodeId());			
			String name = reference.getDisplayName().getText();
			
			// Construct the UINode
			UINode newNode = new UINode( type ,name, id);			
			list.add(newNode);
			
		}
		
		return list;
	}
	
	// Reads all attributes of the given UaNode and saves them to the UINode
	public void getNodeAttributes( UaNode node, UINode uinode ) 
	{
		
		// Get all attribute types supported by this node
		UnsignedInteger attr[] = node.getSupportedAttributes();
		
		// Loop through the attributes
		for (int i = 0; i < attr.length; i++) {
			
			String attrName = "Unknown attribute";
			
			// Find the name of the node attribute
			for (long i2 = Attributes.NodeId.getValue(); i2 < Attributes.UserExecutable.getValue(); i2++) {
				
				if (attr[i].getValue() == i2) {
					attrName = AttributesUtil.toString(UnsignedInteger.valueOf(i2));
					break;
				}
			}
			
			// Read the value of the attribute
			DataValue val = node.readAttribute(attr[i]);			
			String value = val.getValue().toString();
			
			uinode.addAttribute(attrName,value);
			
		}
		
	}
	
	        
	// Writes the value-attribute of the given node
	public void writeAttribute( NodeId nodeid, String value ) throws ServiceException, StatusException, AddressSpaceException
	{
		// change the datatype to fit the node
		UaDataType dataType = null;
		UaVariable v = (UaVariable) addressspace.getNode(nodeid);
		// Initialize DataType node, if it is not initialized yet
		if (v.getDataType() == null)
			v.setDataType(client.getAddressSpace().getType(
					v.getDataTypeId()));
		dataType = (UaDataType) v.getDataType();
			
		Object convertedValue;
		if (dataType != null)
			convertedValue = ((DataTypeConverter) addressspace.getDataTypeConverter()).parseVariant(value, dataType);
		else
			convertedValue = value;
		
		// write the node
		client.writeValue(nodeid, convertedValue );
	}
	
	public void subscribe( NodeId nodeid ) throws ServiceException, StatusException
	{
		// create new subscription if needed
		if (subscription == null) {
			subscription = new Subscription();
		}
		
		// check if item is already monitored
		if(!subscription.hasItem(nodeId, Attributes.Value)) {
			// if not, create item to be monitored
			MonitoredDataItem item = new MonitoredDataItem(nodeId,Attributes.Value,MonitoringMode.Reporting);
			item.addChangeListener(dataChangeListener);
			subscription.addItem(item);
		}
		
		//Add subscription to the client, if it wasn't there already
		if (!client.hasSubscription(subscription.getSubscriptionId())) {
			
			// activate subscription
			subscription.setPublishingEnabled(new Boolean("true"));
			
			client.addSubscription(subscription);
			
			// create new dataholder for subscription
			SubscriptionData subdata = new SubscriptionData(server, nodeid);
			// add subscriptiondata for the ui
			opcreader.addSubscriptionData(subdata);
			
		}		
		
	}
	
	// listener for datachanges
	protected static MonitoredDataItemListener dataChangeListener = new MonitoredDataItemListener() {
		@Override
		public void onDataChange(MonitoredDataItem sender, DataValue prevValue,
				DataValue value) {
			MonitoredItem i = sender;
			
			// on change update the value of the subscriptiondata-list
			NodeId nodeid = i.getNodeId();
			opcreader.updateSubscriptionValue(nodeid, value.getValue().toString());
			
		//	println(dataValueToString(i.getNodeId(), i.getAttributeId(), value));
		}
	};

	
}