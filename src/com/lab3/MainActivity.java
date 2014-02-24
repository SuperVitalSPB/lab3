package com.lab3;


import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;



public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(2);//ActionBar.NAVIGATION_MODE_TABS);   

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mSectionsPagerAdapter.mContext = this;
		mSectionsPagerAdapter.isOnline = isOnline();
		
				
		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});
		

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
		
		
	}
	
	
	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
//		Toast toast = Toast.makeText(getApplicationContext(), String.valueOf(tab.getPosition()), Toast.LENGTH_SHORT); 
//		toast.show();
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
/*		
		if ( ) {
			DummySectionFragment fragment = (DummySectionFragment) mSectionsPagerAdapter.getFragment(0);
			fragment.LoadRate();
		}
*/		
//		if (fragment == null) tab.select();
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == mSectionsPagerAdapter.requestLoginCode) {
			DummySectionFragment fragment = (DummySectionFragment) mSectionsPagerAdapter.getFragment(0);
			if (fragment != null) {
				if (resultCode == RESULT_OK & data != null) {
					fragment.logToken = data.getStringExtra(LoginActivity.logTokenKey);
					fragment.logName  = data.getStringExtra(LoginActivity.logNameKey);
				}else {
					fragment.logToken = "";
					fragment.logName  = "";
				}
				fragment.TabSelected();
			}
		}
	}
	
	public boolean isOnline() {
	  String cs = Context.CONNECTIVITY_SERVICE;
	  ConnectivityManager cm = (ConnectivityManager) getSystemService(cs);
	  if (cm.getActiveNetworkInfo() == null) {
	    return false;
	  }
	  return cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}
	
}


/*
 
    	<LinearLayout
	    android:id="@+id/layout_status"
	    android:layout_width="wrap_content"
	    android:layout_height="wrap_content"
	    android:layout_gravity="center"
	    android:gravity="center_horizontal"
	    android:orientation="vertical"
	    android:visibility="gone" >
	    <ProgressBar
	        style="?android:attr/progressBarStyleLarge"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_marginBottom="8dp" />
	</LinearLayout>

  

 */