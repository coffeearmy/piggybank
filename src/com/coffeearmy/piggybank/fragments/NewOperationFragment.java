package com.coffeearmy.piggybank.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.data.OperationHandler;

import de.greenrobot.event.EventBus;

public class NewOperationFragment extends Fragment {
	public static final String FRAG_NEW_OPERATION_TAG = "FRAG_NEWOPERATION_TAG";
	public static final String ADD_OPERATION_BROADCAST = "ADD_OP_BROADCAST";
	public final static String CUANTITY = "CUANTITY";
	public static final String OP_SIGN = "OP_SIGN";
	public static final String TYPE = "TYPE";
	public static final String ACCOUNT_ID = "ACCOUNT_ID";
	private ToggleButton mSignToggle;
	private EditText mInputCuantity;
	private long mAccountID;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.new_operation_layout,
				container, false);
		InputViewSetup(result);
		

		mAccountID = this.getArguments().getLong(ACCOUNT_ID);
		((ActionBarActivity) getActivity()).getSupportActionBar()
				.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE 
				        | ActionBar.DISPLAY_SHOW_HOME 
				        | ActionBar.DISPLAY_HOME_AS_UP);
		setHasOptionsMenu(true);
		return result;
	}

	public void InputViewSetup(View inputView) {

		mSignToggle = (ToggleButton) inputView.findViewById(R.id.ibtnAddSaves);
		
		mInputCuantity = (EditText) inputView.findViewById(R.id.edtCuantity);
		mSignToggle.setSelected(true);
		mInputCuantity.setHint("0.0");

	}

	protected void Operate(String sign, String value) {
		boolean isAddingOp = false;
		double cuantityFromStringInputEdit = 0.0;
		if (mSignToggle.isChecked()) {
			isAddingOp = true;
		}

		cuantityFromStringInputEdit = Double.parseDouble(value);
		sendMessage(cuantityFromStringInputEdit, isAddingOp, 0);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.add_operation_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.addButton:
			if (mInputCuantity.length() != 0) {
				Operate("+", mInputCuantity.getText().toString());
			}
			break;
		case android.R.id.home:
			getActivity().getSupportFragmentManager().popBackStack();
			return true;

		default:

			break;
		}
		return super.onOptionsItemSelected(item);
	}

	// Send an Intent with an action named "custom-event-name". The Intent sent
	// should
	// be received by the ReceiverActivity.
	private void sendMessage(Double cuantity, Boolean sign, int type) {

		Intent intent = new Intent(OperationHandler.BD_OPERATION);
		// You can also include some extra data.
		intent.setAction(ADD_OPERATION_BROADCAST);
		intent.putExtra(CUANTITY, cuantity);
		intent.putExtra(OP_SIGN, sign);
		intent.putExtra(TYPE, type);
		intent.putExtra(ACCOUNT_ID, mAccountID);

		EventBus.getDefault().post(intent);
	}
}
