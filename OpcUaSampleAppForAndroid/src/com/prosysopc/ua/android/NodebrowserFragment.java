package com.prosysopc.ua.android;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NodebrowserFragment extends Fragment {
	static MainPager mPager;
	
	private List<Nodelist_level_fragment> listFragments = new ArrayList<Nodelist_level_fragment>();
	
	
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	public static final String ARG_SECTION_NUMBER = "section_number";

	public NodebrowserFragment() {
	}

	public static final NodebrowserFragment newInstance(MainPager pager)
	{
		mPager = pager;
		NodebrowserFragment f = new NodebrowserFragment();
		return f;
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,	Bundle savedInstanceState) {
		
		/*FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		
		for (Fragment frag : listFragments) {
			ft.remove(frag);
		}		
		
		listFragments.clear();*/		
		
		View rootView = inflater.inflate(R.layout.nodebrowser, container, false);
		
		if (listFragments.size() == 0) {
			createTestFragments(1);
		}
						
		return rootView;
	}
	
	// Creates lists with dummy data
	private void createTestFragments( int number) {
		
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		
		int startPos = listFragments.size();  
        
		for (int i = 0; i < number; i++) {
		
			Nodelist_level_fragment childfrag = new Nodelist_level_fragment();
			childfrag.setup(this, null, startPos + i, false);			
			ft.add(R.id.nodelevellayout ,childfrag, "nodelevel" + startPos + i);
			listFragments.add(childfrag);
			
		}
		
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
	}
	
	// Adds a new list to the nodebrowser and removes unnecessary old ones.
	public void createList(int position, UINode rootNode, boolean attributeView) {
		
		
		// Transaction for removing the old fragments
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		
		ArrayList<Nodelist_level_fragment> removeList = new ArrayList<Nodelist_level_fragment>();
		
		for (int i = position; i < listFragments.size(); i++) {			
			ft.remove(listFragments.get(i));
			removeList.add(listFragments.get(i));
		}
		
		ft.commit();
		
		// Remove the old fragments from the list
		for (int i = 0; i < removeList.size(); i++) {
			listFragments.remove(removeList.get(i));
		}
		
		
		// New transaction for adding the new fragment
		FragmentTransaction ft2 = getChildFragmentManager().beginTransaction();
		ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		
		Nodelist_level_fragment childfrag = new Nodelist_level_fragment();
		
		childfrag.setup(this, rootNode, position, attributeView);		
		//childfrag.setup(this, null, position, attributeView); // FIXME: test code, correct code above
		
		ft2.add(R.id.nodelevellayout ,childfrag, "nodelevel" + position);		
		ft2.commit();
		listFragments.add(childfrag);
		
	}
	
}