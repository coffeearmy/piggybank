package com.coffeearmy.piggybank.data;

import java.util.List;

import android.database.Cursor;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.Operation;
/** Interface for access the BD. */
public interface OperationsManagerInterface {
	//CRUD Account
	
	public List<Account> getAccountList();
	public void newAccount(Account a);
	public void deleteAccount(Account a);
	public void modifyAcccount(Account a);
	public Account getAccount(Long id);
	
	//CRUD operation
	
	public void newOperation(Account a, Operation o);
	public void modifyOperation(Operation o);
	public void deleteOperation(Operation o);
	public List<Operation> getOperationsList(Account account);
	public List<Operation> getLastOperationList();	
	public void deleteOperation(long operationID);
	public Operation getOperation(long operationID);	
	
	
}
