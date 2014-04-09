package com.coffeearmy.piggybank.data;

import java.util.List;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.Operation;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class OperationLoader extends AsyncTaskLoader<List<Operation>> {

	private Context mContext;
	private Account mAccount;

	public OperationLoader(Context context,Account account) {
		super(context);
		mAccount=account;
	}

	@Override
	public List<Operation> loadInBackground() {		
		return  OperationHandler.getInstance(mContext).getOperationsFromAccountList(mAccount);
	}

}
