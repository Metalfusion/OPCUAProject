package com.prosysopc.ua.android;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import com.prosysopc.ua.android.Logmessage.LogmessageType;


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
		
		// TODO: server connection update/change
		if( connection == null )
		{
			connection = new Connection( newserver );
		}
		else
		{
			connection.disconnect();
			addLog(LogmessageType.INFO, "Disconnected from " + activeServer.getName() );
			connection = new Connection( newserver );
		}
		addLog(LogmessageType.INFO, "Connected to " + newserver.getName() );
		activeServer = newserver;
		
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
}