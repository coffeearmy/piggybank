package com.coffeearmy.piggybank.fragments;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.appcompat.R.bool;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.OperationDao;
import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.OperationDao.Properties;
import com.coffeearmy.piggybank.R.id;
import com.coffeearmy.piggybank.R.layout;
import com.coffeearmy.piggybank.adapters.OperationListAdapter;
import com.coffeearmy.piggybank.data.OperationHandler;


public class AccountFragment extends Fragment {

	public static final String ACCOUNT_ID = "account_ID";
	public static String ACCOUNT_FRAGMENT_TAG = "account_fragment_tag";
	private ListView mTransactionList;
	private TextSwitcher mTxtSwitchSaves;
	private OperationHandler operationHandler;

	private Account account;
	private long accountID;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.account_layout, container,
				false);

		operationHandler = OperationHandler.getInstance();
		Bundle bundle = this.getArguments();
		accountID = bundle.getLong(ACCOUNT_ID);
		// Get list items
		account = operationHandler.getAccount(accountID);
		// Get Current saves
		Double currentMoney = operationHandler.getAccountMoney(account);
		// Get total saves
		List<Operation> results = operationHandler.getOperationsFromAccountList(account);
		// Setup List
		mTransactionList = (ListView) result.findViewById(R.id.lstTransaction);
		// Set Adapter
		mTransactionList.setAdapter(new OperationListAdapter(PiggybankActivity.getContext(), android.R.layout.simple_list_item_1, 1, results));
		// Set total saves
		mTxtSwitchSaves = (TextSwitcher) result
				.findViewById(R.id.txtSavesTotal);

		mTxtSwitchSaves.setFactory(new ViewFactory() {

			public View makeView() {
				TextView myText = new TextView(PiggybankActivity.getContext());
				myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
				myText.setTextSize(40);
				myText.setTextColor(Color.WHITE);
				return myText;
			}
		});
		// Declare the in and out animations and initialize them
		// NINEOLDANIMATION
		// Animation in =
		// AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
		// Animation out =
		// AnimationUtils.loadAnimation(this,android.R.anim.slide_out_right);
		//
		// // set the animation type of textSwitcher
		// mTxtSwitchSaves.setInAnimation(in);
		// mTxtSwitchSaves.setOutAnimation(out);

		// Set currect Saves
		mTxtSwitchSaves.setText(currentMoney.toString());

		setHasOptionsMenu(true);
		return (result);
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onResume() {
		// Set Title in the action bar
		getActivity().setTitle(operationHandler.getAccountName(account));
		super.onResume();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.account_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.account_menu_add:
			showNewOperationFragment();
			break;
		case R.id.account_menu_filter:
			break;
		case R.id.account_menu_properties:
			break;
		}
		return (super.onOptionsItemSelected(item));
	}

	private void showNewOperationFragment() {
		Fragment account = new NewOperationFragment();
		Bundle args = new Bundle();
		args.putLong(NewOperationFragment.ACCOUNT_ID, accountID);
		account.setArguments(args);
		// Insert the fragment by replacing any existing fragment
		getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, account, NewOperationFragment.FRAG_NEW_OPERATION_TAG)
		.addToBackStack(null)
		.commit();
				
		// Highlight the selected item, update the title, and close the drawer		
		getActivity().setTitle("New operation");
		
		
	}

}
