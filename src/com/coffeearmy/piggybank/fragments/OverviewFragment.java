package com.coffeearmy.piggybank.fragments;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.adapters.OverviewOperationListAdapter;
import com.coffeearmy.piggybank.auxiliar.Constant;
import com.coffeearmy.piggybank.data.OperationHandler;
import com.coffeearmy.piggybank.data.OverviewLoader;


/** This fragmnet is under construction :) please check it later */
public class OverviewFragment extends Fragment implements LoaderCallbacks<List<Operation>>{

	public static final String FRAGMENT_TAG = "overview_fragment_tag";
	
	private static OverviewFragment mOverview;
	private List<Operation> mListOperations;
	private FragmentManager mFragmentManager;
	private LayoutInflater mInflater;
	private OverviewOperationListAdapter mListAdapter;
	private Date mDateLastWeek;

	private FragmentActivity mContext;
	private ListView mListOverview;

	private View mEmptyView;

	// /The static instance done for future functionality in the overview
	public static OverviewFragment newInstance() {
		mOverview = new OverviewFragment();

		return mOverview;
	}
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext=getActivity();
		mInflater = inflater;
		View result = inflater.inflate(R.layout.overview_layout, container,
				false);
		mFragmentManager = getActivity().getSupportFragmentManager();
		
		mListOperations= new ArrayList<Operation>();

//		mListOperations = mOperationHandler
//				.getLastOperationListbyDate(dateLastWeek);
		 mListOverview = (ListView) result
				.findViewById(R.id.lstvOverview);
		///TODO set the empty list for a first time the user enter
		// Set Empty view in the listview
		setEmptyView(mListOverview);

		// Set Header listview
		setHeaderView(mListOverview);
		mListAdapter=new OverviewOperationListAdapter(
				mContext,
				R.layout.overview_operation_row, 1, mListOperations);
		mListOverview.setAdapter(mListAdapter);
		
		getLoaderManager().initLoader(Constant.LOADER_OVERVIEW_ID, null, this).forceLoad();
		if(savedInstanceState!=null){
			restoreState(savedInstanceState);
		}

		return result;
	}
	///TODO set the empty list for a first time the user enter
	private void setEmptyView(ListView listOverview) {
		 mEmptyView = mInflater.inflate(R.layout.empty_overview, null);
		// SetUp emptyView
//		Button emptyButton = (Button) emptyView.findViewById(R.id.btnEmpty);
//		emptyButton.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				showNewAccountFragment();
//			}
//		});
		//((ViewGroup) listOverview.getParent()).addView(emptyView);
		//listOverview.setEmptyView(emptyView);
		listOverview.addFooterView(mEmptyView);
		mEmptyView.setVisibility(View.GONE);
	}
	public void setFooterViewVisibility(int size){
		if(size==0){
			mEmptyView.setVisibility(View.VISIBLE);
		}else{
			mEmptyView.setVisibility(View.GONE);
		}
		
	}

	private void setHeaderView(ListView listOverview) {
		// Calendar
		// Get operation in last week
		
		mDateLastWeek = getLastWeek();

		View headerOverviewView = mInflater.inflate(R.layout.header_overview,
				null);
		TextView datesRange = (TextView) headerOverviewView
				.findViewById(R.id.txtvOverviewDateHeader);
		datesRange.setText("Today - "
				+ Constant.dateFormatMMMDD.format(mDateLastWeek));

		// Add Overview
		listOverview.addHeaderView(headerOverviewView);

	}
	private Date getLastWeek(){
		if(mDateLastWeek==null){
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -7);
		Date dateLastWeek = calendar.getTime();
		mDateLastWeek=dateLastWeek;
		}
		return mDateLastWeek;
	}

	private void clearDialogFragment() {
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
		DialogFragment newFragment = AccountDialog.newInstance(0, null);
		newFragment.show(ft, AccountDialog.FRAGMENT_TAG);

	}
	/** Save the last viewed position of the list */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putInt(Constant.OVERVIEW_ITEM_LIST_SAVE,
				mListOverview.getFirstVisiblePosition());		
	}
	
	private void restoreState(Bundle savedInstanceState) {
		int selectedItem = savedInstanceState
				.getInt(Constant.OVERVIEW_ITEM_LIST_SAVE);
		mListOverview.smoothScrollToPosition(selectedItem);
		mListOverview.setSelection(selectedItem);
	}


	@Override
	public Loader<List<Operation>> onCreateLoader(int arg0, Bundle arg1) {
		return new OverviewLoader(mContext);
	}

	@Override
	public void onLoadFinished(Loader<List<Operation>> arg0,
			List<Operation> arg1) {
		setFooterViewVisibility(arg1.size());
		mListAdapter.changeDataSet(arg1);		
	}

	@Override
	public void onLoaderReset(Loader<List<Operation>> arg0) {
		mListAdapter.changeDataSet(new ArrayList<Operation>());		
	}	

}
