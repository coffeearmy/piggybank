package com.coffeearmy.piggybank.data;

import java.util.Date;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.AccountDao;
import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.auxiliar.Constant;
import com.coffeearmy.piggybank.fragments.AccountDialog;
import com.coffeearmy.piggybank.fragments.AccountFragment;
import com.coffeearmy.piggybank.fragments.DrawerMenu;
import com.coffeearmy.piggybank.fragments.OperationDialog;

import de.greenrobot.event.EventBus;

/** Class that listen to the Eventbus and performs the operations with the database
 * @see https://github.com/greenrobot/EventBus
 */
public class OperationHandler extends Fragment {
	//Tag used in event bus for a interaction with the BD
	public static final String BD_OPERATION = "bd_operation";
	public static final String OPERATION_HANDLER_TAG = "operation_handler_tag";

	//We use an interface for communicate with the BD with Green DAO
	//If in the future we can use another DAO or external server
	OperationsManagerInterface dataProvider;

	public OperationHandler(){
		dataProvider = new OperationSQLProvider();
	} 
	
	//Singleton pattern
	private static OperationHandler mOperationHandler;
	public static OperationHandler getInstance() {
		if (mOperationHandler == null) {
			mOperationHandler = new OperationHandler();
			
			return mOperationHandler;
		} else {
			return mOperationHandler;
		}
	}
	
	/**
	 *  Class used by EventBus lib for listen events. The intent passed has the data for the operation,
	 *  and the action field in the intent specified the type of operation.
	 *  Eg: NEW_ACCOUNT_BROADCAST, create a new Account in the bd
	 *  
	 *  @param intent  Intent with the information for the operation
	 */
	public void onEvent(Intent intent){
		//New Account
		if (intent.getAction().equals(
				AccountDialog.NEW_ACCOUNT_BROADCAST)) {
			newAccountFromIntent(intent);
		
		//Delete Account	
		} else if(intent.getAction().equals(
				AccountDialog.DELETE_ACCOUNT_BROADCAST)){
			deleteAccountFromIntent(intent);
			
		//Edit Account	
		} else if(intent.getAction().equals(
				AccountDialog.EDIT_ACCOUNT_BROADCAST)){
			editAccountFromIntent(intent);
		
		//Add Operation	
		}else if(intent.getAction().equals(
				OperationDialog.ADD_OPERATION_BROADCAST)){
			newOperationFromIntent(intent);
			EventBus.getDefault().post(AccountFragment.NOTIFY_CHANGE_OPERATION_LIST);
			
		//Edit Operation		
		}else if(intent.getAction().equals(
				OperationDialog.EDIT_OPERATION_BROADCAST)){
			editOperationFromIntent(intent);
			EventBus.getDefault().post(AccountFragment.NOTIFY_CHANGE_OPERATION_LIST);
		
		//Delete Operation	
		}else if(intent.getAction().equals(
				OperationDialog.DELETE_OPERATION_BROADCAST)){
			deleteOperationFromIntent(intent);
			EventBus.getDefault().post(AccountFragment.NOTIFY_CHANGE_OPERATION_LIST);
		}
		//We send a broadcast event for refresh lists.
		EventBus.getDefault().post(DrawerMenu.NOTIFY_CHANGE_ACCOUNT_LIST);
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
				accountType, accountType);

