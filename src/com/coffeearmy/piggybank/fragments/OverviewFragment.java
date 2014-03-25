package com.coffeearmy.piggybank.fragments;



import java.util.List;

import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.adapters.OverviewOperationListAdapter;
import com.coffeearmy.piggybank.data.OperationHandler;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**This fragmnet is under construction :) please check it later */
public class OverviewFragment extends ListFragment {
	
	
	public static final String FRAGMENT_TAG = "overview_fragment_tag";
	private static OverviewFragment mOverview;

	public static OverviewFragment newInstance() {
		
		if(mOverview==null){
			mOverview = new OverviewFragment();
		}	
		return mOverview;
	}


	private OperationHandler mOperationHandler;
	private List<Operation> mListOperations;
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mOperationHandler=OperationHandler.getInstance();
		
		mListOperations=mOperationHandler.getLastOperationList();
		setListShownNoAnimation(true);
		TextView titleView = new TextView(getActivity());
		titleView.setText("Last Operation");
		
		getListView().addHeaderView(titleView);
		getListView().setAdapter(new OverviewOperationListAdapter(PiggybankActivity.getContext(), R.layout.overview_operation_row, 1, mListOperations));
		

		
	}
	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		View result = inflater.inflate(R.layout.overview_layout, container,
//				false);
//		showOverviewAccountFragment();
//		showOverviewOperationListFragment();
//		return result;
//	}
	
//	private void showOverviewAccountFragment(){
//		Fragment fragment = new OverviewAccount();
//		
//		// Insert the fragment by replacing any existing fragment
//		getActivity().getSupportFragmentManager().beginTransaction()
//				.replace(R.id.frameAccountListOverview, fragment, OverviewAccount.FRAGMENT_TAG).commit();
//
//		
//	}
//	
//	private void showOverviewOperationListFragment(){
//		Fragment fragment = new OverviewOperation();
//		
//		// Insert the fragment by replacing any existing fragment
//		getActivity().getSupportFragmentManager().beginTransaction()
//				.replace(R.id.frameOperationListOverview, fragment, OverviewOperation.FRAGMENT_TAG).commit();
//
//		
//	}

}
