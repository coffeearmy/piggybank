package com.coffeearmy.piggybank.fragments;

import java.text.ParseException;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.auxiliar.Constant;
import com.coffeearmy.piggybank.data.OperationHandler;
import com.coffeearmy.piggybank.fragments.AccountDialog.OnClickSelectedTag;
import com.coffeearmy.piggybank.fragments.AccountDialog.OnDeleteAccountClickListener;
import com.coffeearmy.piggybank.fragments.AccountDialog.OnEditAccountClickListener;
import com.coffeearmy.piggybank.fragments.AccountDialog.OnIconIsSelected;
import com.coffeearmy.piggybank.fragments.AccountDialog.OnSaveAccountClickListener;
import com.coffeearmy.piggybank.view.CustomCheckIcon;
import com.coffeearmy.piggybank.view.CustomIcon;

import de.greenrobot.event.EventBus;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;
import android.widget.RadioGroup.OnCheckedChangeListener;

/** Dialog for create/edit/delete an Operation */
public class OperationDialog extends DialogFragment {

	public static final String FRAGMENT_TAG = "dialog_operation_tag";
	public static final String ADD_OPERATION_BROADCAST = "add_operation_broadcast";
	public static final String EDIT_OPERATION_BROADCAST = "edit_operation_broadcast";
	public static final String DELETE_OPERATION_BROADCAST = "delete_operation_broadcast";
	private static boolean mIsInEditMode;

	private ViewSwitcher mViewSwitcher;
	private EditText mEdtMoneyOperation;
	private ToggleButton mToggleSign;
	private TextView mDeleteOperation;
	public int mSelectedIcon;
	private RadioGroup mRadioGroup;
	private boolean mSignForEdit;
	private double mMoneyForEdit;
	private CustomIcon mCustomIcon;
	private int mSelectedBG;
	
	public OperationDialog() {}


