package com.prosysopc.ua.android;

public class Server
{
	private String name;
	private String address;
	private String identity;
	private String password;
	private Integer timeout;
	
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