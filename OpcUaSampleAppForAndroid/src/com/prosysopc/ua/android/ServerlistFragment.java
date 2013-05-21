package com.prosysopc.ua.android;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Identifiers;

import com.prosysopc.ua.ServiceException;
import com.prosysopc.ua.StatusException;
import com.prosysopc.ua.android.Logmessage.LogmessageType;
import com.prosysopc.ua.client.AddressSpaceException;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.SimpleAdapter;


@SuppressLint("ValidFragment")
public class ServerlistFragment extends ListFragment {
	
	
	static MainPager mPager;
	private SimpleAdapter adapter;
	ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
	
	
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";
	
	public ServerlistFragment() {		
	}
	
	@SuppressLint("ValidFragment")
	public ServerlistFragment(MainPager pager) {
		mPager = pager;
	}
	
	public static final ServerlistFragment newInstance( MainPager pager )
	{
		
		ServerlistFragment f = new ServerlistFragment( pager );
		return f;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.serverlist, container, false);
		
		updateList();	    
	    registerForContextMenu(rootView.findViewById(android.R.id.list));
	    
		return rootView;
	}
	
	
	public void onStart()
	{
		super.onStart();
		updateList();		
	}
	
	private void updateList() {
		
		List<Server> servers =  mPager.opcreader.getServers();
		HashMap<String,String> item;
		
		// clear the list so the list doesn't grow on every time it is created
		list.clear();
	    for( int i=0; i < servers.size(); i++ ){
	      item = new HashMap<String,String>();
	      item.put( "line1", servers.get(i).getName());
	      item.put( "line2", servers.get(i).getAddress());
	      list.add( item );
	    }
	    adapter = new SimpleAdapter(this.getActivity(), list,
	    		      android.R.layout.two_line_list_item ,
	    		      new String[] { "line1","line2" },
	    		      new int[] {android.R.id.text1, android.R.id.text2});
	    setListAdapter( adapter );
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
	    
		super.onCreateContextMenu(menu, v, menuInfo);
	    
		if (v.getId() == this.getListView().getId()) {
	    
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
	        //HashMap item = (HashMap) getListView().getItemAtPosition(info.position);
	        menu.setHeaderTitle("Select action");
	        menu.add(0, info.position, 0, "Connect");
	        menu.add(0, 0, 0, "Edit");
	        menu.add(0, 0, 0, "Delete");
	        // toiseksi parametriksi pitäis tulla ilmeisesti itemin id, tyyliin HashMap.getId()
	        
	    }
	}
		
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		
		
	    if (item.getTitle() == "Connect") {
	        //Code To Handle connect
	    	try {
				mPager.opcreader.updateConnection(mPager.opcreader.getServer((int)info.id));
			} catch (URISyntaxException e) {
				
				mPager.opcreader.addLog(LogmessageType.WARNING, e.toString() );
			}
	    	
	    	// TODO: make the read asynchronous and let opcreader handle that 
	    	// refresh nodebrowser, doesn't work
	    	NodebrowserFragment f = (NodebrowserFragment) mPager.mPagerAdapter.getItem(1);
	    	if( f != null)
	    	{
	    		Toast.makeText(getActivity(), "Wasn't null", Toast.LENGTH_SHORT).show();
	    		
	    		try {
	    			f.updateRootList(mPager.opcreader.connection.getNode(Identifiers.RootFolder, true));
	    			
	    		} catch (ServiceException e) {
	    			
	    			mPager.opcreader.addLog(LogmessageType.WARNING, e.toString());
	    			
	    		} catch (AddressSpaceException e) {
	    			
	    			mPager.opcreader.addLog(LogmessageType.WARNING, e.toString());
	    			
	    		} catch (StatusException e) {
	    			
	    			mPager.opcreader.addLog(LogmessageType.WARNING, e.toString());
	    			
	    		} catch (ServiceResultException e) {
	    			
	    			mPager.opcreader.addLog(LogmessageType.WARNING, e.toString());
	    		}	    		
	    	}
	    	
	    	
	    } 
	    else if (item.getTitle() == "Edit") {
	    	Intent intent = new Intent(getActivity(), ServerSettingsActivity.class);
			Bundle b = new Bundle();
			Server server = mPager.opcreader.getServer((int)info.id);
	    	b.putString("name", server.getName());
	    	b.putString("address", server.getAddress());
	    	b.putString("identity",server.getIdentity());
	    	b.putString("password",server.getPassword());
	    	b.putInt("timeout",server.getTimeout());
	    	intent.putExtras(b);
			startActivity(intent);
	    } 
	    else if (item.getTitle() == "Delete") {
	        //Code To Handle deletion
	    	mPager.opcreader.removeServer((int)info.id);
	    	adapter.notifyDataSetChanged();
	    } 
	    else {
	    	
	        return false;
	    }
	    
	    onStart();
	    return true;
	}
	
}