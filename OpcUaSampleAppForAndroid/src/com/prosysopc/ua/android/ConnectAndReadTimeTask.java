package com.prosysopc.ua.android;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Locale;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.opcfoundation.ua.builtintypes.DataValue;
import org.opcfoundation.ua.builtintypes.DateTime;
import org.opcfoundation.ua.builtintypes.LocalizedText;
import org.opcfoundation.ua.core.ApplicationDescription;
import org.opcfoundation.ua.core.ApplicationType;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.opcfoundation.ua.utils.CertificateUtils;

import com.prosysopc.ua.ApplicationIdentity;
import com.prosysopc.ua.PkiFileBasedCertificateValidator;
import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.SessionActivationException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.UserIdentity;
import com.prosysopc.ua.client.InvalidServerEndpointException;
import com.prosysopc.ua.client.ServerConnectionException;
import com.prosysopc.ua.client.UaClient;

/**
 * This is an AsyncTask for connecting to an OPC UA server and reading the server time
 */
public class ConnectAndReadTimeTask extends AsyncTask<String, Void, Boolean>
{
	private String applicationName;
	private DateTime time;
	private OpcUaSampleActivity activity;
	private String error;
	
	/**
	 * Creates a new Task
	 * 
	 * @param appName
	 */
	public ConnectAndReadTimeTask(OpcUaSampleActivity activity, String appName){
		this.activity = activity;
		this.applicationName = appName;
	}
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 * 
	 * This method is executed in the background, and will do the reading
	 */
	protected Boolean doInBackground(String...strings)
	{
		String serverUri = strings[0];
		UaClient myClient;
		try {
			myClient = createClient(serverUri);
			myClient.connect();
		} catch (Exception e) {
			error = e.toString();
			return false;
		}

		DataValue dv;
		try {
			dv = myClient.readValue(Identifiers.Server_ServerStatus_CurrentTime);
			time = (DateTime) dv.getValue().getValue();
		} catch (Exception e) {
			error = e.toString();
			return false;
		}
		
		myClient.disconnect();
   
	   	return true;
	}

	/**
	 * This is an example on how to create and set parameters to an UaClient
	 * 
	 * @param serverUri
	 * @return
	 * @throws URISyntaxException
	 * @throws SessionActivationException
	 */
	public UaClient createClient(String serverUri) throws URISyntaxException,
			SessionActivationException {
		// Create the UaClient
		UaClient myClient = new UaClient(serverUri);
		
		// Create and set certificate validator
		PkiFileBasedCertificateValidator validator = new PkiFileBasedCertificateValidator("/sdcard/PKI/CA");
		myClient.setCertificateValidator(validator);

		// Create application description
		ApplicationDescription appDescription = new ApplicationDescription();
		appDescription.setApplicationName(new LocalizedText(applicationName, Locale.ENGLISH));
		appDescription.setApplicationUri("urn:localhost:UA:"+applicationName);
		appDescription.setProductUri("urn:prosysopc.com:UA:"+applicationName);
		appDescription.setApplicationType(ApplicationType.Client);

		// Create and set application identity
		ApplicationIdentity identity =  new ApplicationIdentity();
		identity.setApplicationDescription(appDescription);
		identity.setOrganisation("Prosys");
		myClient.setApplicationIdentity(identity);
		
		// Set locale
		myClient.setLocale(Locale.ENGLISH);
		
		// Set default timeout to 60 seconds
		myClient.setTimeout(60000);

		// Set security mode to NONE (others not currently supported on Android)
		myClient.setSecurityMode(SecurityMode.NONE);

		// Set anonymous user identity
		myClient.setUserIdentity(new UserIdentity());
		
		return myClient;
	}
	
	protected void onPostExecute(Boolean success) {
		String result = null;
		if(success)
			// If the operation was a success, show the time on the UI
			result = time.toString();
		else
			// If we got a failure, show the exception
			result = "Error: " + error;
		activity.setTime(result);
    }
}
