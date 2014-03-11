package com.coffeearmy.piggybank;

import java.util.List;

import android.R.anim;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.adapters.AccountListAdapter;
import com.coffeearmy.piggybank.data.OperationHandler;
import com.coffeearmy.piggybank.fragments.AccountDialog;
import com.coffeearmy.piggybank.fragments.AccountFragment;
import com.coffeearmy.piggybank.fragments.NewAccountFragment;
import com.coffeearmy.piggybank.fragments.NewOperationFragment;

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
	private LinearLayout mLayoutDrawer;
	private Menu menuAccount;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		context=this;
		//Add Operation handler Fragment
		getSupportFragmentManager().beginTransaction().add(new OperationHandler(), OperationHandler.OPERATION_HANDLER_TAG).commit();
		//Set Title
		mTitle = mDrawerTitle = getTitle();
		//Get layout drawer 
		mLayoutDrawer =(LinearLayout) findViewById(R.id.left_drawer_container);
		
		//Get List drawer
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		
		//Get initial list of accounts
		OperationHandler opHandler= OperationHandler.getInstance();
		List<Account> accountsCursor=opHandler.getAccountsList();
		
		//Set adapter
		mDrawerList.setAdapter(new AccountListAdapter(this,R.layout.drawer_row,0,accountsCursor));
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
				//The action Split bar is on top of the DrawerLayout this why when the drawer is showing
				//we hide the actionbar
				///TODO find a better solution for this
				//showMenuItems(menuAccount);
				// creates call to onPrepareOptionsMenu()
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getSupportActionBar().setTitle(mDrawerTitle);
				ActivityCompat.invalidateOptionsMenu(PiggybankActivity.this); 
				//hideMenuItems(menuAccount);
				// creates call to onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		//Display icons in action bar
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		if (getSupportFragmentManager().findFragmentById(R.id.content_frame) == null) {
			///TODO remember last seeing account
			//showAccountFragment(0);
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {		
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.account_menu, menu);
		menuAccount=menu;		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
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
	
	@Override
	public void onItemClick(AdapterView<?> listView, View row, int position,
			long id) {
		String nameAccount= ((TextView) row.findViewById(R.id.txtv_account_name)).getText().toString();
		mTitle=nameAccount;
		//Show fragment clicked in the list
		showAccountFragment((Long) row.getTag(R.id.account_id),position);
		mDrawerLayout.closeDrawers();
	}
	
	public void onClickAddNewAccount(View v){
		showNewAccountFragment();
	}

	private void showNewAccountFragment() {
		
		//TESTTTTT
//		Fragment newAccount = new NewAccountFragment();
//		
//		// Insert the fragment by replacing any existing fragment
//		getSupportFragmentManager().beginTransaction()
//				.replace(R.id.content_frame, newAccount, NewAccountFragment.NEW_ACCOUNT_FRAGMENT_TAG).addToBackStack(null).commit();
//
//		// Highlight the selected item, update the title, and close the drawer
//		
//		setTitle("New account");
//		mDrawerLayout.closeDrawer(mLayoutDrawer);
		// DialogFragment.show() will take care of adding the fragment
	    // in a transaction.  We also want to remove any currently showing
	    // dialog, so make our own transaction and take care of that here.
	    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	    Fragment prev = getSupportFragmentManager().findFragmentByTag(AccountDialog.FRAGMENT_TAG);
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);

	    // Create and show the dialog. 
	    //First parameter of newIntance indicates if is a new account or an edit account dialog
	    DialogFragment newFragment = AccountDialog.newInstance(0,"",0,0,0);
	    newFragment.show(ft, "dialog");
		
	}
	
//	private void showNewOperationFragment() {
//		Fragment account = new NewOperationFragment();
//		
//		// Insert the fragment by replacing any existing fragment
//		getSupportFragmentManager().beginTransaction()
//				.replace(R.id.content_frame, account, NewOperationFragment.FRAG_NEW_OPERATION_TAG).addToBackStack(null).commit();
//
//		// Highlight the selected item, update the title, and close the drawer		
//		setTitle("New operation");
//		mDrawerLayout.closeDrawer(mLayoutDrawer);
//		
//	}

	private void showAccountFragment(Long ID,int position) {

		// Create a new fragment and specify the planet to show based on
		// position
		Fragment fragment = new AccountFragment();
		Bundle args = new Bundle();
		args.putLong(AccountFragment.ACCOUNT_ID, ID);
		fragment.setArguments(args);

		// Insert the fragment by replacing any existing fragment
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, fragment, AccountFragment.ACCOUNT_FRAGMENT_TAG).commit();

		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		mDrawerLayout.closeDrawer(mLayoutDrawer);

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