	/** Returns an instance of the dialog */
	public static OperationDialog newInstance(int mode, Operation item,
			Account account) {
		OperationDialog f = new OperationDialog();
		mIsInEditMode = false;

		Bundle args = new Bundle();
		if (mode == 1) {// Edit Operation
			args.putLong(Constant.OPERATION_ID, item.getId());
			args.putDouble(Constant.OPERATION_MONEY, item.getMoney());
			args.putBoolean(Constant.OPERATION_SING, item.getSign());
			args.putLong(Constant.ACCOUNT_ID, account.getId());
			args.putInt(Constant.ACCOUNT_ICON, account.getIcon());
			args.putInt(Constant.OPERATION_ICON, item.getIcon());
			mIsInEditMode = true;
		} else {
			args.putLong(Constant.ACCOUNT_ID, account.getId());
		}
		// New Operation
		f.setArguments(args);
		return f;
	}
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		mSelectedIcon = 0;

		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.new_operation_dialog, null);

		mEdtMoneyOperation = (EditText) view
				.findViewById(R.id.edtQuantityOperation);
		mToggleSign = (ToggleButton) view.findViewById(R.id.tbtnAddSaves);

		mDeleteOperation = (TextView) view
				.findViewById(R.id.txtvDeleteOperation);
		mRadioGroup = (RadioGroup) view
				.findViewById(R.id.radioGroupOperationIcon);
		mRadioGroup.clearCheck();
		mRadioGroup.setOnCheckedChangeListener(new OnIconIsSelected());

		mViewSwitcher = (ViewSwitcher) view
				.findViewById(R.id.viewSwitcherOperationDialog);
		mToggleSign.setSelected(true);
		mEdtMoneyOperation.setHint("0.0");

		mCustomIcon = (CustomIcon) view
				.findViewById(R.id.imgSelectOperationTag);
		mCustomIcon.setOnClickListener(new OnClickSelectedTag());
		

		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
				getActivity());
		if (getArguments().size() == 1) {
			alertBuilder

			.setPositiveButton(R.string.add, new OnSaveOperationClickListener())
					.setNegativeButton(R.string.cancel, null);

		} else {
			alertBuilder

			.setPositiveButton(R.string.edit, new OnEditOperationClickListener())
					.setNegativeButton(R.string.cancel, null);
			setDialogReadyForEdit();
		}

		alertBuilder.setView(view);
		Dialog customDialog = alertBuilder.create();
		customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		if(savedInstanceState!=null){
			restoreState(savedInstanceState);
		}

		return customDialog;
	}
	
	private void restoreState(Bundle savedInstanceState) {
		mToggleSign.setChecked(savedInstanceState.getBoolean(Constant.OPERATION_DIALOG_SIGN_SAVE));
		mEdtMoneyOperation.setText(savedInstanceState.getString(Constant.OPERATION_DIALOG_MONEY_SAVE));
		mCustomIcon.setStyle(mSelectedBG, savedInstanceState.getInt(Constant.OPERATION_DIALOG_ICON_SAVE));
		
	}
	@Override
	 public void onDestroyView() {
	     if (getDialog() != null && getRetainInstance())
	         getDialog().setDismissMessage(null);
	         super.onDestroyView();
	 }

	@Override
	public void onSaveInstanceState(Bundle arg0) {
		
		arg0.putBoolean(Constant.OPERATION_DIALOG_SIGN_SAVE, mToggleSign.isChecked());
		arg0.putString(Constant.OPERATION_DIALOG_MONEY_SAVE, isEmpty(mEdtMoneyOperation)?"":mEdtMoneyOperation.getText().toString());
		arg0.putInt(Constant.OPERATION_DIALOG_ICON_SAVE, mSelectedIcon);
		super.onSaveInstanceState(arg0);
	}

	private void setDialogReadyForEdit() {
		mSignForEdit = getArguments().getBoolean(Constant.OPERATION_SING);
		mMoneyForEdit = getArguments().getDouble(Constant.OPERATION_MONEY);
		mSelectedIcon =  getArguments().getInt(Constant.OPERATION_ICON);
		mSelectedBG=getArguments().getInt(Constant.ACCOUNT_ICON);
		mCustomIcon.setStyle(mSelectedBG, mSelectedIcon);

		mToggleSign.setChecked(mSignForEdit);
		mEdtMoneyOperation.setText(Constant.DF.format(mMoneyForEdit));

		mViewSwitcher.showNext();
		mDeleteOperation
				.setOnClickListener(new onDeleteOperationClickListener());
	}

	protected void readFieldsEditOperation() {

		boolean isAddingOp = false;
		double cuantityFromStringInputEdit = 0.0;
		String amountfromEditText = "";

		if (mToggleSign.isChecked()) {
			isAddingOp = true;
		}

		if (!isEmpty(mEdtMoneyOperation)) {
			amountfromEditText = mEdtMoneyOperation.getText().toString();
			try {
				Number numericParse = Constant.DF.parse(amountfromEditText);
				cuantityFromStringInputEdit= Double.valueOf(numericParse.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}

		
		sendMessageForEditOperation(cuantityFromStringInputEdit, isAddingOp,
				mSelectedIcon);

	}

	private void sendMessageForEditOperation(double newMoney, boolean sign,
			int type) {
		Intent intent = new Intent(OperationHandler.BD_OPERATION);
		// You can also include some extra data.
		intent.setAction(EDIT_OPERATION_BROADCAST);
		intent.putExtra(Constant.OPERATION_MONEY, newMoney);
		intent.putExtra(Constant.OPERATION_SING, sign);
		intent.putExtra(Constant.OPERATION_ICON, type);
		intent.putExtra(Constant.OPERATION_SING_PREVIOUS, mSignForEdit);
		intent.putExtra(Constant.OPERATION_MONEY_PREVIOUS, mMoneyForEdit);
		intent.putExtra(Constant.ACCOUNT_ID,
				getArguments().getLong(Constant.ACCOUNT_ID));
		intent.putExtra(Constant.OPERATION_ID,
				getArguments().getLong(Constant.OPERATION_ID));

		EventBus.getDefault().post(intent);

	}

	protected void readFieldsNewOperation() {

		boolean isAddingOp = false;
		double cuantityFromStringInputEdit = 0.0;
		String amountfromEditText = "";

		if (mToggleSign.isChecked()) {
			isAddingOp = true;
		}

		if (!isEmpty(mEdtMoneyOperation)) {
			amountfromEditText = mEdtMoneyOperation.getText().toString();
			cuantityFromStringInputEdit = Double
					.parseDouble(amountfromEditText);
		}

		sendMessageNewOperation(cuantityFromStringInputEdit, isAddingOp,
				mSelectedIcon);

	}

	private void sendMessageNewOperation(Double cuantity, Boolean sign, int type) {

		Intent intent = new Intent(OperationHandler.BD_OPERATION);

		intent.setAction(ADD_OPERATION_BROADCAST);
		intent.putExtra(Constant.OPERATION_MONEY, cuantity);
		intent.putExtra(Constant.OPERATION_SING, sign);
		intent.putExtra(Constant.OPERATION_ICON, type);
		intent.putExtra(Constant.ACCOUNT_ID,
				getArguments().getLong(Constant.ACCOUNT_ID));

		EventBus.getDefault().post(intent);
	}

	/** Returns if the EditText is empty */
	private boolean isEmpty(EditText etText) {
		return etText.getText().toString().trim().length() == 0;
	}

	protected void readFieldsDeleteOperation() {

		boolean isAddingOp = false;
		double cuantityFromStringInputEdit = 0.0;
		String amountfromEditText = "";

		if (mToggleSign.isChecked()) {
			isAddingOp = true;
		}

		if (!isEmpty(mEdtMoneyOperation)) {
			amountfromEditText = mEdtMoneyOperation.getText().toString();
		}

		cuantityFromStringInputEdit = Double.parseDouble(amountfromEditText);
		sendMessageDeleteOperation(cuantityFromStringInputEdit, isAddingOp);

	}

	private void sendMessageDeleteOperation(Double cuantity, Boolean sign) {

		Intent intent = new Intent(OperationHandler.BD_OPERATION);

		intent.setAction(DELETE_OPERATION_BROADCAST);
		intent.putExtra(Constant.OPERATION_MONEY, cuantity);
		intent.putExtra(Constant.OPERATION_SING, sign);
		intent.putExtra(Constant.ACCOUNT_ID,
				getArguments().getLong(Constant.ACCOUNT_ID));
		intent.putExtra(Constant.OPERATION_ID,
				getArguments().getLong(Constant.OPERATION_ID));

		EventBus.getDefault().post(intent);
	}

	// Listeners
	/** OnClick listener calls to new Account method */
	protected class OnSaveOperationClickListener implements
			DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			readFieldsNewOperation();
		}
	}

	/** OnClick listener calls to edit/modify Account method */
	protected class OnEditOperationClickListener implements
			DialogInterface.OnClickListener {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			readFieldsEditOperation();
		}
	}

	/** OnClick listener calls to delete Account method */
	protected class onDeleteOperationClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			readFieldsDeleteOperation();
			dismiss();
		}
	}

	/** On click listener for choosing a icon for the operatio */
	protected class OnIconIsSelected implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			mSelectedIcon = ((CustomCheckIcon) group.findViewById(checkedId))
					.getIconSelected();
			if (mIsInEditMode) {
				mViewSwitcher.showPrevious();
				mCustomIcon.setStyle(mSelectedBG, mSelectedIcon);
			}

		}

	}

	/** OnClick listener calls to delete Account method */
	protected class OnClickSelectedTag implements OnClickListener {

		@Override
		public void onClick(View v) {
			mViewSwitcher.showNext();
		}
	}

}
