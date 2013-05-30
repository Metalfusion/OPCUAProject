package com.prosysopc.ua.android;

import java.util.Calendar;

import com.prosysopc.ua.android.Logmessage.LogmessageType;
import com.prosysopc.ua.client.Subscription;
import com.prosysopc.ua.client.SubscriptionAliveListener;

/**
 * A sampler listener for subscription alive events.
 */
public class MySubscriptionAliveListener implements SubscriptionAliveListener {

	private OPCReader opcreader;

	public MySubscriptionAliveListener(OPCReader opcreader) {

		this.opcreader = opcreader;
	}

	@Override
	public void onAlive(Subscription s) {

		// the client acknowledged that the
		// connection is alive,
		// although there were no changes to send

		opcreader.addLog(LogmessageType.INFO, String.format("%tc Subscription alive: ID=%d lastAlive=%tc", Calendar.getInstance(), s.getSubscriptionId().getValue(), s.getLastAlive()));

	}

	@Override
	public void onTimeout(Subscription s) {

		// the client did not acknowledged that the
		// connection is alive,
		// and the maxKeepAliveCount has been
		// exceeded
		opcreader.addLog(LogmessageType.INFO, String.format("%tc Subscription timeout: ID=%d lastAlive=%tc", Calendar.getInstance(), s.getSubscriptionId().getValue(), s.getLastAlive()));

	}

};