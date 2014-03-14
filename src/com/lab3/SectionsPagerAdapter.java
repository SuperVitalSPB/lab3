package com.lab3;

import java.util.Locale;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
	Fragment[] Fragments;
	Intent intentLogin;
	public final int requestLoginCode = 123;
	public FragmentActivity mContext;
	public boolean isOnline;	
	
	
	public void ShowLogin(){
	   intentLogin = new Intent(mContext, LoginActivity.class);
	   mContext.startActivityForResult(intentLogin, requestLoginCode);
	}
	
	
	public Fragment getFragment(int position) {
		return Fragments[position];
	};

	public SectionsPagerAdapter(android.support.v4.app.FragmentManager fm) {
		super(fm);
		Fragments = new Fragment[getCount()];
	};

	@Override
	public Fragment getItem(int position) {
		// getItem is called to instantiate the fragment for the given page.
		// Return a DummySectionFragment (defined as a static inner class
		// below) with the page number as its lone argument.
		Fragment fragment = new Fragment();
		
		if (position == 0) {
			fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			((DummySectionFragment)fragment).isOnline = isOnline; 
			
		} else if (position == 1) {
			fragment = new DummySectionFragment2();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment2.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			
		} else if (position == 2) {
			fragment = new DummySectionFragment3();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment3.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			
		}
		
		Fragments [position] = fragment;
		
		if (getFragment(0) != null ){
			if (intentLogin == null && isOnline) {
				ShowLogin();
//				((DummySectionFragment)getFragment(0)).isDevelop = true;
//				((DummySectionFragment)getFragment(0)).logToken = "logtoken";
			}
		}

		
		return fragment;
	}

	@Override
	public int getCount() {
		// Show 3 total pages.
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			return mContext.getString(R.string.title_section1).toUpperCase(l);
		case 1:
			return mContext.getString(R.string.title_section2).toUpperCase(l);
		case 2:
			return mContext.getString(R.string.title_section3).toUpperCase(l);
		}
		return null;
	}
}
