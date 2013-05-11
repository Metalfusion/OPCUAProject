package com.prosysopc.ua.android;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;

import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.android.Logmessage.LogmessageType;
import com.prosysopc.ua.android.UINode.AttributeValuePair;
import com.prosysopc.ua.android.UINode.UINodeType;
import com.prosysopc.ua.client.AddressSpaceException;
import com.prosysopc.ua.nodes.UaInstance;
import com.prosysopc.ua.nodes.UaNode;
import com.prosysopc.ua.nodes.UaReference;


public class OPCReader
{
	List<Server> servers;
	List<Logmessage> messagelog;
	Server activeServer;
	// settings
	// connectionsettings
	Connection connection;

	
	public OPCReader() {
		
		servers = new ArrayList<Server>();
		messagelog = new ArrayList<Logmessage>();
		activeServer = null;
		
	}
	
	// Adds a new server to server list, or updates an existing one.
	public void addModifyServer( String name, String address, String identity, String password, Integer timeout ) throws URISyntaxException
	{
		addModifyServer(new Server(name,address,identity,password,timeout));
	}

	
	// Adds a new server to server list, or updates an existing one.	
	private void addModifyServer( Server newserver ) throws URISyntaxException
	{
		boolean serverfound = false;
		
		for (Server s : servers) {
			
			if (s.name == newserver.name) {
				
				if (s == activeServer) {
					updateConnection(newserver);
				}
				
				servers.set(servers.indexOf(s), newserver);
				serverfound = true;				
				break;
			}
			
		}
		
		if (!serverfound) {
			servers.add(newserver);
		}
		
	}
	
	// Updates the connection within the prosys framework
	public void updateConnection( Server newserver) throws URISyntaxException {
		
		if( connection == null )
		{
			connection = new Connection( newserver );
			activeServer = newserver;
		}
		else
		{
			connection.disconnect();
			addLog(LogmessageType.INFO, "Disconnected from " + activeServer.getName() );
			connection = new Connection( newserver );
			addLog(LogmessageType.INFO, "Connected to " + newserver.getName() );
			activeServer = newserver;
		}
		
		
	}

	public List<Server> getServers()
	{
		return servers;
	}
	
	// returns i:th server from serverlist
	public Server getServer( int i )
	{
		return servers.get(i);
	}
	
	public boolean removeServer( int i )
	{
		if( i > servers.size())
		{
			return false;
		}
		
		servers.remove(i);
		return true;
	}
	
	// adds new message to log
	public void addLog( LogmessageType type, String message )
	{
		// timestamp is added on Logmessage constructor
		messagelog.add( new Logmessage( type, message) );
	}
	
	public List<Logmessage> getMessagelog()
	{
		return messagelog;
	}
	
	// return UINode made from node of the nodeID
	public UINode getNode(NodeId nodeID)
	{
		UINode node = null;
		UINodeType type;
		String name;
		List<UINode> childNodes; 
		List<AttributeValuePair> attributes;
		
		try {
			UaNode uanode = connection.client.getAddressSpace().getNode(nodeID);
			
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
			
			
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			addLog(LogmessageType.WARNING, e.toString() );
		} catch (AddressSpaceException e) {
			// TODO Auto-generated catch block
			addLog(LogmessageType.WARNING, e.toString() );
		} catch (StatusException e) {
			// TODO Auto-generated catch block
			addLog(LogmessageType.WARNING, e.toString() );
		}
		
		
		return node;
	}
	
	public List<UINode> getNodeChildren(NodeId nodeID)
	{
		List<UINode> list = new ArrayList<UINode>();
		UaNode uanode = null;
		try {
			uanode = connection.client.getAddressSpace().getNode(nodeID);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			addLog(LogmessageType.WARNING, e.toString() );
		} catch (AddressSpaceException e) {
			// TODO Auto-generated catch block
			addLog(LogmessageType.WARNING, e.toString() );
		} catch (StatusException e) {
			// TODO Auto-generated catch block
			addLog(LogmessageType.WARNING, e.toString() );
		}
		UaReference[] references = uanode.getReferences();
		for( UaReference reference : references)
		{
			UaNode node = reference.getTargetNode();
			list.add(getNode(node.getNodeId()));
		}
		return list;
	}
	
}