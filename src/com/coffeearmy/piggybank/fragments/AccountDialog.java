package com.coffeearmy.piggybank.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.auxiliar.Constant;
import com.coffeearmy.piggybank.auxiliar.FragmentNavigation;
import com.coffeearmy.piggybank.data.OperationHandler;
import com.coffeearmy.piggybank.view.CustomCheckIcon;
import com.coffeearmy.piggybank.view.CustomIcon;

import de.greenrobot.event.EventBus;

/** Dialog for create/edit/delete an Account */
public class AccountDialog extends DialogFragment {

	public static final String FRAGMENT_TAG = "dialog_tag";

	// Actions used in the broadcasts events
	public static final String NEW_ACCOUNT_BROADCAST = "new_account_broadcast";
	public static final String DELETE_ACCOUNT_BROADCAST = "delete_account_broadcast";
	public static final String EDIT_ACCOUNT_BROADCAST = "edit_account_broadcast";

	private ViewSwitcher mViewSwitcher;
	private EditText mEdtAccountName;
	private EditText mEdtInitialQuantity;
	private TextView mTxtvDeleteAccount;
	private CustomIcon mCustomIcon;
	private RadioGroup mRadioGroup;

	public int mSelectedStyle = 0;

	private FragmentManager mFragmentManager;

	private boolean mIsEdit;

	/** Returns an instance of the dialog */
	public static AccountDialog newInstance(int mode, Account account) {
		AccountDialog f = new AccountDialog();

		Bundle args = new Bundle();
		if (mode == 1) {// Edit Account
			args.putLong(Constant.ACCOUNT_ID, account.getId());
			args.putString(Constant.ACCOUNT_NAME, account.getName());
			f.setArguments(args);
		}

		return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.new_account_dialog, null);
		
		//Is the dialog for edit a piggybank account
		mIsEdit=getArguments()!=null?true:false;
		
		// Fragment Manager
		mFragmentManager = getActivity().getSupportFragmentManager();

		mEdtAccountName = (EditText) view.findViewById(R.id.edtNameAccount);
		mEdtInitialQuantity = (EditText) view
				.findViewById(R.id.edtInitialQuantity);
		mTxtvDeleteAccount = (TextView) view
				.findViewById(R.id.txtvDeleteAccount);
		mViewSwitcher = (ViewSwitcher) view
				.findViewById(R.id.viewSwitcherAccountDialog);

		// RadioButton for choose the icon used in the account
		mRadioGroup = (RadioGroup) view
				.findViewById(R.id.radioGroupAccountIcon);
		mRadioGroup.clearCheck();
		mRadioGroup.setOnCheckedChangeListener(new OnIconIsSelected());

		mCustomIcon = (CustomIcon) view.findViewById(R.id.imgSelectTag);

