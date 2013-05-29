package com.prosysopc.ua.android;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.common.NamespaceTable;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.core.MonitoringMode;
import org.opcfoundation.ua.core.NodeClass;
import org.opcfoundation.ua.core.ReferenceDescription;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.utils.AttributesUtil;

import android.widget.Toast;

import com.prosysopc.ua.DataTypeConverter;
import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.SessionActivationException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.UserIdentity;
import com.prosysopc.ua.android.Logmessage.LogmessageType;
import com.prosysopc.ua.android.UINode.UINodeType;
import com.prosysopc.ua.client.AddressSpace;
import com.prosysopc.ua.client.AddressSpaceException;
import com.prosysopc.ua.client.ConnectException;
import com.prosysopc.ua.client.InvalidServerEndpointException;
import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.MonitoredDataItemListener;
import com.prosysopc.ua.client.MonitoredItem;
import com.prosysopc.ua.client.Subscription;
import com.prosysopc.ua.client.SubscriptionNotificationListener;
import com.prosysopc.ua.client.UaClient;
import com.prosysopc.ua.nodes.UaDataType;
import com.prosysopc.ua.nodes.UaNode;
import com.prosysopc.ua.nodes.UaVariable;

public class Connection {

	static OPCReader opcreader;
	Server server;
	UaClient client;
	NodeId nodeId = Identifiers.RootFolder;
	AddressSpace addressspace;
	NamespaceTable namespacetable;
	MainPager mpager;

	Subscription subscription;
	SubscriptionNotificationListener subscriptionListener;
	MySubscriptionAliveListener subscriptionAliveListener;

	public Connection(Server server, OPCReader opcReader) throws URISyntaxException {

		this.server = server;
		opcreader = opcReader;
		subscriptionListener = new MySubscriptionNotificationListener(opcreader);
		subscriptionAliveListener = new MySubscriptionAliveListener(opcreader);
		client = new UaClient(server.getAddress());

	}

	public boolean connect() throws InvalidServerEndpointException, ConnectException, SessionActivationException, ServiceException, StatusException {

		client.setTimeout(server.getTimeout() * 1000);
		client.setSecurityMode(SecurityMode.NONE);

		UserIdentity ident;

		if (server.getIdentity().isEmpty() && server.getPassword().isEmpty()) {
			ident = new UserIdentity();
		} else {
			ident = new UserIdentity(server.getIdentity(), server.getPassword());
		}

		client.setUserIdentity(ident);
		client.connect();
		addressspace = client.getAddressSpace();
		namespacetable = client.getNamespaceTable();
		addressspace.getCache().setMaxQueueLength(20);
		addressspace.getCache().setNodeMaxAgeInMillis(server.getTimeout() * 1000 * 10);

		return true;
	}

	public boolean disconnect() {

		client.disconnect();

		return true;
	}

	public DataValue readNode(NodeId nodeId) throws ServiceException, StatusException {

		DataValue value = client.readValue(nodeId);
		return value;
	}

	// return UINode made from node of the nodeID
	public UINode getNode(NodeId nodeID, Boolean getChildren) throws ServiceException, AddressSpaceException, StatusException, ServiceResultException {

		Date nodeGetStart = new Date();

		UINode node = null;
		UINodeType type;
		String name;

		UaNode uanode = addressspace.getNode(nodeID);
		addressspace.getCache().addNode(uanode);		
				
		name = uanode.getDisplayName().getText();

		if (uanode.getNodeClass() == NodeClass.Object) {
			
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

		} else {
			// node is leaf
			type = UINodeType.leafNode;
			node = new UINode(type, name, nodeID);
			node.referencesSet = true;
		}

		// add attributes to node
		getNodeAttributes(uanode, node);
		node.attributesSet = true;

		if (getChildren == true) {
			opcreader.addLog(LogmessageType.INFO, "Getting the whole '" + node.name + "' node took " + (new Date().getTime() - nodeGetStart.getTime()) + " milliseconds");
		}

		return node;

	}

	// Get minimal info about the node's children without polling the server too much
	public List<UINode> getNodeChildren(UaNode node) throws ServiceException, AddressSpaceException, StatusException, ServiceResultException {

		// List for the child UINodes
		ArrayList<UINode> list = new ArrayList<UINode>();
		List<ReferenceDescription> references = addressspace.browse(node.getNodeId());

		// Loop through the references
		for (ReferenceDescription reference : references) {

			// Node class defines whether it is a folder or leaf
			UINodeType type = reference.getNodeClass() == NodeClass.Object ? UINodeType.folderNode : UINodeType.leafNode;

			// Get NodeId and display name
			NodeId id = namespacetable.toNodeId(reference.getNodeId());
			String name = reference.getDisplayName().getText();

			// Construct the UINode
			UINode newNode = new UINode(type, name, id);
			list.add(newNode);

		}

		// Sort the children nodes
		Collections.sort(list);

		return list;
	}

