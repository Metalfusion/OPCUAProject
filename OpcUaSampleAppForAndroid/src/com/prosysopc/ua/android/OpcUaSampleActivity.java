package com.prosysopc.ua.android;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OpcUaSampleActivity extends Activity {
	
	// UI elements for testing purpose
	Button connectAndReadButton;
	TextView serverTimeField;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Get handles to the UI elements
        connectAndReadButton = (Button)findViewById(R.id.connectAndReadButton);
        serverTimeField = (TextView)findViewById(R.id.serverTimeField);
        
        // Set a handler to connectAndReadButton
        connectAndReadButton.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v)
        	{
        		// Read the time asynchronously by creating and executing a ConnectAndReadTimeTask
        		ConnectAndReadTimeTask task = new ConnectAndReadTimeTask(OpcUaSampleActivity.this, "DemoAppForAndroid");
        		task.execute(new String[] {"opc.tcp://10.0.2.2:52520/OPCUA/SampleConsoleServer"});
        	}
        });
    }
    
    /**
     * Sets the time on the UI. Only call this with the UI thread, in this example ConnectAndReadTimeTask.onPostExecute will call this.
     * 
     * @param time
     */
    public void setTime(String value)
    {
    	serverTimeField.setText(value);
    }
}