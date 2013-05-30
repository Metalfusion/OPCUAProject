package com.prosysopc.ua.android;

import java.io.Serializable;

// A data holder class for server settings. 
// Note that this class doesn't expose any mutators and so the objects can't be modified after creation.
public class Server implements Serializable {
	
	private static final long serialVersionUID = 0L;
	
	private String name;
	private String address;
	private String identity;	// User name
	private String password;	// User password
	private Integer timeout;	// Timeout in seconds

	public Server(String namevalue, String addressvalue, String identityvalue, String passwordvalue, Integer timeoutvalue) {

		name = namevalue;
		address = addressvalue;
		identity = identityvalue;
		password = passwordvalue;
		timeout = timeoutvalue;
	}

	public String getName() {

		return name;
	}

	public String getAddress() {

		return address;
	}

	public String getIdentity() {

		return identity;
	}

	public String getPassword() {

		return password;
	}

	public Integer getTimeout() {

		return timeout;
	}

}