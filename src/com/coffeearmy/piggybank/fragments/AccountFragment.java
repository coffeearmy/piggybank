package com.coffeearmy.piggybank.fragments;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.AdapterView.OnItemLongClickListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.adapters.OperationListAdapter;
import com.coffeearmy.piggybank.auxiliar.Constant;
import com.coffeearmy.piggybank.data.OperationHandler;
import com.coffeearmy.piggybank.view.FontFitTextView;

import de.greenrobot.event.EventBus;

public class AccountFragment extends Fragment {

	public static final String ACCOUNT_ID = "account_ID";
	public static final Object NOTIFY_CHANGE_OPERATION_LIST = "notify_change_operation_list";
	public static String ACCOUNT_FRAGMENT_TAG = "account_fragment_tag";

	private ListView mOperationList;
	private TextSwitcher mTxtSwitchSaves;
	private OperationHandler operationHandler;

	private Account mAccount;
	private long mAccountID;
	private FragmentManager mFragmentManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.account_layout, container,
				false);

		mFragmentManager = getActivity().getSupportFragmentManager();
		operationHandler = OperationHandler.getInstance();
		Bundle bundle = this.getArguments();
		mAccountID = bundle.getLong(ACCOUNT_ID);
		// Get list items
		mAccount = operationHandler.getAccount(mAccountID);
		// Get Current saves
		Double currentMoney = mAccount.getMoney();
		// Get total saves
		List<Operation> results = operationHandler
				.getOperationsFromAccountList(mAccount);
		// Setup List
		mOperationList = (ListView) result.findViewById(R.id.lstTransaction);
		// Set Adapter
		mOperationList
				.setAdapter(new OperationListAdapter(PiggybankActivity
						.getContext(), android.R.layout.simple_list_item_1, 1,
						results));
		// Set onLongclick listener
		mOperationList.setOnItemLongClickListener(new OnOperationLongClick());
		// Set total saves
		mTxtSwitchSaves = (TextSwitcher) result
				.findViewById(R.id.txtSavesTotal);

		mTxtSwitchSaves.setFactory(new ViewFactory() {

			public View makeView() {
			
				LayoutInflater inflater = LayoutInflater.from(PiggybankActivity
						.getContext());

				TextView textView = (TextView) inflater.inflate(
						R.layout.text_view_switcher, null);

				return textView;

			}
		});
		// /TODO TO BE DONE
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
		mTxtSwitchSaves.setText(Constant.DF.format(currentMoney));

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

	/** EventBus method for read broadcasting events */
	public void onEvent(String message) {
		if (message.equals(NOTIFY_CHANGE_OPERATION_LIST)) {
			listChanged();
		}
	}

	/** Method for change the adapter in the operation list */
	public void listChanged() {
		// Get list items
		mAccount = operationHandler.getAccount(mAccountID);

		// Get Current saves
		Double currentMoney = mAccount.getMoney();
		
		// Set currect Saves
		mTxtSwitchSaves.setText(Constant.DF.format(currentMoney));
		// Get total saves
		List<Operation> operationList = new ArrayList<Operation>();
		operationList.addAll(operationHandler
				.getOperationsFromAccountList(mAccount));
		((OperationListAdapter) mOperationList.getAdapter())
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
		// case R.id.account_menu_filter:
		// break;
		// case R.id.account_menu_properties:
		// break;
		}
		return (super.onOptionsItemSelected(item));
	}

	/** Show Dialog for a new Operation */
	private void showNewOperationFragment() {

		FragmentTransaction ft = getActivity().getSupportFragmentManager()
				.beginTransaction();
		Fragment prev = getActivity().getSupportFragmentManager()
				.findFragmentByTag(AccountDialog.FRAGMENT_TAG);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		OperationDialog newFragment = OperationDialog.newInstance(0, null,
				mAccount);
		newFragment.show(ft, OperationDialog.FRAGMENT_TAG);

	}

	/** On Long click in a row of the list shows the operation dialog for edit */
	protected class OnOperationLongClick implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			showEditOperationFragment((Operation) arg0.getAdapter().getItem(
					arg2));
			mOperationList.setSelection(arg2);
			return false;
		}

	}

	/** Pass information with the row clicked for the operation dialog */
	private void showEditOperationFragment(Operation item) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		Fragment prev = mFragmentManager
				.findFragmentByTag(OperationDialog.FRAGMENT_TAG);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		DialogFragment newFragment = OperationDialog.newInstance(1, item,
				mAccount);
		newFragment.show(ft, OperationDialog.FRAGMENT_TAG);
	}

}
