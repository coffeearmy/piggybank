package main.java.com.coffeearmy.piggybank;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.coffeearmy.piggybank.R;

public class PiggybankActivity extends ActionBarActivity implements
		OnItemClickListener {
	//Context
	private static Context context;
	//UI elements
	private DrawerLayout mDrawerLayout = null;
	private ActionBarDrawerToggle mDrawerToggle = null;
	private ListView mDrawerList;
	//Titles
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context=this;
		//Set Title
		mTitle = mDrawerTitle = getTitle();
		//Get List drawer
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		//Set adapter
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_row, getResources().getStringArray(
						R.array.TestAccounts)));
		//Set onclicklistener
		mDrawerList.setOnItemClickListener(this);
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
				// creates call to onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(mDrawerTitle);
				ActivityCompat.invalidateOptionsMenu(PiggybankActivity.this); 
				// creates call to onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		//Display icons in action bar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		if (getSupportFragmentManager().findFragmentById(R.id.content_frame) == null) {
			///TODO remember last seeing account
			showAccountFragment(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return (true);
		}

		return (super.onOptionsItemSelected(item));
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View row, int position,
			long id) {
		//Show fragment clicked in the list
		showAccountFragment(position);
		mDrawerLayout.closeDrawers();
	}

	private void showAccountFragment(int position) {

		// Create a new fragment and specify the planet to show based on
		// position
		Fragment fragment = new AccountFragment();
		Bundle args = new Bundle();
		args.putInt(AccountFragment.ACCOUNT_ID, position);
		fragment.setArguments(args);

		// Insert the fragment by replacing any existing fragment
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(getResources().getStringArray(R.array.TestAccounts)[position]);
		mDrawerLayout.closeDrawer(mDrawerList);

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getSupportActionBar().setTitle(mTitle);
	}
	//Get Context
		public static Context getContext(){
			return context;
		}

}
