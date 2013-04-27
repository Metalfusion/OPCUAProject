package com.prosysopc.ua.android;

public class Server
{
	public String name;
	public String address;
	public String identity;
	public String password;
	public Integer timeout;
	
	public Server( String namevalue, String addressvalue, String identityvalue, 
					String passwordvalue, Integer timeoutvalue)
	{
		name = namevalue;
		address = addressvalue;
		identity = identityvalue;
		password = passwordvalue;
		timeout = timeoutvalue;
	}
	
	public String toString()
	{
		return name;
	}
	
}