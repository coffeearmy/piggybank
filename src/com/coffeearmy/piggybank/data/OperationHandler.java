package com.coffeearmy.piggybank.data;

import java.util.Date;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.AccountDao;
import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.PiggybankActivity;
import com.coffeearmy.piggybank.R;
import com.coffeearmy.piggybank.fragments.NewAccountFragment;
import com.coffeearmy.piggybank.fragments.NewOperationFragment;

import de.greenrobot.event.EventBus;

public class OperationHandler extends Fragment {

	public static final String BD_OPERATION = "new_Item";
	public static final String OPERATION_HANDLER_TAG = "operation_handler_tag";
	private static OperationHandler mOperationHandler;
	OperationsManagerInterface dataProvider;

	public OperationHandler(){
		dataProvider = new OperationSQLProvider();
	} 
	
	public static OperationHandler getInstance() {
		if (mOperationHandler == null) {
			mOperationHandler = new OperationHandler();
			
			return mOperationHandler;
		} else {
			return mOperationHandler;
		}
	}
	
	public void onEvent(Intent intent){
		if (intent.getAction().equals(
				NewAccountFragment.NEW_ACCOUNT_BROADCAST)) {
			newAccount(intent);
		} else {
			newOperation(intent);
		}
	}
		
		
	@Override
	public void onResume() {
		EventBus.getDefault().register(this);
		super.onResume();
	}

	
	@Override
	public void onPause() {
		EventBus.getDefault().unregister(this);
		super.onPause();
	}
	
	// Accounts
	/** Create a new Account from intent */
	private void newAccount(Intent intent) {

		String accountName = intent
				.getStringExtra(NewAccountFragment.ACCOUNT_NAME);

		Double initialCuantity = intent.getDoubleExtra(
				NewAccountFragment.INITIAL_CUANTITY, 0.);

		int accountType = intent.getIntExtra(NewAccountFragment.TYPE, 0);

		Account newAccount = new Account(null, accountName, initialCuantity,
				accountType, 0);

		dataProvider.newAccount(newAccount);
		storeOperation(newAccount, initialCuantity, true, 0);
	}

	/** Get an account from account ID */
	public Account getAccount(Long accountID) {
		return dataProvider.getAccount(accountID);
	}

	/** Get account money from account ID */
	public Double getAccountMoney(Long accountID) {
		return getAccount(accountID).getMoney();
	}

	/** Get account from money from an account */
	public Double getAccountMoney(Account account) {
		return dataProvider.getAccountMoney(account);
	}

	/** Get list of accounts */
	public Cursor getAccounts() {
		return dataProvider.getAccounts();
	}
	public List<Account> getAccountsList(){
		return dataProvider.getAccountList();
	}

	/** Modify an account */
	public void modifyAccount(Account account) {
		dataProvider.modifyAcccount(account);
	}

	/** Delete Account */
	public void deleteAccount(Account account) {
		dataProvider.deleteAccount(account);
	}

	/** Save current Money in a account */
	public void saveAccountCurrentMoney(Account account, Double currentMoney) {
		account.setMoney(currentMoney);
		dataProvider.modifyAcccount(account);
	}

	/** Get column names from the bd */
	public String[] getColumnNameAccounts() {
		String[] columnNames = { AccountDao.Properties.Name.columnName };
		return columnNames;
	}
	public String getAccountName(Account account) {
		return account.getName();
	}


	// Operations
	private void newOperation(Intent intent) {
		Account account = getAccount(intent.getLongExtra(
				NewOperationFragment.ACCOUNT_ID, -1));
		storeOperation(account,
				intent.getDoubleExtra(NewOperationFragment.CUANTITY, 0.),
				intent.getBooleanExtra(NewOperationFragment.OP_SIGN, true),
				intent.getIntExtra(NewOperationFragment.TYPE, 0));
	}

	public Cursor getOperationsFromAcount(Account account) {
		dataProvider.getOperations(account);
		return null;
	}
	
	public List<Operation> getOperationsFromAccountList(Account account){
		return dataProvider.getOperationsList(account);
	}

	public void storeOperation(Account account, Double cuantity, Boolean sign,
			int type) {
		int intsign = sign ? 0 : 1;
		Operation operation = new Operation(null, intsign, cuantity,
				new Date(), account.getId(), "", type);
		dataProvider.newOperation(account, operation);

	}


	
}
