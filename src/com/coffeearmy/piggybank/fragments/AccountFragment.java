package com.coffeearmy.piggybank.fragments;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.app.NotificationCompat.Style;
import android.support.v4.content.Loader;
import android.widget.AdapterView.OnItemLongClickListener;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.Button;
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
import com.coffeearmy.piggybank.auxiliar.StyleAPP;
import com.coffeearmy.piggybank.data.OperationHandler;
import com.coffeearmy.piggybank.data.OperationLoader;
import com.coffeearmy.piggybank.view.FontFitTextView;

import de.greenrobot.event.EventBus;

public class AccountFragment extends Fragment implements LoaderCallbacks<List<Operation>>{

	public static final String ACCOUNT_ID = "account_ID";
	public static final Object NOTIFY_CHANGE_OPERATION_LIST = "notify_change_operation_list";
	public static String ACCOUNT_FRAGMENT_TAG = "account_fragment_tag";

	private ListView mOperationList;
	private TextSwitcher mTxtSwitchSaves;
	private OperationHandler mOperationHandler;

	private Account mAccount;
	private long mAccountID;
	private FragmentManager mFragmentManager;
	private FragmentActivity mContext;
	private OperationListAdapter mListAdapter;
	private LayoutInflater mInflater;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRetainInstance(true);
		super.onCreate(savedInstanceState);
		
	}

	// Static pattern for Fragments
	public static AccountFragment newInstance(long ID) {

		AccountFragment f = new AccountFragment();

		Bundle args = new Bundle();
		args.putLong(AccountFragment.ACCOUNT_ID, ID);
		f.setArguments(args);

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mInflater=inflater;
		View result = mInflater.inflate(R.layout.account_layout, container,
				false);
		mContext=getActivity();
		mFragmentManager = getActivity().getSupportFragmentManager();
		mOperationHandler = OperationHandler.getInstance(mContext);

		
		// Setup List
		mOperationList = (ListView) result.findViewById(R.id.lstTransaction);
		// Set Adapter
		mListAdapter=new OperationListAdapter(mContext, android.R.layout.simple_list_item_1, 1,
				new ArrayList<Operation>());
		mOperationList
				.setAdapter(mListAdapter);
		// Set onLongclick listener
		mOperationList.setOnItemClickListener(new OnOperationClick());
		// Set total saves
		mTxtSwitchSaves = (TextSwitcher) result
				.findViewById(R.id.txtSavesTotal);
		//Set Header with total money of the account
				setHeaderFragment();
		
		if (savedInstanceState != null) {
			restoreState(savedInstanceState);
		}
		//Init loader for retrieve the operations in the account
		getLoaderManager().initLoader(Constant.LOADER_OPERATION_ID, null, this).forceLoad();
		setEmptyView();
		setHasOptionsMenu(true);
		return (result);
	}

	private void setEmptyView() {
		View emptyView = mInflater.inflate(R.layout.empty_view_operation, null);
		// SetUp emptyView		
		((ViewGroup) mOperationList.getParent()).addView(emptyView);
		mOperationList.setEmptyView(emptyView);		
	}

	private void setHeaderFragment() {
		Bundle bundle = this.getArguments();
		mAccountID = bundle.getLong(ACCOUNT_ID);
		// Get list items
		mAccount = mOperationHandler.getAccount(mAccountID);
		// Get Current saves
		Double currentMoney = mAccount.getMoney();
		mTxtSwitchSaves.setFactory(new ViewFactory() {

			public View makeView() {

				LayoutInflater inflater = LayoutInflater.from(mContext);

				TextView textView = (TextView) inflater.inflate(
						R.layout.text_view_switcher, null);

				return textView;

			}
		});

		// Set TextSwitch background
		int[] backgroundColor = StyleAPP.getBackgroundColor(getActivity(),
				mAccount.getIcon());
		mTxtSwitchSaves.setBackgroundColor(backgroundColor[0]);

		
		// Declare the in and out animations and initialize them
		 Animation in =
		 AnimationUtils.loadAnimation(mContext,R.anim.top_to_down_anim);
		 Animation out =
		 AnimationUtils.loadAnimation(mContext,android.R.anim.fade_out);
		
		 // set the animation type of textSwitcher
		 mTxtSwitchSaves.setInAnimation(in);
		 mTxtSwitchSaves.setOutAnimation(out);

		// Set currect Saves
		mTxtSwitchSaves.setText(Constant.DF.format(currentMoney));
		
	}

	private void restoreState(Bundle savedInstanceState) {
		int selectedItem = savedInstanceState
				.getInt(Constant.OPERATION_ITEM_LIST_SAVE);
		mOperationList.smoothScrollToPosition(selectedItem);
		mOperationList.setSelection(selectedItem);
	}

	@Override
	public void onPause() {
		Log.d(ACCOUNT_FRAGMENT_TAG, "unregister AccountFragment");
		EventBus.getDefault().unregister(this);
		super.onPause();
	}

	@Override
	public void onResume() {
		// Set Title in the action bar
		getActivity().setTitle(mOperationHandler.getAccountName(mAccount));
		EventBus.getDefault().register(this);
		super.onResume();
	}
	
	/** Save the last viewed position of the list */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(Constant.OPERATION_ITEM_LIST_SAVE,
				mOperationList.getFirstVisiblePosition());		
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
		mAccount = mOperationHandler.getAccount(mAccountID);
		// Get Current saves
		Double currentMoney = mAccount.getMoney();
		// Set currect Saves
		mTxtSwitchSaves.setText(Constant.DF.format(currentMoney));
		
		getLoaderManager().initLoader(Constant.LOADER_OPERATION_ID, null, this).forceLoad();
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

	/*
	 * 
	 * Methods for navigation between fragments
	 * 
	 */
	/** Show Dialog for a new Operation */
	private void showNewOperationFragment() {

		FragmentTransaction ft = mFragmentManager.beginTransaction();
		OperationDialog newFragment = OperationDialog.newInstance(0, null,
				mAccount);
		newFragment.show(ft, OperationDialog.FRAGMENT_TAG);

	}	

	/** Pass information with the row clicked for the operation dialog */
	private void showEditOperationFragment(Operation item) {
		FragmentTransaction ft = mFragmentManager.beginTransaction();
		
		DialogFragment newFragment = OperationDialog.newInstance(1, item,
				mAccount);
		newFragment.show(ft, OperationDialog.FRAGMENT_TAG);
	}

	/*
	 * 
	 *  Listeners
	 * 
	 */	
	/** On click in a row of the list shows the operation dialog for edit */
	protected class OnOperationClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			mOperationList.setSelection(arg2);
			showEditOperationFragment((Operation) arg0.getAdapter().getItem(
					arg2));			
		}
	}
	
	/*
	 * 
	 * Loader Callbaks
	 * @see android.support.v4.app.LoaderManager.LoaderCallbacks#onCreateLoader(int, android.os.Bundle)
	 */
	@Override
	public Loader<List<Operation>> onCreateLoader(int arg0, Bundle arg1) {
		return new OperationLoader(mContext,mAccount);
	}

	@Override
	public void onLoadFinished(Loader<List<Operation>> arg0,
			List<Operation> arg1) {
		mListAdapter.changeDataSet(arg1);		
	}

	@Override
	public void onLoaderReset(Loader<List<Operation>> arg0) {
		mListAdapter.changeDataSet(new ArrayList<Operation>());		
	}

}
