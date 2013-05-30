package com.prosysopc.ua.android;

import java.util.Date;

// A container class for log messages
public class Logmessage {
	private Long timestamp;
	private LogmessageType type;
	private String message;

	// Create a new log message. The message is timestamped automatically
	public Logmessage(LogmessageType typevalue, String messagevalue) {

		type = typevalue;
		message = messagevalue;
		timestamp = (System.currentTimeMillis());
	}

	public LogmessageType getType() {

		return type;
	}

	public Long getTimestamp() {

		return timestamp;
	}

	public String getMessage() {

		return message;
	}
	
	// Enum for the log message types which note the severity
	public enum LogmessageType {
		WARNING, ERROR, INFO;
		
		// String representations for the items
		public String toString() {

			String s;
			switch (this) {
			case WARNING:
				s = new String("WARNING");
				break;
			case ERROR:
				s = new String("ERROR");
				break;
			case INFO:
				s = new String("INFO");
				break;
			default:
				s = new String("");
				break;
			}
			
			return s;
		}
	}
	
	// Get the timestamp as a string
	public String getTimestampString() {

		Date date = new Date(timestamp);
		return date.toString();
	}

}