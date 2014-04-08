package com.coffeearmy.piggybank.auxiliar;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.fragments.AccountFragment;
import com.coffeearmy.piggybank.fragments.OverviewFragment;

public class FragmentNavigation {
	
	/** Show the Overview Fragment */
	protected static void showOverviewFragment(FragmentManager fm) {
		Fragment fragment = OverviewFragment.newInstance();

		fm.beginTransaction()
			.add(R.id.content_frame, fragment,
				OverviewFragment.FRAGMENT_TAG).commit();
	}
	
	/**
	 * Show the detail Fragment with the data from the account
	 * */
	protected static void showAccountFragment(Long ID, FragmentManager fm) {

		// Create a new fragment and specify the planet to show based on
		// position
		Fragment fragment = AccountFragment.newInstance(ID);

		// Insert the fragment by replacing any existing fragment
				fm.beginTransaction()
				.replace(R.id.content_frame, fragment,
						AccountFragment.ACCOUNT_FRAGMENT_TAG).commit();		
	}
	
	public static void showFragment(Fragment fragment,String tag,FragmentManager fm){
		fm.beginTransaction()
		.replace(R.id.content_frame, fragment,
				tag).commit();		
	}

}