	// Reads all attributes of the given UaNode and saves them to the UINode
	public void getNodeAttributes(UaNode node, UINode uinode) {

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

			uinode.addAttribute(attrName, value);

		}

	}

	// writes the value of the node
	public void writeAttribute(NodeId nodeid, String value) throws ServiceException, StatusException, AddressSpaceException {

		// change the datatype to fit the node
		UaDataType dataType = null;
		UaVariable v = (UaVariable) addressspace.getNode(nodeid);
		// Initialize DataType node, if it is not initialized yet
		if (v.getDataType() == null)
			v.setDataType(client.getAddressSpace().getType(v.getDataTypeId()));
		dataType = (UaDataType) v.getDataType();

		Object convertedValue;
		if (dataType != null)
			convertedValue = ((DataTypeConverter) addressspace.getDataTypeConverter()).parseVariant(value, dataType);
		else
			convertedValue = value;

		// write the node
		client.writeValue(nodeid, convertedValue);
	}
	
	
	// Adds a data value subscription to the given node
	public void subscribe(NodeId nodeid) {

		// create new subscription if needed
		if (subscription == null) {

			try {
				subscription = createSubscription();
			} catch (Exception e) {
				opcreader.addLog(LogmessageType.ERROR, "Creating subscription failed" + "\n" + e.toString());
				return;
			}
		}

		// check if item is already monitored
		if (!subscription.hasItem(nodeid, Attributes.Value)) {
			
			// if not, create item to be monitored
			MonitoredDataItem item = new MonitoredDataItem(nodeid, Attributes.Value, MonitoringMode.Reporting);

			try {

				item.setSamplingInterval(-1);
				subscription.addItem(item);

			} catch (Exception e) {
				
				opcreader.addLog(LogmessageType.ERROR, "Creating subscription failed" + "\n" + e.toString());

				try {
					subscription.removeItem(item);
				} catch (Exception e1) {}

				return;
			}

			item.addChangeListener(dataChangeListener);

			// create new dataholder for subscription
			SubscriptionData subdata = new SubscriptionData(server, nodeid, item);
			
			try {
				
				UaNode node = addressspace.getNode(nodeid);
				
				// Read the value of the node
				DataValue val = node.readAttribute(Attributes.Value);
				String value = val.getValue().toString();				
				subdata.updateValue(value);
				
				
			} catch (Exception e) {}

			// add subscriptiondata for the ui
			opcreader.addSubscriptionData(subdata);

		} else {
			opcreader.addLog(LogmessageType.WARNING, "Error: Node is already subscribed to");
			return;
		}

		opcreader.addLog(LogmessageType.INFO, "Node value subscription added" + "\n" + "Node ID: " + nodeid.toString());
		Toast.makeText(MainPager.pager, "Subscription added", Toast.LENGTH_SHORT).show();

	}

	private Subscription createSubscription() throws ServiceException, StatusException {

		Subscription subs;

		// Create the subscription if it does not yet exist
		subs = new Subscription();

		// Default PublishingInterval is 1000 ms
		subs.setPublishingInterval(5000);
		subs.setPublishingEnabled(true);

		// LifetimeCount should be at least 3 times KeepAliveCount
		subs.setLifetimeCount(1000);
		subs.setMaxKeepAliveCount(50);

		// Listen to notifications - the data changes and events are
		// handled using the item listeners (see below), but in many
		// occasions, it may be best to use the subscription
		// listener also to handle those notifications
		subs.addNotificationListener(subscriptionListener);
		subs.addAliveListener(subscriptionAliveListener);

		// Add it to the client, if it wasn't there already
		if (!client.hasSubscription(subs.getSubscriptionId())) {

			client.addSubscription(subs);
		}

		return subs;

	}

	private static MonitoredDataItemListener dataChangeListener = new MonitoredDataItemListener() {

		@Override
		public void onDataChange(MonitoredDataItem sender, DataValue prevValue, DataValue value) {
			
			MonitoredItem i = sender;			
			NodeId nodeid = i.getNodeId();
			opcreader.updateSubscriptionValue(nodeid, value.getValue().toString());
		}

	};

	public void removeSubscription(MonitoredDataItem item) {
		
		try {
			subscription.removeItem(item);
		} catch (Exception e) {
			opcreader.addLog(LogmessageType.ERROR, "Removing subscription failed" + "\n" + e.toString());
		}
	}

}