		dataProvider.newAccount(newAccount);
		storeOperation(newAccount, initialQuantity, true, 0);
	}
	
	/** Edit Account from Intent*/
	private void editAccountFromIntent(Intent intent) {
		Long accountID = intent.getLongExtra(Constant.ACCOUNT_ID, 0);
		Account account =getAccount(accountID);
		if(account!=null){
			account.setName(intent.getStringExtra(Constant.ACCOUNT_NAME));
			account.setIcon(intent.getIntExtra(Constant.ACCOUNT_ICON, 0));
			modifyAccount(account);
		}		
	}
	/** Delete Account from Intent */
	private void deleteAccountFromIntent(Intent intent) {		
		Long accountID = intent.getLongExtra(Constant.ACCOUNT_ID, 0);
		deleteAccount(getAccount(accountID));	
		
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
		return account.getMoney();
	}

	/** Get list of accounts */
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
	
	/** Get Account Name from account */
	public String getAccountName(Account account) {
		return account.getName();
		
	}


	// Operations
	/** Create a New Operation from Intent */
	private void newOperationFromIntent(Intent intent) {
		Account account = getAccount(intent.getLongExtra(
				Constant.ACCOUNT_ID, -1));
		storeOperation(account,
				intent.getDoubleExtra(Constant.OPERATION_MONEY, 0.),
				intent.getBooleanExtra(Constant.OPERATION_SING, true),
				intent.getIntExtra(Constant.OPERATION_ICON, 0));
		//Log.d(this.BD_OPERATION, "newOperationfromIntente icon "+ intent.getIntExtra(Constant.OPERATION_ICON, 0));
	}
	
	/** Edit an Operation from Intent  */
	private void editOperationFromIntent(Intent intent) {
		Account account = getAccount(intent.getLongExtra(
				Constant.ACCOUNT_ID, -1));
		editOperation(account,intent.getLongExtra(Constant.OPERATION_ID, 0),
				intent.getDoubleExtra(Constant.OPERATION_MONEY, 0.),
				intent.getBooleanExtra(Constant.OPERATION_SING, true),
				intent.getIntExtra(Constant.OPERATION_ICON, 0),
				intent.getDoubleExtra(Constant.OPERATION_MONEY_PREVIOUS, 0.),
				intent.getBooleanExtra(Constant.OPERATION_SING_PREVIOUS, true));
	}
	
	/** Delete Operation from Intent*/
	private void deleteOperationFromIntent(Intent intent) {		
		deleteOperation(intent.getLongExtra(Constant.OPERATION_ID, 0),
				intent.getLongExtra(Constant.ACCOUNT_ID, -1),
				intent.getDoubleExtra(Constant.OPERATION_MONEY, 0.),
				intent.getBooleanExtra(Constant.OPERATION_SING, true));				
	}
	
	/** Edit Operation*/ 
	private void editOperation(Account account, long operationID,
			double newMoney, boolean newSign, int newIcon,
			double oldMoney, boolean oldSign) {
			//Undo the last operation
			undoOperation(account, oldMoney, oldSign);
			dataProvider.modifyAcccount(account);
			//Do the new operation
			doOperation(account, newMoney, newSign);
			dataProvider.modifyAcccount(account);
			//Edit operation
			Operation operation =dataProvider.getOperation(operationID);
			operation.setMoney(newMoney);
			operation.setSign(newSign);
			operation.setIcon(newIcon);
			//Store changes
			dataProvider.modifyOperation(operation);
			//dataProvider.modifyAcccount(account);
	}


	/** Delete the operation*/
	private void deleteOperation(long operationID,long accountID, Double money, boolean sign) {
		
		Account account = dataProvider.getAccount(accountID);
		
		dataProvider.deleteOperation(dataProvider.getOperation(operationID));	
		undoOperation(account, money, sign);
		//Store changes
		dataProvider.modifyAcccount(account);
	}

	/** Store the operation*/
	public void storeOperation(Account account, Double quantity, Boolean sign,
			int type) {
		
		if(sign){
			//Add
			
			account.setMoney(account.getMoney()+Math.abs(quantity));
		}else{
			//Substract
			
			account.setMoney(account.getMoney()-Math.abs(quantity));
		}
		Log.d(this.BD_OPERATION, "storeOperation"+ account.getMoney());
		Operation operation = new Operation(null, sign, quantity,
				new Date(), account.getId(), "", type);
		dataProvider.modifyAcccount(account);
		dataProvider.newOperation(account, operation);
		

	}
	
	/** Undo an operation to a account */
	private Account undoOperation(Account account, Double money, boolean sign){
		
		if(sign){ //The op was adding
			account.setMoney(account.getMoney()-Math.abs(money));			
		}else{
			account.setMoney(account.getMoney()+Math.abs(money));		
		}		
		return account;
	}
	
	/** Do an operation to a account */
	private Account doOperation(Account account, Double money, boolean sign){
		
		if(sign){ //The op is adding
			account.setMoney(account.getMoney()+ Math.abs(money));			
		}else{
			account.setMoney(account.getMoney()-Math.abs(money));		
		}		
		return account;
	}
    
	//Get operation Methods 
	
	public List<Operation> getLastOperationList() {
		return dataProvider.getLastOperationList();
	}

	public List<Operation> getOperationsFromAccountList(Account account){
		return dataProvider.getOperationsList(account);
	}
	
}
