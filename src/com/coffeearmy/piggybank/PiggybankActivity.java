package com.coffeearmy.piggybank;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.coffeearmy.piggybank.data.OperationHandler;
import com.coffeearmy.piggybank.fragments.AccountFragment;
import com.coffeearmy.piggybank.fragments.DrawerMenu;
import com.coffeearmy.piggybank.fragments.OverviewFragment;

public class PiggybankActivity extends ActionBarActivity {

	// UI elements
	private static DrawerLayout mDrawerLayout = null;
	private static ActionBarDrawerToggle mDrawerToggle = null;

	/** Close the menu drawer */
	public static void closeDrawer(String name) {
		if (!(name == null)) {
			mTitle = name;
		}
		mDrawerLayout.closeDrawer(mLayoutDrawer);
	}

	// Titles
	private CharSequence mDrawerTitle;
	private OperationHandler mOperationHandler;
	private FragmentManager mFragmentManager;

	private static CharSequence mTitle;

	private static RelativeLayout mLayoutDrawer;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mFragmentManager = getSupportFragmentManager();

		createFragments();

		// Replace the layout inside the drawer layout with Drawer Menu fragment
		configDrawerMenu();

		// Set Title
		mTitle = mDrawerTitle = getTitle();
		// Get layout drawer
		mLayoutDrawer = (RelativeLayout) findViewById(R.id.drawerContainer);

		// Set drawer Layout
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// Configurate Drawer toggle
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
		// Display icons in action bar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Show drawer menu
		if (mDrawerToggle.onOptionsItemSelected(item))
			return (true);
		return (super.onOptionsItemSelected(item));
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	/**
	 * Replace the layout fragment in the layout drawer with the DrawerMenu
	 * Fragment
	 */
	private void configDrawerMenu() {

		// Get the fragment instance
		Fragment fragment = DrawerMenu.newInstance();

		// Insert the fragment by replacing any existing fragment
		mFragmentManager
				.beginTransaction()
				.replace(R.id.left_drawer_container, fragment,
						DrawerMenu.FRAGMENT_TAG).commit();
	}

	private void configurateOperationHandler() {

		if (mOperationHandler == null
				&& null == mFragmentManager
						.findFragmentByTag(OperationHandler.OPERATION_HANDLER_TAG)) {
			mOperationHandler = OperationHandler.getInstance(this);
			mFragmentManager
					.beginTransaction()
					.add(mOperationHandler,
							OperationHandler.OPERATION_HANDLER_TAG).commit();
		}
	}

	private void createFragments() {
		// Add Overview fragment
		showOverviewFragment();

		// Operation Handler
		configurateOperationHandler();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	/** Show the Overview Fragment */
	private void showOverviewFragment() {
		// When the orientation changes we need to know if an account fragment
		// is showing
		if (null == mFragmentManager
				.findFragmentByTag(AccountFragment.ACCOUNT_FRAGMENT_TAG)) {
			Fragment fragment = OverviewFragment.newInstance();

			mFragmentManager
					.beginTransaction()
					.replace(R.id.content_frame, fragment,
							OverviewFragment.FRAGMENT_TAG).commit();
		}
	}
}
