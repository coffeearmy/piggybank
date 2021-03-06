package com.coffeearmy.piggybank.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.adapters.AccountListAdapter;
import com.coffeearmy.piggybank.auxiliar.Constant;
import com.coffeearmy.piggybank.auxiliar.FragmentNavigation;
import com.coffeearmy.piggybank.data.AccountLoader;
import com.coffeearmy.piggybank.data.OperationHandler;

import de.greenrobot.event.EventBus;

/** Fragment with the logic of the Drawer menu */
public class DrawerMenu extends Fragment implements OnItemClickListener,
		OnItemLongClickListener, LoaderCallbacks<List<Account>>{

	public static final String FRAGMENT_TAG = "drawer_menu_tag";
	public static final Object NOTIFY_CHANGE_ACCOUNT_LIST = "notify_change_account_list";

	private static DrawerMenu mDrawerMenu;
	private ListView mDrawerList;
	private FragmentManager mFragmentManager;
	
	private ImageView mImageAddPiggybanck;
	private TextView mOverviewTextV;
	private AccountDialog mAccountDialog;
	private int mSelectedItem;

	private FragmentActivity mContext;
	private AccountListAdapter mAdapterList;
	private LayoutInflater mInflater;

	// Singleton
	public static DrawerMenu newInstance() {
		if (mDrawerMenu == null) {
			mDrawerMenu = new DrawerMenu();

			return mDrawerMenu;
		} else {
			return mDrawerMenu;
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater=inflater;
		View result = inflater.inflate(R.layout.drawer_menu_layout, container,
				false);
		mContext=getActivity();
		// Fragment Manager
		mFragmentManager = getActivity().getSupportFragmentManager();
		// Get List drawer
		mDrawerList = (ListView) result.findViewById(R.id.list_drawer);

		
		List<Account> accountsCursor = new ArrayList<Account>();
		mAdapterList=new AccountListAdapter(getActivity(),
				R.layout.drawer_row, 0, accountsCursor);
		// Set adapter
		mDrawerList.setAdapter(mAdapterList);
		//Set Empty View
		setEmptyView();
		
		// Set onclicklistener		
		mImageAddPiggybanck = (ImageView) result.findViewById(R.id.imgAddNewAccount);
		mImageAddPiggybanck.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showNewAccountFragment();				
			}
		});
		
		//Set Overview link
		mOverviewTextV= (TextView) result.findViewById(R.id.txtvOverviewDrawer);
		mOverviewTextV.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showOverviewFragment();		
			
			}
		});
		

		mDrawerList.setOnItemClickListener(this);
		mDrawerList.setOnItemLongClickListener(this);
		
		if(savedInstanceState!=null){
			restoreState(savedInstanceState);
		}
		getLoaderManager().initLoader(Constant.LOADER_ACCOUNT_ID, null, this).forceLoad();
		
		return result;
	}

	private void setEmptyView() {
		View emptyView = mInflater.inflate(R.layout.empty_view_operation, null);
		TextView emptyText = (TextView) emptyView.findViewById(R.id.decoEmptyOp1);
		emptyText.setText("No piggybanks :(");
		TextView emptyText2 = (TextView) emptyView.findViewById(R.id.decoEmptyOp2);
		emptyText2.setText("Add a new piggybank");
		((ViewGroup) mDrawerList.getParent()).addView(emptyView);
		mDrawerList.setEmptyView(emptyView);
	}
	private void restoreState(Bundle savedInstanceState) {
		mSelectedItem=savedInstanceState.getInt(Constant.MENU_SELECTED_ITEM);
		mDrawerList.setItemChecked(mSelectedItem, true);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	
		outState.putInt(Constant.MENU_SELECTED_ITEM, mSelectedItem);
		super.onSaveInstanceState(outState);
	}
	

	@Override
	public void onResume() {
		EventBus.getDefault().register(this);
		super.onResume();
	}

	@Override
	public void onPause() {
		EventBus.getDefault().unregister(this);
		super.onPause();
	}
	
	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		MenuItem menuItemAccountView = menu.findItem(R.id.account_menu_add);
		if(menuItemAccountView!=null){
			menuItemAccountView.setVisible(false);
		}
		super.onPrepareOptionsMenu(menu);
	}

	/** EventBus class for listening the broadcast events */
	public void onEvent(String message) {
		if (message.equals(NOTIFY_CHANGE_ACCOUNT_LIST)) {
			listChanged();
		}
	}

	/** Method for change the data from the account list */
	public void listChanged() {
		getLoaderManager().initLoader(Constant.LOADER_ACCOUNT_ID, null, this).forceLoad();		
	}
	
	
	/*
	 * 
	 * Navigation between fragments
	 * 
	 */
	private void clearDialogFragment(){
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		Fragment prev = mFragmentManager
				.findFragmentByTag(AccountDialog.FRAGMENT_TAG);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null).commit();
	}

	/** Show the Dialog for create a new account */
	private void showNewAccountFragment() {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		clearDialogFragment();
		 mAccountDialog = AccountDialog.newInstance(0, null);
		 mAccountDialog.show(ft, AccountDialog.FRAGMENT_TAG);

	}

	/** Show the Dialog for editing and account */
	private void showEditAccountFragment(Account account) {

		FragmentTransaction ft = mFragmentManager.beginTransaction();
		clearDialogFragment();

		// Create and show the dialog.
		// First parameter of newIntance indicates if is a new account or an
		// edit account dialog
		mAccountDialog = AccountDialog.newInstance(1, account);
		mAccountDialog.show(ft, AccountDialog.FRAGMENT_TAG);

	}

	/**
	 * Show the detail Fragment with the data from the account	 
	 * @param ID , id from the account
	 * @param position , position from the list for set the item checked
	 * */
	private void showAccountFragment(Long ID, int position) {

		Fragment fragment = AccountFragment.newInstance(ID);
		
		// Insert the fragment by replacing any existing fragment
		FragmentNavigation.showFragment(fragment, AccountFragment.ACCOUNT_FRAGMENT_TAG, mFragmentManager);	
		
		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		PiggybankActivity.closeDrawer(null);
	}
	
	/** Show the Overview fragment, and change the actionbar title to "Overview"*/
	protected void showOverviewFragment() {
		Fragment fragment = OverviewFragment.newInstance();
		FragmentNavigation.showFragment(fragment, OverviewFragment.FRAGMENT_TAG, mFragmentManager);		

		PiggybankActivity.closeDrawer("Overview");
	}

	/*
	 * 
	 * Listeners
	 * 
	 */
	/** ON long click in the list shows the account dialog for edit or detele it */
	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		showEditAccountFragment((Account) arg0.getAdapter().getItem(arg2));
		mDrawerList.setSelection(arg2);
		mSelectedItem=arg2;
		return false;
	}

	/**
	 * On click in the row shows the detail fragment with the data of the
	 * account
	 */
	@Override
	public void onItemClick(AdapterView<?> listView, View row, int position,
			long id) {
		String nameAccount = ((TextView) row
				.findViewById(R.id.txtvDrawerAccountName)).getText().toString();

		// Show fragment clicked in the list
		showAccountFragment((Long) row.getTag(R.id.account_id), position);
		mDrawerList.setSelection(position);
		mSelectedItem=position;
		PiggybankActivity.closeDrawer(nameAccount);
	}
	
	/*
	 * 
	 *  Loader Interface 
	 * 
	 */
	@Override
	public Loader<List<Account>> onCreateLoader(int arg0, Bundle arg1) {
		return new AccountLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<List<Account>> arg0, List<Account> arg1) {
		mAdapterList.changeDataSet(arg1);		
	}

	@Override
	public void onLoaderReset(Loader<List<Account>> arg0) {
		mAdapterList.changeDataSet(new ArrayList<Account>());
	}

}
