package com.prosysopc.ua.android;

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

	
	public OPCReader() {
		
		servers = new ArrayList<Server>();
		messagelog = new ArrayList<Logmessage>();
		activeServer = null;
		
	}
	
	// Adds a new server to server list, or updates an existing one.
	public void addModifyServer( String name, String address, String identity, String password, Integer timeout )
	{
		addModifyServer(new Server(name,address,identity,password,timeout));
	}

	
	// Adds a new server to server list, or updates an existing one.	
	private void addModifyServer( Server newserver )
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
	private void updateConnection( Server newserver) {
		
		// TODO: server connection update/change
		
		activeServer = newserver;
		
	}

	public List<Server> getServers()
	{
		return servers;
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