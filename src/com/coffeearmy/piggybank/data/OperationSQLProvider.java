package com.coffeearmy.piggybank.data;

import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.AccountDao;
import com.coffeearmy.piggybank.AccountDao.Properties;
import com.coffeearmy.piggybank.DaoMaster;
import com.coffeearmy.piggybank.DaoMaster.DevOpenHelper;
import com.coffeearmy.piggybank.DaoSession;
import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.OperationDao;
import com.coffeearmy.piggybank.PiggybankActivity;

import de.greenrobot.dao.query.QueryBuilder;

public class OperationSQLProvider implements OperationsManagerInterface {
	
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private AccountDao accountDao;
	private OperationDao operationDao;

	public OperationSQLProvider() {
		//Create Scheema 
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(PiggybankActivity.getContext(), "notes-db", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        accountDao= daoSession.getAccountDao();
        operationDao= daoSession.getOperationDao();
	}

	@Override
	public Cursor getAccounts() {
		String textColumn = AccountDao.Properties.Name.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        Cursor cursor = db.query(accountDao.getTablename(), accountDao.getAllColumns(), null, null, null, null, orderBy);
		return cursor;
	}
	@Override
	public List<Account> getAccountList() {
		return accountDao.loadAll();
	}
	
	@Override
	public Account getAccount(Long id) {
		QueryBuilder qb = accountDao.queryBuilder();
		qb.where(Properties.Id.eq(id));
		Account account = (Account) qb.uniqueOrThrow();
		return account;
	}

	@Override
	public void newAccount(Account a) {
		accountDao.insertOrReplace(a);
	}

	@Override
	public void deleteAccount(Account a) {
		accountDao.delete(a);

	}

	@Override
	public void modifyAcccount(Account a) {
		accountDao.insertOrReplace(a);

	}

	@Override
	public Cursor getOperations(Account a) {
		String textColumn = OperationDao.Properties.Date.columnName;
        String orderBy = textColumn + " COLLATE LOCALIZED ASC";
        //String select = OperationDao.Properties.AccountId.columnName+"="+a.getId();
        //Cursor cursor = db.query(operationDao.getTablename(), operationDao.getAllColumns(), select, null, null, null, orderBy);
		return null;//cursor;
	}
	@Override
	public List<Operation> getOperationsList(Account account) {
		return account.getOperations();
	}

	@Override
	public void newOperation(Account a, Operation o) {
		operationDao.insertOrReplace(o);
	}

	@Override
	public void modifyOperation(Operation o) {
		operationDao.insertOrReplace(o);
	}

	@Override
	public void deleteOperation(Operation o) {
		operationDao.delete(o);
	}

	@Override
	public void createBD() {}

	@Override
	public Double getAccountMoney(Account account) {
		return account.getMoney();
	}

	

	

}
