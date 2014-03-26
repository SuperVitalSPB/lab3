/**
 * 
 */
package ru.supervital.lab3;

import java.util.Locale;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
	
	Context mContext;
	DummySectionFragment[] Fragments;
	public boolean isOnline;	
	
	public DummySectionFragment getFragment(int position) {
		return Fragments[position];
	};
	
	
	public SectionsPagerAdapter(FragmentManager fm) {
		super(fm);
		Fragments = new DummySectionFragment[getCount()];
	}

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		DummySectionFragment mFragment;
		

		if (position == 0) {
			mFragment = new DSF_Rate();
		} else if (position == 1) {
			mFragment = new DSF_Graph();
		} else if (position == 2) {
			mFragment = new DSF_Map();
		} else return null;
		
		mFragment.isOnline = isOnline;
		
		Fragments [position] = mFragment;		
		
		Bundle args = new Bundle();
		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
		mFragment.setArguments(args);
		
		return mFragment;
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String res;
		if (position == 0) {
			res = mContext.getString(R.string.title_Rate);
		} else if (position == 1) {
			res = mContext.getString(R.string.title_Graph);
		} else if (position == 2) {
			res = mContext.getString(R.string.title_Map);
		} else res= null;	
		return res; 	
	}
}
