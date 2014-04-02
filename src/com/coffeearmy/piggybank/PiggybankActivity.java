package com.coffeearmy.piggybank;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.coffeearmy.piggybank.data.OperationHandler;
import com.coffeearmy.piggybank.fragments.AccountFragment;
import com.coffeearmy.piggybank.fragments.DrawerMenu;
import com.coffeearmy.piggybank.fragments.OverviewFragment;

public class PiggybankActivity extends ActionBarActivity  {
	//Context
	private static Context context;
	//UI elements
	private static DrawerLayout mDrawerLayout = null;
	private static ActionBarDrawerToggle mDrawerToggle = null;
	//Titles
	private CharSequence mDrawerTitle;
	private static CharSequence mTitle;
	private static RelativeLayout mLayoutDrawer;
		

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context=this;
		//Add Operation handler Fragment
		getSupportFragmentManager().beginTransaction().add(new OperationHandler(), OperationHandler.OPERATION_HANDLER_TAG).commit();
		
		//Add Overview fragment
		showOverviewFragment();
		//Replace the layout inside the drawer layout with Drawer Menu fragment
		configDrawerMenu();
		//Set Title
		mTitle = mDrawerTitle = getTitle();
		//Get layout drawer 
		mLayoutDrawer =(RelativeLayout) findViewById(R.id.drawerContainer);
		

		//Set drawer Layout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		
		
		//Configurate Drawer toggle
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			
			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getSupportActionBar().setTitle(mTitle);
				ActivityCompat.invalidateOptionsMenu(PiggybankActivity.this); 
				
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(mDrawerTitle);
				ActivityCompat.invalidateOptionsMenu(PiggybankActivity.this); 
			
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		//Display icons in action bar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		
	}
	/** Replace the layout fragment in the layout drawer with the DrawerMenu Fragment*/
	private void configDrawerMenu() {

		// Get the fragment instance
		Fragment fragment =  DrawerMenu.newInstance();
	
		// Insert the fragment by replacing any existing fragment
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.left_drawer_container, fragment, DrawerMenu.FRAGMENT_TAG).commit();
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {	
		//Show drawer menu
		if(mDrawerToggle.onOptionsItemSelected(item))
			return (true);		
		return (super.onOptionsItemSelected(item));
	}
	

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	/** Returns activity context*/
	public static Context getContext(){
		return context;
	}
	
	/** Close the menu drawer*/
	public static void closeDrawer(String name){
		if(!(name==null)){
			mTitle = name;
		}
		mDrawerLayout.closeDrawer(mLayoutDrawer);
			
	}
	
	/** Show the Overview Fragment*/
	private void showOverviewFragment(){
		Fragment fragment = OverviewFragment.newInstance();
				
		getSupportFragmentManager().beginTransaction()
				.add(R.id.content_frame, fragment, OverviewFragment.FRAGMENT_TAG).commit();
	}
}
