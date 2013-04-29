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
	
	
	public LogmessageType getType() {
		return type;
	}
	
	public Integer getTimestamp()
	{
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public enum LogmessageType
	{
		WARNING,
		ERROR,
		INFO;
		
		public String toString() {
			String s;
			switch(this){
				case WARNING:
					s = new String( "WARNING");
					break;
				case ERROR:
					s = new String( "ERROR");
					break;
				case INFO:
					s = new String( "INFO");
					break;
				default:
					s = new String( "" );
				break;
				
			}
			return s;
		}
	}
	
}