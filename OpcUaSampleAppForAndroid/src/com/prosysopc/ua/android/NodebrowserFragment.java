package com.prosysopc.ua.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NodebrowserFragment extends Fragment {
	static MainPager mPager;
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public NodebrowserFragment() {
	}

	public static final NodebrowserFragment newInstance()
	{
		NodebrowserFragment f = new NodebrowserFragment();
		return f;
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.nodebrowser, container, false);
		
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
             
		Fragment childfrag = new Nodelist_level_fragment();		
		ft.add(R.id.nodelevellayout ,childfrag, "nodelevel1");
		
		Fragment childfrag2 = new Nodelist_level_fragment();		
		ft.add(R.id.nodelevellayout ,childfrag2, "nodelevel2");
		
		Fragment childfrag3 = new Nodelist_level_fragment();		
		ft.add(R.id.nodelevellayout ,childfrag3, "nodelevel3");
        
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
				
		return rootView;
	}
}