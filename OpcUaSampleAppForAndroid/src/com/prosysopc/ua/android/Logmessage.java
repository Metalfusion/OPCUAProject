package com.prosysopc.ua.android;

public class Logmessage
{
	private Integer timestamp;
	private LogmessageType type;
	private String message;
	
	public Logmessage( LogmessageType typevalue, String messagevalue)
	{
		type = typevalue;
		message = messagevalue;
		timestamp = (int) (System.currentTimeMillis());
	}
	
	
	public enum LogmessageType
	{
		WARNING,
		ERROR,
		INFO
	}
}