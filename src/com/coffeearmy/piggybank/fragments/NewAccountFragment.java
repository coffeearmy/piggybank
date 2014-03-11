package com.coffeearmy.piggybank.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.data.OperationHandler;

import de.greenrobot.event.EventBus;

public class NewAccountFragment extends Fragment{

	public static final String NEW_ACCOUNT_FRAGMENT_TAG = "New_account";
	public static final String TYPE = "type";
	public static final String INITIAL_CUANTITY = "initial_cuantity";
	public static final String ACCOUNT_NAME = "account_name";
	public static final String NEW_ACCOUNT_BROADCAST = "new_account_broadcast";

	private EditText edtAccountName;
	private EditText edtInitialCuantity;
	private View[] viewGrid;
	private View lastFocusedView;
	private int selectedItem;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View result = inflater.inflate(R.layout.new_account_dialog, container,
				false);
		edtAccountName = (EditText) result.findViewById(R.id.edt_Name_Account);
		edtInitialCuantity = (EditText) result.findViewById(R.id.edt_initial_cuantity);
		viewGrid= new View[8];
		viewGrid[0]= (View) result.findViewById(R.id.img_icon_1_proyect);
		viewGrid[1]= (View) result.findViewById(R.id.img_icon_2_proyect);
		viewGrid[2]= (View) result.findViewById(R.id.img_icon_3_proyect);
		viewGrid[3]= (View) result.findViewById(R.id.img_icon_4_proyect);
		viewGrid[4]= (View) result.findViewById(R.id.img_icon_5_proyect);
		viewGrid[5]= (View) result.findViewById(R.id.img_icon_6_proyect);
		viewGrid[6]= (View) result.findViewById(R.id.img_icon_7_proyect);
		viewGrid[7]= (View) result.findViewById(R.id.img_icon_8_proyect);
		setListenerViewGrid();
		viewGrid[0].requestFocus();	
		lastFocusedView=viewGrid[0];
		setHasOptionsMenu(true);
	
		((ActionBarActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		return result;
	}
	
	OnClickListener itemClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			selectedItem=(Integer) v.getTag();
			if(lastFocusedView!=null&&!lastFocusedView.equals(v))
			lastFocusedView.clearFocus();
			lastFocusedView=v;
			v.requestFocus();			
		}
	};
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.add_account_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.ok) {
			Toast.makeText(getActivity(), "ADDING buyaa!", Toast.LENGTH_LONG)
					.show();
			newAccount();
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void newAccount(){
		String accountNameText="";
		Double initialCuantityValue=0.;
		if(!isEmpty(edtAccountName))
		 accountNameText= edtAccountName.getText().toString();
		if(!isEmpty(edtInitialCuantity))
		 initialCuantityValue= Double.parseDouble(edtInitialCuantity.getText().toString());
		int type=whichIconIsSelect();
		sendMessage(accountNameText, initialCuantityValue, type);
	}
	
	
		// Send an Intent with an action named "OperationHandler.BD_OPERATION". The Intent sent
		// should
		// be received by the ReceiverActivity.
		private void sendMessage(String accountName, Double cuantity, int type) {
			
			Intent intent = new Intent(OperationHandler.BD_OPERATION);
			// You can also include some extra data.
			intent.setAction(NEW_ACCOUNT_BROADCAST);
			intent.putExtra(ACCOUNT_NAME, accountName);
			intent.putExtra(INITIAL_CUANTITY, cuantity);
			intent.putExtra(TYPE, type);
			
			EventBus.getDefault().post(intent);
			Log.i( NEW_ACCOUNT_FRAGMENT_TAG,"Broadcast send: New Account: "+ accountName+","+cuantity+","+type);
		}
	
	private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
	}
	private int whichIconIsSelect() {	
		
		return selectedItem;
	}
	
	public void setListenerViewGrid(){
		int i=0;
		for(View v:viewGrid){
			v.setOnClickListener(itemClickListener);
			v.setTag(i);
			i=i+1;
		}
	}
	
}
