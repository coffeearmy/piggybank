package com.coffeearmy.piggybank.data;

import java.util.List;

import android.database.Cursor;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.Operation;

public interface OperationsManagerInterface {
	//Crud Account
	public Cursor getAccounts();
	public void newAccount(Account a);
	public void deleteAccount(Account a);
	public void modifyAcccount(Account a);
	//CRUD operation
	public List<Operation> getOperations(Account a);
	public void newOperation(Account a, Operation o);
	public void modifyOperation(Operation o);
	public void deleteOperation(Operation o);
	//CRUD BD
	public void createBD();
	
}
