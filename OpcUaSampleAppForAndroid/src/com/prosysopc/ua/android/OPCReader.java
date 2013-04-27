package com.prosysopc.ua.android;

import java.util.List;

public class OPCReader
{
	List<Server> servers;
	// settings
	// connectionsettings
	
	// adds new server to server list
	public void addServer( Server newserver )
	{
		servers.add(newserver);
	}

	public List<Server> getServers()
	{
		return servers;
	}
}