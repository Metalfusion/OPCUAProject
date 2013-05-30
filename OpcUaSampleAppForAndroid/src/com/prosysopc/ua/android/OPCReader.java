package com.prosysopc.ua.android;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.opcfoundation.ua.builtintypes.NodeId;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

import com.prosysopc.ua.android.Logmessage.LogmessageType;
import com.prosysopc.ua.client.MonitoredDataItem;

// Class having the main logic of the application and handling the data resources
public class OPCReader {
		 
	List<SubscriptionData> subscriptions = new ArrayList<SubscriptionData>();	// Node subscriptions
	List<Server> servers;			// Server settings
	List<Logmessage> messagelog;	// Log messages
	Server activeServer;			// Server onto which we are connected

	// Connection settings holder and handler
	Connection connection;

	// Temporary variable for storing the nodeID during value write operations 
	private NodeId nodeidtobewritten;

	public OPCReader() {

		servers = new ArrayList<Server>();
		messagelog = new ArrayList<Logmessage>();
		activeServer = null;
		
		Handler handler = new Handler();
		
		// The MainPager's functions can't be used when it is still being constructed so we must delay the loading of servers 
		handler.postDelayed(new Runnable() {

		    public void run() {
		    	
		    	loadServers("Servers");
		
		    	if (servers.isEmpty()) {
				
		    		try {
						addModifyServer("Ascolab public demoserver", "opc.tcp://demo.ascolab.com:4841", "", "", 20);
					} catch (URISyntaxException e) {
						e.printStackTrace();
					}
				}		    	
		    }
		}, 100); // 100ms delay to allow the MainPager to settle first
		
	}

	// Adds a new server to server list, or updates an existing one.
	public void addModifyServer(String name, String address, String identity, String password, Integer timeout) throws URISyntaxException {

		addModifyServer(new Server(name, address, identity, password, timeout));
	}

	// Adds a new server to server list, or updates an existing one.
	private void addModifyServer(Server newserver) throws URISyntaxException {

		boolean serverfound = false;

		for (Server s : servers) {

			if (s.getName().equals(newserver.getName())) {

				if (s == activeServer) {
					updateConnection(newserver);
				}

				servers.set(servers.indexOf(s), newserver);
				serverfound = true;
				break;
			}

		}

		if (!serverfound) {
			servers.add(newserver);
		}

	}

	// Updates the connection within the prosys framework
	public void updateConnection(Server newserver) throws URISyntaxException {
		
		// Disconnecting
		if (newserver == null) {

			if (connection != null) {
				
				connection.disconnect();
				removeSubscriptions();				
				connection = null;
				
				addLog(LogmessageType.INFO, "Disconnected from server " + activeServer.getName());
				activeServer = null;
			}

		} else if (connection == null) {	// Connecting

			connection = new Connection(newserver, this);
			activeServer = newserver;
			addLog(LogmessageType.INFO, "Connected to server " + newserver.getName() + "\n" + newserver.getAddress());
			try {
				connection.connect();
			} catch (Exception e) {
				addLog(LogmessageType.ERROR, e.toString());
			}

		} else {	// Disconnecting and connecting

			connection.disconnect();
			removeSubscriptions();
						
			addLog(LogmessageType.INFO, "Disconnected from " + activeServer.getName());
			
			connection = new Connection(newserver, this);
			addLog(LogmessageType.INFO, "Connected to server " + newserver.getName() + "\n" + newserver.getAddress());
			activeServer = newserver;

			try {
				connection.connect();
			} catch (Exception e) {
				addLog(LogmessageType.ERROR, e.toString());
			}

		}

	}
	
	// Returns the list of servers
	public List<Server> getServers() {

		return servers;
	}

	// Returns i:th server from serverlist
	public Server getServer(int i) {

		return servers.get(i);
	}
	
	// Removes a server from the internal list
	public boolean removeServer(int i) {

		if (i > servers.size()) {
			return false;
		}

		servers.remove(i);
		return true;
	}

