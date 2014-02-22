package com.coffeearmy.piggybank.data;

import java.util.List;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.Operation;

public interface OperationsManagerInterface {
	public List<Account> getAccounts();
	public void newAccount(Account a);
	public void deleteAccount(Account a);
	public void modifyAcccount(Account a);
	public List<Operation> getOperations(Account a);
	public void newOperation(Account a, Operation o);
	public void modifyOperation(Operation o);
	public void deleteOperation(Operation o);
}
