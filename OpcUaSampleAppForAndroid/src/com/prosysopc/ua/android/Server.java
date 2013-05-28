package com.prosysopc.ua.android;

import java.io.Serializable;

public class Server implements Serializable {
	
	private static final long serialVersionUID = 0L;
	
	private String name;
	private String address;
	private String identity;
	private String password;
	private Integer timeout;

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