	// Adds a new message to the log
	public void addLog(LogmessageType type, String message) {
		
		// This method might be called from anywhere, but the code needs to be run in the UI Thread to avoid some problems.
		
		// Create a special runner class to do the message addition
		class logMessageRunner implements Runnable {
	        
			LogmessageType mType;
			String msg;
	        
			public logMessageRunner(LogmessageType type1, String message1) {
				
				mType = type1;
				msg = message1;
			
			}	
			
	        public void run() {
	        	
	        	// Timestamp is added on Logmessage constructor
				messagelog.add(new Logmessage(mType, msg));

				try {
					if (mType == LogmessageType.ERROR) {
						Toast.makeText(MainPager.pager.getBaseContext(), "Log: ERROR", Toast.LENGTH_SHORT).show();
					} else if (mType == LogmessageType.WARNING) {
						Toast.makeText(MainPager.pager.getBaseContext(), "Log: WARNING", Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {}
				
	        }
	    }
		
		// Run on UI thread
		MainPager.pager.runOnUiThread(new logMessageRunner(type, message));		
		
	}

	// Returns all log messages
	public List<Logmessage> getMessagelog() {

		return messagelog;
	}

	// Clears all log messages
	public void clearLog() {

		messagelog.clear();
	}

	// Gets a node from the server and converts it to UINode
	// Returns null if the operation fails
	public UINode getNode(NodeId nodeid) {

		UINode node = null;
		
		// Get the node from Connection
		try {
			node = connection.getNode(nodeid, true);
		} catch (Exception e) {
			addLog(LogmessageType.ERROR, e.toString());
		}

		return node;
	}
	
	// Presets the nodeId of the node the will have its value written with writeAttributes
	public void setNodeIdtoBeWritten(NodeId nodeid) {

		nodeidtobewritten = nodeid;
	}
	
	// Writes the value attribute of the predefined Node to the server
	public void writeAttributes(String value) {
		
		try {

			if (nodeidtobewritten != null) {
				connection.writeAttribute(nodeidtobewritten, value);
			}

		} catch (Exception e) {

			addLog(LogmessageType.ERROR, e.toString());
		}
	}
	
	// Adds the subscription data
	public void addSubscriptionData(SubscriptionData sd) {

		subscriptions.add(sd);
	}
	
	// Gets all subscriptions
	public List<SubscriptionData> getSubscriptionData() {

		return subscriptions;
	}

	// Adds subscription to the given nodeId's value attribute
	public void subscribe(NodeId nodeid) {

		try {
			connection.subscribe(nodeid);
		} catch (Exception e) {
			addLog(LogmessageType.ERROR, e.toString());
		}
		
		MainPager.pager.updateSubscriptionView();

	}
	
	// Removes all subscriptions
	private void removeSubscriptions() {
		
		try {
			connection.client.removeSubscription(connection.subscription);
		} catch (Exception e) {}
		
		subscriptions.clear();
		MainPager.pager.updateSubscriptionView();
	}
	
	// Removes a subscription from OPCReader and server
	public void removeSubscription(MonitoredDataItem item) {
		
		connection.removeSubscription(item);
		
		NodeId removedId = null;
		
		// Find the correct SubscriptionData
		for (int i = 0; i < subscriptions.size(); i++) {
			
			SubscriptionData subData = subscriptions.get(i);
			
			if (subData.getDataItem() == item) {
				
				subscriptions.remove(i);
				removedId = subData.getNodeId();
				break;
			}
		}
		
		// The subscription was found and removed without error
		if (removedId != null) {
			addLog(LogmessageType.INFO, "Subscription to " + removedId.toString() + " was removed");
		}
		
	}
	
	// Sets the subscribed nodes value to the data structure
	public void updateSubscriptionValue(NodeId nodeid, String value) {

		// get the subscription to be updated from the list
		for (SubscriptionData sd : subscriptions) {

			if (sd.getNodeId() == nodeid) {
				// and update it
				sd.updateValue(value);
				break;
			}

		}
	}
	

	// Serializes all servers to the given file. Returns true if successful.
	public Boolean saveServers(String fileName) {
		
		try {

			FileOutputStream fos = MainPager.pager.openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			
			for (Server serv : servers) {
				os.writeObject(serv);
			}
			
			os.close();
			
		} catch (IOException e) {
			
			addLog(LogmessageType.ERROR, "Saving servers failed." + "\n" + e.toString());
			return false;
		}
		
		addLog(LogmessageType.INFO, "Servers were saved to:" + "\n" + fileName);
		return true;
		
	}
	
	// Tries to load servers from the given file. Returns true if successful.
	public boolean loadServers(String fileName) {
		
		try {
		
			FileInputStream fis = MainPager.pager.getBaseContext().openFileInput(fileName);
			ObjectInputStream is = new ObjectInputStream(fis);
			
			Server newServer = (Server)is.readObject();
						
			servers.clear();
			
			while (newServer != null) {
				
				addModifyServer(newServer);
				//servers.add(newServer);
				newServer = (Server)is.readObject();								
			}			
			
			is.close();
		
		} catch (Exception e) {
			
			//addLog(LogmessageType.ERROR, "Loading servers failed." + "\n" + e.toString());					
			return false;
		}
		
		addLog(LogmessageType.INFO, servers.size() + " servers were loaded from:" + "\n" + fileName);
		return true;
		
	}
	
}