
package com.prosysopc.ua.android;

import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DiagnosticInfo;
import org.opcfoundation.ua.builtintypes.ExtensionObject;
import org.opcfoundation.ua.builtintypes.StatusCode;
import org.opcfoundation.ua.builtintypes.UnsignedInteger;
import org.opcfoundation.ua.builtintypes.Variant;
import org.opcfoundation.ua.core.NotificationData;

import com.prosysopc.ua.android.Logmessage.LogmessageType;
import com.prosysopc.ua.client.MonitoredDataItem;
import com.prosysopc.ua.client.MonitoredEventItem;
import com.prosysopc.ua.client.Subscription;
import com.prosysopc.ua.client.SubscriptionNotificationListener;

/**
 * A listener for subscription notifications.
 */
public class MySubscriptionNotificationListener implements	SubscriptionNotificationListener {
	
	private OPCReader opcreader;
	
	public MySubscriptionNotificationListener(OPCReader reader) {
		opcreader = reader;
	}
	
	@Override
	public void onBufferOverflow(Subscription subscription,	UnsignedInteger sequenceNumber, ExtensionObject[] notificationData) {
		opcreader.addLog(LogmessageType.ERROR, "SUBCRIPTION BUFFER OVERFLOW");
	}

	@Override
	public void onDataChange(Subscription subscription, MonitoredDataItem item,
			DataValue newValue) {
		// Called for each data change notification
	}

	@Override
	public void onError(Subscription subscription, Object notification,	Exception exception) {
		// Called if the parsing of the notification data fails,
		// notification is either a MonitoredItemNotification or
		// an EventList
		opcreader.addLog(LogmessageType.ERROR, "Subscription error: " + "\n" + exception.toString());
	}

	@Override
	public void onEvent(Subscription subscription, MonitoredEventItem item,	Variant[] eventFields) {
		// Called for each event notification
	}

	@Override
	public long onMissingData(UnsignedInteger lastSequenceNumber, long sequenceNumber, long newSequenceNumber, StatusCode serviceResult) {

		// Called if a data packet is missed due to communication errors and failing Republish
		opcreader.addLog(LogmessageType.WARNING, "Subscription error: " + "\n" + "Data missed: lastSequenceNumber=" + lastSequenceNumber + " newSequenceNumber=" + newSequenceNumber);
		return newSequenceNumber; // Accept the default
	}

	@Override
	public void onNotificationData(Subscription subscription, NotificationData notification) {
		
		// Called after a complete notification data package is handled
		// if (notification instanceof DataChangeNotification) {
		// DataChangeNotification d = (DataChangeNotification) notification;
		// println("onNotificationData: " + d.getMonitoredItems().length);
		// }

	}

	@Override
	public void onStatusChange(Subscription subscription, StatusCode oldStatus,	StatusCode newStatus, DiagnosticInfo diagnosticInfo) {
		
		// Called when the subscription status has changed in the server		
		opcreader.addLog(LogmessageType.INFO, "Subscription status change: " + "\n" + "Old status: " + oldStatus + "\n" + "New status: " + newStatus + "\n" + "Diagnostic info: " + diagnosticInfo );
	}

};
