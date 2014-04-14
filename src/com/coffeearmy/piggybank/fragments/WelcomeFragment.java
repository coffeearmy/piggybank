package com.coffeearmy.piggybank.fragments;

import com.coffeearmy.piggybank.R;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class WelcomeFragment extends Fragment {

		public static final String FRAGMENT_TAG = "welcome_tag";
		private FragmentManager mFragmentManager;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			mFragmentManager=getActivity().getSupportFragmentManager();
			 View welcomeView = inflater.inflate(R.layout.welcome_view, null);
				// SetUp emptyView
				Button emptyButton = (Button) welcomeView.findViewById(R.id.btnEmpty);
				emptyButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						showNewAccountFragment();
					}
				});
			return welcomeView;
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
}
