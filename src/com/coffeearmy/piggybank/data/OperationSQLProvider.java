package com.coffeearmy.piggybank.data;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.AccountDao;
import com.coffeearmy.piggybank.DaoMaster;
import com.coffeearmy.piggybank.DaoMaster.DevOpenHelper;
import com.coffeearmy.piggybank.DaoSession;
import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.PiggybankActivity;

public class OperationSQLProvider implements OperationsManagerInterface {
	
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private AccountDao accountDao;

	public OperationSQLProvider() {
		//Create Scheema 
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(PiggybankActivity.getContext(), "notes-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        accountDao= daoSession.getAccountDao();
        
	}

	@Override
	public Cursor getAccounts() {
		String textColumn = AccountDao.Properties.Name.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        Cursor cursor = db.query(accountDao.getTablename(), accountDao.getAllColumns(), null, null, null, null, orderBy);
		return cursor;
	}

	@Override
	public void newAccount(Account a) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAccount(Account a) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyAcccount(Account a) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Operation> getOperations(Account a) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void newOperation(Account a, Operation o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void modifyOperation(Operation o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteOperation(Operation o) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createBD() {
		// TODO Auto-generated method stub
		
	}

}
