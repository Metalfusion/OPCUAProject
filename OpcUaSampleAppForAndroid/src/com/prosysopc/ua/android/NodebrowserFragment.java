package com.prosysopc.ua.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NodebrowserFragment extends Fragment {
	
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
		
		return rootView;
	}
}