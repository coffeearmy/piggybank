package com.coffeearmy.piggybank.data;

import java.util.List;

import com.coffeearmy.piggybank.Account;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class AccountLoader extends AsyncTaskLoader<List<Account>> {

	private Context mContext;

	public AccountLoader(Context context) {
		super(context);
		mContext=context;
	}

	@Override
	public List<Account> loadInBackground() {
	
		return 	OperationHandler.getInstance(mContext).getAccountsList();
	}

}
