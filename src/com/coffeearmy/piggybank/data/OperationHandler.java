package com.coffeearmy.piggybank.data;

import java.util.List;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.Operation;

public class OperationHandler {
	
	OperationsManagerInterface dataProvider;
	
	public OperationHandler() {
		dataProvider= new OperationSQLProvider();
	}

	
	public List<Operation> getOperationsFromAcount(Account account) {
		// TODO Auto-generated method stub
		return null;
	}


	public Account getAccount(Float accountID) {
		// TODO Auto-generated method stub
		return null;
	}

}
