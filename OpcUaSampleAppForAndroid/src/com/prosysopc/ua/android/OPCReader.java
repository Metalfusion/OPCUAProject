package com.prosysopc.ua.android;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class OPCReader
{
	List<Server> servers;
	Server activeServer;
	// settings
	// connectionsettings

	
	public OPCReader() {
		
		servers = new ArrayList<Server>();
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
}