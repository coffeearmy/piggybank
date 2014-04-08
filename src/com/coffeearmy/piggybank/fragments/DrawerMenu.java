package com.coffeearmy.piggybank.fragments;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.coffeearmy.piggybank.data.OperationHandler;

import de.greenrobot.event.EventBus;

/** Fragment with the logic of the Drawer menu */
public class DrawerMenu extends Fragment implements OnItemClickListener,
		OnItemLongClickListener {

	public static final String FRAGMENT_TAG = "drawer_menu_tag";
	public static final Object NOTIFY_CHANGE_ACCOUNT_LIST = "notify_change_account_list";
	private static final String SAVED_FRAGMENT = "saved_fragment";
	private static DrawerMenu mDrawerMenu;
	private ListView mDrawerList;
	private FragmentManager mFragmentManager;
	
	private ImageView mImageAddPiggybanck;
	private TextView mOverviewTextV;
	private AccountDialog mAccountDialog;
	private int mSelectedItem;
	private Fragment mVisibleFragment;

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
		View result = inflater.inflate(R.layout.drawer_menu_layout, container,
				false);

		// Fragment Manager
		mFragmentManager = getActivity().getSupportFragmentManager();
		// Get List drawer
		mDrawerList = (ListView) result.findViewById(R.id.list_drawer);

		// Get initial list of accounts
		OperationHandler opHandler = OperationHandler.getInstance();
		List<Account> accountsCursor = opHandler.getAccountsList();

		// Set adapter
		mDrawerList.setAdapter(new AccountListAdapter(getActivity(),
				R.layout.drawer_row, 0, accountsCursor));
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
		
		return result;
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
		OperationHandler opHandler = OperationHandler.getInstance();
		List<Account> accountsList = opHandler.getAccountsList();
		((AccountListAdapter) mDrawerList.getAdapter())
				.changeDataSet(accountsList);
	}
	
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
	 * 
	 * @param ID
	 *            , id from the account
	 * @param position
	 *            , position from the list for set the item checked
	 * */
	private void showAccountFragment(Long ID, int position) {

		// Create a new fragment and specify the planet to show based on
		// position
		Fragment fragment = AccountFragment.newInstance(ID);
		mVisibleFragment=fragment;
		// Insert the fragment by replacing any existing fragment
		mFragmentManager
				.beginTransaction()
				.replace(R.id.content_frame, fragment,
						AccountFragment.ACCOUNT_FRAGMENT_TAG).commit();

		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		PiggybankActivity.closeDrawer(null);
	}
	
	/** Show the Overview fragment, and change the actionbar title to "Overview"*/
	protected void showOverviewFragment() {
		Fragment fragment = OverviewFragment.newInstance();
		mVisibleFragment=fragment;
		FragmentNavigation.showFragment(fragment, OverviewFragment.FRAGMENT_TAG, mFragmentManager);		
//		mFragmentManager
//		.beginTransaction()
//		.replace(R.id.content_frame, fragment,
//				OverviewFragment.FRAGMENT_TAG).commit();
		PiggybankActivity.closeDrawer("Overview");
	}

	// Listeners
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

}
