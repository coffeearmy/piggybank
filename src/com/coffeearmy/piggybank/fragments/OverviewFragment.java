package com.coffeearmy.piggybank.fragments;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.adapters.OverviewOperationListAdapter;
import com.coffeearmy.piggybank.auxiliar.Constant;
import com.coffeearmy.piggybank.data.OperationHandler;

/** This fragmnet is under construction :) please check it later */
public class OverviewFragment extends Fragment {

	public static final String FRAGMENT_TAG = "overview_fragment_tag";
	private static OverviewFragment mOverview;

	// /The static instance done for future functionality in the overview
	public static OverviewFragment newInstance() {

		mOverview = new OverviewFragment();

		return mOverview;
	}

	private OperationHandler mOperationHandler;
	private List<Operation> mListOperations;
	private FragmentManager mFragmentManager;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.overview_layout, container,
				false);
		mFragmentManager = getActivity().getSupportFragmentManager();
		mOperationHandler = OperationHandler.getInstance();

		View emptyView = inflater.inflate(R.layout.empty_main, null);
		// SetUp emptyView
		Button emptyButton = (Button) emptyView.findViewById(R.id.btnEmpty);
		emptyButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showNewAccountFragment();
			}
		});

		mListOperations = mOperationHandler.getLastOperationList();
		ListView listOverview = (ListView) result
				.findViewById(R.id.lstvOverview);

		// Set Empty view in the listview
		((ViewGroup) listOverview.getParent()).addView(emptyView);
		listOverview.setEmptyView(emptyView);

		// Set Header listview
		// TextView titleView = new TextView(getActivity());
		// titleView.setText("Last Operation");
		// listOverview.addHeaderView(titleView);
		// Calendar
		// Get operation in last week
		Calendar calendar = Calendar.getInstance();
		Date dateToday = calendar.getTime();
		calendar.add(Calendar.DATE, -7);
		Date dateLastWeek = calendar.getTime();

		mListOperations = mOperationHandler
				.getLastOperationListbyDate(dateLastWeek);

		View headerOverviewView = inflater.inflate(R.layout.header_overview,
				null);
		TextView datesRange = (TextView) headerOverviewView
				.findViewById(R.id.txtvOverviewDateHeader);
		datesRange.setText("Today - "
				+ Constant.dateFormatMMMDD.format(dateLastWeek));

		// Add Overview
		listOverview.addHeaderView(headerOverviewView);

		listOverview.setAdapter(new OverviewOperationListAdapter(
				PiggybankActivity.getContext(),
				R.layout.overview_operation_row, 1, mListOperations));
		
		setRetainInstance(true);

		return result;
	}

	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// View result = inflater.inflate(R.layout.overview_layout, container,
	// false);
	// showOverviewAccountFragment();
	// showOverviewOperationListFragment();
	// return result;
	// }

	// private void showOverviewAccountFragment(){
	// Fragment fragment = new OverviewAccount();
	//
	// // Insert the fragment by replacing any existing fragment
	// getActivity().getSupportFragmentManager().beginTransaction()
	// .replace(R.id.frameAccountListOverview, fragment,
	// OverviewAccount.FRAGMENT_TAG).commit();
	//
	//
	// }
	//
	// private void showOverviewOperationListFragment(){
	// Fragment fragment = new OverviewOperation();
	//
	// // Insert the fragment by replacing any existing fragment
	// getActivity().getSupportFragmentManager().beginTransaction()
	// .replace(R.id.frameOperationListOverview, fragment,
	// OverviewOperation.FRAGMENT_TAG).commit();
	//
	//
	// }
	// public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	// super.onCreateOptionsMenu(menu, inflater);
	// }

	/** Show the Dialog for create a new account */
	private void showNewAccountFragment() {

		FragmentTransaction ft = mFragmentManager.beginTransaction();
		Fragment prev = mFragmentManager
				.findFragmentByTag(AccountDialog.FRAGMENT_TAG);
		if (prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		DialogFragment newFragment = AccountDialog.newInstance(0, null);
		newFragment.show(ft, AccountDialog.FRAGMENT_TAG);

	}
}
