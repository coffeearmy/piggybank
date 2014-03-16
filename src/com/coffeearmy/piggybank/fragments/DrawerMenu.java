package com.coffeearmy.piggybank.fragments;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.adapters.AccountListAdapter;
import com.coffeearmy.piggybank.auxiliar.Constant;
import com.coffeearmy.piggybank.data.OperationHandler;

import de.greenrobot.event.EventBus;

public class DrawerMenu extends Fragment implements OnItemClickListener, OnItemLongClickListener  {

	public static final String FRAGMENT_TAG = "drawer_menu_tag";
	public static final Object NOTIFY_CHANGE_ACCOUNT_LIST = "notify_change_account_list";
	private static DrawerMenu mDrawerMenu;
	private ListView mDrawerList;
	private FragmentManager mFragmentManager;
	private Button mButtonAddPiggybank;
	
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
		
		//Fragment Manager
		mFragmentManager= getActivity().getSupportFragmentManager();
		// Get List drawer
		mDrawerList = (ListView) result.findViewById(R.id.left_drawer);

		// Get initial list of accounts
		OperationHandler opHandler = OperationHandler.getInstance();
		List<Account> accountsCursor = opHandler.getAccountsList();

		// Set adapter
		mDrawerList.setAdapter(new AccountListAdapter(getActivity(),
				R.layout.drawer_row, 0, accountsCursor));
		// Set onclicklistener
		mButtonAddPiggybank= (Button) result.findViewById(R.id.btn_new_account_drawer_menu);
		mButtonAddPiggybank.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				showNewAccountFragment();
			}
		});
		
		mDrawerList.setOnItemClickListener(this);
		mDrawerList.setOnItemLongClickListener(this);
		return result;
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
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		showEditAccountFragment((Account) arg0.getAdapter().getItem(arg2));
		mDrawerList.setSelection(arg2);
		return false;
	}	
	
	@Override
	public void onItemClick(AdapterView<?> listView, View row, int position,
			long id) {
		String nameAccount= ((TextView) row.findViewById(R.id.txtv_account_name)).getText().toString();
		
		//Show fragment clicked in the list
		showAccountFragment((Long) row.getTag(R.id.account_id),position);
		
		PiggybankActivity.closeDrawer(nameAccount);
	}
	
	public void onEvent(String message){
		if (message.equals(
				NOTIFY_CHANGE_ACCOUNT_LIST)) {
			listChanged();
		}
	}
	
	public void listChanged(){
		OperationHandler opHandler = OperationHandler.getInstance();
		List<Account> accountsList = opHandler.getAccountsList();
		((AccountListAdapter) mDrawerList.getAdapter()).changeDataSet(accountsList);
	}
		
	private void showNewAccountFragment() {
		
		// DialogFragment.show() will take care of adding the fragment
	    // in a transaction.  We also want to remove any currently showing
	    // dialog, so make our own transaction and take care of that here.
	    FragmentTransaction ft = mFragmentManager.beginTransaction();
	    Fragment prev = mFragmentManager.findFragmentByTag(AccountDialog.FRAGMENT_TAG);
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);

	    // Create and show the dialog. 
	    //First parameter of newIntance indicates if is a new account or an edit account dialog
	    DialogFragment newFragment = AccountDialog.newInstance(0,null);
	    newFragment.show(ft, AccountDialog.FRAGMENT_TAG);
		
	}
	
	private void showEditAccountFragment(Account account) {
		
		FragmentTransaction ft = mFragmentManager.beginTransaction();
	    Fragment prev = mFragmentManager.findFragmentByTag(AccountDialog.FRAGMENT_TAG);
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);
	   

	    // Create and show the dialog. 
	    //First parameter of newIntance indicates if is a new account or an edit account dialog
	    DialogFragment newFragment = AccountDialog.newInstance(1,account);
	    newFragment.show(ft, AccountDialog.FRAGMENT_TAG);
		
	}
	

	private void showAccountFragment(Long ID,int position) {

		// Create a new fragment and specify the planet to show based on
		// position
		Fragment fragment = new AccountFragment();
		Bundle args = new Bundle();
		args.putLong(AccountFragment.ACCOUNT_ID, ID);
		fragment.setArguments(args);

		// Insert the fragment by replacing any existing fragment
		mFragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment, AccountFragment.ACCOUNT_FRAGMENT_TAG).commit();

		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		PiggybankActivity.closeDrawer(null);
	}

}