		mCustomIcon.setOnClickListener(new OnClickSelectedTag());

		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
				getActivity());
		// Set buttons to the dialog
		if (!mIsEdit) {
			alertBuilder.setPositiveButton(R.string.add,
					new OnSaveAccountClickListener()).setNegativeButton(
					R.string.cancel, null);
			mTxtvDeleteAccount.setVisibility(View.GONE);
		} else {
			alertBuilder.setPositiveButton(R.string.edit,
					new OnEditAccountClickListener()).setNegativeButton(
							R.string.cancel, null);	
			mEdtInitialQuantity.setVisibility(View.GONE);
		}
		//Fill the dialog fields if is needed
		if(savedInstanceState!=null){
			restoreState(savedInstanceState);
		}else{
			if(mIsEdit){
				prepareEditDialog(getArguments());
			}
		}

		alertBuilder.setView(view);
		Dialog customDialog = alertBuilder.create();
		customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		return customDialog;
	}
	
	private void prepareEditDialog(Bundle arguments) {
		mEdtAccountName.setText(arguments.getString(
				Constant.ACCOUNT_NAME));		
		mTxtvDeleteAccount
				.setOnClickListener(new OnDeleteAccountClickListener());
	}

	private void restoreState(Bundle savedInstanceState) {		
			mEdtAccountName.setText(savedInstanceState.getString(Constant.ACCOUNT_DIALOG_NAME_FIELD));
			mEdtInitialQuantity.setText(savedInstanceState.getString(Constant.ACCOUNT_DIALOG_MONEY_FIELD));
			mCustomIcon.setStyle(savedInstanceState.getInt(Constant.ACCOUNT_DIALOG_ICON_FIELD), -1);
					
	}

	@Override
	public void onSaveInstanceState(Bundle arg0) {
		
		//Dialog fields
		arg0.putString(Constant.ACCOUNT_DIALOG_NAME_FIELD, isEmpty(mEdtAccountName)?"":mEdtAccountName.toString());
		arg0.putString(Constant.ACCOUNT_DIALOG_MONEY_FIELD, isEmpty(mEdtInitialQuantity)?"":mEdtInitialQuantity.toString());
		arg0.putInt(Constant.ACCOUNT_DIALOG_ICON_FIELD, mSelectedStyle);
		
		super.onSaveInstanceState(arg0);
	}
	
	
	/** Method reads the fields of the new account */
	private void newAccount() {
		String accountNameText = "";
		Double initialQuantityValue = 0.;

		if (!isEmpty(mEdtAccountName))
			accountNameText = mEdtAccountName.getText().toString();

		if (!isEmpty(mEdtInitialQuantity))
			initialQuantityValue = Double.parseDouble(mEdtInitialQuantity
					.getText().toString());

		addAccountSendMessage(accountNameText, initialQuantityValue,
				mSelectedStyle);
	}

	/** Method reads the fields of the modified account */
	private void editAccount() {
		Long accountId = getArguments().getLong(Constant.ACCOUNT_ID);
		String accountNameText = getArguments()
				.getString(Constant.ACCOUNT_NAME);
		if (!isEmpty(mEdtAccountName))
			accountNameText = mEdtAccountName.getText().toString();
		editAccount(accountId, accountNameText, mSelectedStyle);
	}

	/** Send a message with the modified fields */
	private void editAccount(Long accountId, String accountName, int type) {
		Intent intent = new Intent(OperationHandler.BD_OPERATION);

		intent.setAction(EDIT_ACCOUNT_BROADCAST);
		intent.putExtra(Constant.ACCOUNT_ID, accountId);
		intent.putExtra(Constant.ACCOUNT_NAME, accountName);
		intent.putExtra(Constant.ACCOUNT_ICON, type);

		EventBus.getDefault().post(intent);

	}

	/** Send a message with the fields of the new account */
	private void addAccountSendMessage(String accountName, Double quantity,
			int type) {

		Intent intent = new Intent(OperationHandler.BD_OPERATION);

		intent.setAction(NEW_ACCOUNT_BROADCAST);
		intent.putExtra(Constant.ACCOUNT_NAME, accountName);
		intent.putExtra(Constant.ACCOUNT_MONEY, quantity);
		intent.putExtra(Constant.ACCOUNT_ICON, type);

		EventBus.getDefault().post(intent);

	}

	/** Send a message with the information for deleting an account */
	private void deleteAccountSendMenssage(Long accountId) {
		Intent intent = new Intent(OperationHandler.BD_OPERATION);

		intent.setAction(DELETE_ACCOUNT_BROADCAST);
		intent.putExtra(Constant.ACCOUNT_ID, accountId);

		EventBus.getDefault().post(intent);

	}

	/** Returns if the EditText is empty */
	private boolean isEmpty(EditText etText) {
		return etText.getText().toString().trim().length() == 0;
	}

	// Listeners
	/** OnClick listener calls to new Account method */
	protected class OnSaveAccountClickListener implements
			DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			newAccount();
		}
	}

	/** OnClick listener calls to edit/modify Account method */
	protected class OnEditAccountClickListener implements
			DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			editAccount();
		}
	}

	/** OnClick listener calls to delete Account method */
	protected class OnDeleteAccountClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			deleteAccountSendMenssage(getArguments().getLong(
					Constant.ACCOUNT_ID));
			//When delete an account show the overview Fragment
			FragmentNavigation.showFragment(OverviewFragment.newInstance(),
					OverviewFragment.FRAGMENT_TAG, mFragmentManager);
			dismiss();
		}
	}

	/** OnClick listener calls to delete Account method */
	protected class OnClickSelectedTag implements OnClickListener {

		@Override
		public void onClick(View v) {
			mViewSwitcher.showNext();
		}
	}

	/** On checked listener to select the icon for the account */
	protected class OnIconIsSelected implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			mSelectedStyle = ((CustomCheckIcon) group.findViewById(checkedId))
					.getStyle();
			mCustomIcon.setStyle(mSelectedStyle, -1); // Icon is -1 because only
														// need the bg
			mViewSwitcher.showPrevious();

		}

	}

}
