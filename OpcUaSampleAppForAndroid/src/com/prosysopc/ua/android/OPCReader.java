package com.prosysopc.ua.android;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;
import android.widget.Toast;
import com.prosysopc.ua.android.Logmessage.LogmessageType;

public class OPCReader
{
	List<Server> servers;
	List<Logmessage> messagelog;
	Server activeServer;
	
	// Connection settings
	Connection connection;
	
	List<SubscriptionData> subscriptions = new ArrayList<SubscriptionData>();

	private NodeId nodeidtobewritten;
	
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
			
			if (s.name.equals(newserver.name)) {
				
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
		
		if( newserver == null )
		{
			if( connection != null )
			{
				connection.disconnect();
				connection = null;
				addLog(LogmessageType.INFO, "Disconnected from server " + activeServer.getName());
				activeServer = null;
			}
		}
		
		else if( connection == null )
		{
			connection = new Connection( newserver, this );
			activeServer = newserver;
			addLog(LogmessageType.INFO, "Connected to server " + newserver.getName() );
			try {
				connection.connect();
			} catch (Exception e) {
				addLog(LogmessageType.WARNING, e.toString() );			
			}
		}
		else
		{
			connection.disconnect();
			addLog(LogmessageType.INFO, "Disconnected from " + activeServer.getName() );
			connection = new Connection( newserver, this);
			addLog(LogmessageType.INFO, "Connected to " + newserver.getName() );
			activeServer = newserver;
			
			try {
				connection.connect();
			} catch (Exception e) {
				addLog(LogmessageType.WARNING, e.toString() );			
			}
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
		// Timestamp is added on Logmessage constructor
		messagelog.add( new Logmessage( type, message) );
		
		try {
			if (type == LogmessageType.ERROR) {
				Toast.makeText(MainPager.pager.getBaseContext(), "Log: ERROR", Toast.LENGTH_SHORT).show();
			} else if (type == LogmessageType.WARNING) {
				Toast.makeText(MainPager.pager.getBaseContext(), "Log: WARNING", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {}
		
	}
	
	public List<Logmessage> getMessagelog()
	{
		return messagelog;
	}
	
	public void clearLog() {
		messagelog.clear();
	}
	
	public UINode getNode( NodeId nodeid ) 
	{
		UINode node = null;
		try {
			node = connection.getNode(nodeid, true);
		} catch (Exception e) {
			addLog(LogmessageType.ERROR, e.toString() );
		}
		
		return node;
	}
	
	public void writeAttributes(String value)
	{
		try {
			
			if( nodeidtobewritten != null )	{
				connection.writeAttribute(nodeidtobewritten, value);
			}
			
		} catch (Exception e) {
			
			addLog(LogmessageType.ERROR, e.toString() );		
		}
	}
	
	public void addSubscriptionData( SubscriptionData sd )
	{
		subscriptions.add(sd);
	}
	
	public List<SubscriptionData> getSubscriptionData()
	{
		return subscriptions;
	}
	
	public void subscribe( NodeId nodeid)
	{
		try {
			connection.subscribe(nodeid);
		} catch (Exception e) {
			
			addLog(LogmessageType.ERROR, e.toString() );
		}
	}
	
	public void updateSubscriptionValue(NodeId nodeid, String value )
	{
		// get the subscription to be updated from the list
		for( SubscriptionData sd : subscriptions)
		{
			if( sd.getNodeId() == nodeid)
			{
				// and update it
				sd.updateValue(value);
				break;
			}
		}
	}
	
	public void setNodeIdtoBeWritten( NodeId nodeid )
	{
		nodeidtobewritten = nodeid;
	}
	
}