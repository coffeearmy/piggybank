package com.coffeearmy.piggybank.fragments;

import com.coffeearmy.piggybank.R;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ViewSwitcher;

public class AccountDialog extends DialogFragment {

	public static final String FRAGMENT_TAG = "dialog_tag";
	private Button mButtonOK;
	private ViewSwitcher mViewSwitcher;
	private EditText mEdtAccountName;
	private EditText mEdtInitialCuantity;
	private Button mButtonCancel;

	public static AccountDialog newInstance(int mode, String name, long money,
			int icon, long accountID) {
		AccountDialog f = new AccountDialog();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		if (mode == 1) {// Edit Account
			args.putString("name", name);
			args.putLong("accountID", accountID);
			args.putLong("money", money);
			args.putInt("icon", icon);
			f.setArguments(args);
		}
		// New Account

		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.new_account_dialog, container);
		mButtonOK = (Button) view.findViewById(R.id.btn_ok_account_dialog);
		mButtonCancel = (Button) view
				.findViewById(R.id.btn_cancel_account_dialog);
		mEdtAccountName = (EditText) view.findViewById(R.id.edt_Name_Account);
		mEdtInitialCuantity = (EditText) view
				.findViewById(R.id.edt_initial_cuantity);
		mViewSwitcher = (ViewSwitcher) view
				.findViewById(R.id.ViewSwitcherAccountDialog);
		mButtonOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTagView();

			}
		});
		// mEditText = (EditText) view.findViewById(R.id.txt_your_name);

		// if( getResources())
		// getDialog().setTitle("Edit Account");
		// else
		getDialog().setTitle("New Account");

		return view;
	}

	/**  */
	public void showTagView() {
		// Store data

		// Switch to the next View
		mViewSwitcher.showNext();

	}

}
