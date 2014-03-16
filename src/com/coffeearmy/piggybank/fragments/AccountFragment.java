package com.coffeearmy.piggybank.fragments;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.adapters.OperationListAdapter;
import com.coffeearmy.piggybank.data.OperationHandler;

import de.greenrobot.event.EventBus;

public class AccountFragment extends Fragment {

	public static final String ACCOUNT_ID = "account_ID";
	public static final Object NOTIFY_CHANGE_OPERATION_LIST = "notify_change_operation_list";
	public static String ACCOUNT_FRAGMENT_TAG = "account_fragment_tag";
	private ListView mTransactionList;
	private TextSwitcher mTxtSwitchSaves;
	private OperationHandler operationHandler;

	private Account mAccount;
	private long mAccountID;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.account_layout, container,
				false);

		operationHandler = OperationHandler.getInstance();
		Bundle bundle = this.getArguments();
		mAccountID = bundle.getLong(ACCOUNT_ID);
		// Get list items
		mAccount = operationHandler.getAccount(mAccountID);
		// Get Current saves
		Double currentMoney = operationHandler.getAccountMoney(mAccount);
		// Get total saves
		List<Operation> results = operationHandler
				.getOperationsFromAccountList(mAccount);
		// Setup List
		mTransactionList = (ListView) result.findViewById(R.id.lstTransaction);
		// Set Adapter
		mTransactionList
				.setAdapter(new OperationListAdapter(PiggybankActivity
						.getContext(), android.R.layout.simple_list_item_1, 1,
						results));
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
		EventBus.getDefault().unregister(this);
		super.onPause();
	}

	@Override
	public void onResume() {
		// Set Title in the action bar
		getActivity().setTitle(operationHandler.getAccountName(mAccount));
		EventBus.getDefault().register(this);
		super.onResume();
	}

	public void onEvent(String message) {
		if (message.equals(NOTIFY_CHANGE_OPERATION_LIST)) {
			listChanged();
		}
	}

	public void listChanged() {
		// Get list items
		mAccount = operationHandler.getAccount(mAccountID);
		// Get Current saves
		Double currentMoney = operationHandler.getAccountMoney(mAccount);
		// Set currect Saves
		mTxtSwitchSaves.setText(currentMoney.toString());
		// Get total saves
		List<Operation> operationList = new ArrayList<Operation>();
		operationList.addAll( operationHandler
				.getOperationsFromAccountList(mAccount));
		((OperationListAdapter) mTransactionList.getAdapter())
				.changeDataSet(operationList);
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

		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		Fragment prev = getActivity().getSupportFragmentManager()
				.findFragmentByTag(AccountDialog.FRAGMENT_TAG);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		OperationDialog newFragment = OperationDialog.newInstance(1, mAccount);
		newFragment.show(ft, OperationDialog.FRAGMENT_TAG);

	}

}
