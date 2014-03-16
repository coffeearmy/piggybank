package com.coffeearmy.piggybank.data;

import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.AccountDao;
import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.auxiliar.Constant;
import com.coffeearmy.piggybank.fragments.AccountDialog;
import com.coffeearmy.piggybank.fragments.AccountFragment;
import com.coffeearmy.piggybank.fragments.DrawerMenu;
import com.coffeearmy.piggybank.fragments.OperationDialog;

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
				AccountDialog.NEW_ACCOUNT_BROADCAST)) {
			newAccountFromIntent(intent);
			EventBus.getDefault().post(DrawerMenu.NOTIFY_CHANGE_ACCOUNT_LIST);
		} else if(intent.getAction().equals(
				AccountDialog.DELETE_ACCOUNT_BROADCAST)){
			deleteAccountFromIntent(intent);
			EventBus.getDefault().post(DrawerMenu.NOTIFY_CHANGE_ACCOUNT_LIST);
		} else if(intent.getAction().equals(
				AccountDialog.EDIT_ACCOUNT_BROADCAST)){
			editAccountFromIntent(intent);
			EventBus.getDefault().post(DrawerMenu.NOTIFY_CHANGE_ACCOUNT_LIST);
		}else if(intent.getAction().equals(
				OperationDialog.ADD_OPERATION_BROADCAST)){
			newOperation(intent);
			EventBus.getDefault().post(AccountFragment.NOTIFY_CHANGE_OPERATION_LIST);
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
	private void newAccountFromIntent(Intent intent) {

		String accountName = intent
				.getStringExtra(Constant.ACCOUNT_NAME);

		Double initialQuantity = intent.getDoubleExtra(
				Constant.ACCOUNT_MONEY, 0.);

		int accountType = intent.getIntExtra(Constant.ACCOUNT_ICON, 0);
		//The initial quantity will be add or subtract in the operation 
		Account newAccount = new Account(null, accountName, 0,
				accountType, 0);

		dataProvider.newAccount(newAccount);
		storeOperation(newAccount, initialQuantity, true, 0);
	}
	
	private void editAccountFromIntent(Intent intent) {
		Long accountID = intent.getLongExtra(Constant.ACCOUNT_ID, 0);
		Account account =getAccount(accountID);
		if(account!=null){
			account.setName(intent.getStringExtra(Constant.ACCOUNT_NAME));
			account.setIcon(intent.getIntExtra(Constant.ACCOUNT_ICON, 0));
			modifyAccount(account);
		}		
	}

	private void deleteAccountFromIntent(Intent intent) {		
		Long accountID = intent.getLongExtra(Constant.ACCOUNT_ID, 0);
		deleteAccount(getAccount(accountID));	
		///TODO redraw the list 
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
				Constant.ACCOUNT_ID, -1));
		storeOperation(account,
				intent.getDoubleExtra(Constant.OPERATION_MONEY, 0.),
				intent.getBooleanExtra(Constant.OPERATION_SING, true),
				intent.getIntExtra(Constant.OPERATION_ICON, 0));
	}

	public Cursor getOperationsFromAcountCursor(Account account) {
		dataProvider.getOperations(account);
		return null;
	}
	
	public List<Operation> getOperationsFromAccountList(Account account){
		return dataProvider.getOperationsList(account);
	}

	public void storeOperation(Account account, Double quantity, Boolean sign,
			int type) {
		int intsign = 0;
		if(sign){
			//Add
			intsign=0;
			account.setMoney(account.getMoney()+quantity);
		}else{
			//Substract
			intsign=1;
			account.setMoney(account.getMoney()-quantity);
		}
		Operation operation = new Operation(null, intsign, quantity,
				new Date(), account.getId(), "", type);
		dataProvider.modifyAcccount(account);
		dataProvider.newOperation(account, operation);

	}


	
}
