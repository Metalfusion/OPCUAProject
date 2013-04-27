package com.prosysopc.ua.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


	public class SublistFragment extends Fragment {
		static MainPager mPager;
		/**
		 * The fragment argument representing the section number for this fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";
	
		public SublistFragment() {
		}
		
		public static final SublistFragment newInstance(MainPager pager)
		{
			mPager = pager;
			SublistFragment f = new SublistFragment();
			return f;
			
		}
	
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
			
			View rootView = inflater.inflate(R.layout.sublist, container, false);
			
			return rootView;
		}
	}