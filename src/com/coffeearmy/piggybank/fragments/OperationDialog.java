package com.coffeearmy.piggybank.fragments;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.auxiliar.Constant;
import com.coffeearmy.piggybank.data.OperationHandler;
import com.coffeearmy.piggybank.fragments.AccountDialog.OnDeleteAccountClickListener;
import com.coffeearmy.piggybank.fragments.AccountDialog.OnEditAccountClickListener;
import com.coffeearmy.piggybank.fragments.AccountDialog.OnSaveAccountClickListener;

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
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewSwitcher;

public class OperationDialog extends DialogFragment {
	
	public static final String FRAGMENT_TAG = "dialog_operation_tag";
	public static final String ADD_OPERATION_BROADCAST = "add_operation_broadcast";

	private ViewSwitcher mViewSwitcher;
	private EditText mEdtMoneyOperation;
	private ToggleButton mToggleSign;

	/** Returns an instance of the dialog */
	public static OperationDialog newInstance(int mode, Account account) {
		OperationDialog f = new OperationDialog();

		
		Bundle args = new Bundle();
		if (mode == 1) {// Edit Account
			args.putLong(Constant.ACCOUNT_ID, account.getId());
			args.putString(Constant.ACCOUNT_NAME, account.getName());
			f.setArguments(args);
		}
		// New Account

		return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.new_operation_dialog, null);

		mEdtMoneyOperation = (EditText) view.findViewById(R.id.edtQuantityOperation);
		mToggleSign =(ToggleButton) view.findViewById(R.id.tbtnAddSaves);
		
//		mViewSwitcher = (ViewSwitcher) view
//				.findViewById(R.id.ViewSwitcherAccountDialog);
		mToggleSign.setSelected(true);
		mEdtMoneyOperation.setHint("0.0");

		AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
				getActivity());
		if (getArguments()!=null) {
			alertBuilder//.setMessage("New Operation")
					.setPositiveButton("Add", new OnSaveOperationClickListener())
					.setNegativeButton("No way", null);
			
		} else {
			alertBuilder//.setMessage("Edit Operation")
					.setPositiveButton("Update", new OnEditOperationClickListener())
					.setNegativeButton("No way", null);
			
		}
		
		alertBuilder.setView(view);
		Dialog customDialog=alertBuilder.create();
		customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		return customDialog;
	}
	
	
	protected void Operate() {
		
		boolean isAddingOp = false;
		double cuantityFromStringInputEdit = 0.0;
		String amountfromEditText="";
		
		if (mToggleSign.isChecked()) {
			isAddingOp = true;
		}
		
		if(!isEmpty(mEdtMoneyOperation)){
			amountfromEditText=mEdtMoneyOperation.getText().toString();
		}

		cuantityFromStringInputEdit = Double.parseDouble(amountfromEditText);
		sendMessage(cuantityFromStringInputEdit, isAddingOp, 0);

	}
	private void sendMessage(Double cuantity, Boolean sign, int type) {

		Intent intent = new Intent(OperationHandler.BD_OPERATION);
		// You can also include some extra data.
		intent.setAction(ADD_OPERATION_BROADCAST);
		intent.putExtra(Constant.OPERATION_MONEY, cuantity);
		intent.putExtra(Constant.OPERATION_SING, sign);
		intent.putExtra(Constant.OPERATION_ICON, type);
		intent.putExtra(Constant.ACCOUNT_ID, getArguments().getLong(Constant.ACCOUNT_ID));

		EventBus.getDefault().post(intent);
	}

	
	/** Returns if the EditText is empty */
	private boolean isEmpty(EditText etText) {
		return etText.getText().toString().trim().length() == 0;
	}
	
	/** To be done: return the selected icon*/
	private int whichIconIsSelect() {

		return 0;
	}	
	
	//Listeners
	/** OnClick listener calls to new Account method*/
	protected class OnSaveOperationClickListener implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Operate(); 			
		}		
	}
	/** OnClick listener calls to edit/modify Account method*/
	protected class OnEditOperationClickListener implements DialogInterface.OnClickListener{

		@Override
		public void onClick(DialogInterface dialog, int which) {
			//editAccount();			
		}		
	}
	/** OnClick listener calls to delete Account method*/
	protected class OnDeleteAccountClickListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			//deleteAccountSendMenssage(getArguments().getLong(Constant.ACCOUNT_ID));			
		}		
	}


}
