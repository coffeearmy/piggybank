package com.coffeearmy.piggybank.data;

import java.util.List;

import android.database.sqlite.SQLiteDatabase;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.AccountDao;
import com.coffeearmy.piggybank.DaoMaster;
import com.coffeearmy.piggybank.DaoMaster.DevOpenHelper;
import com.coffeearmy.piggybank.DaoSession;
import com.coffeearmy.piggybank.Operation;
import com.coffeearmy.piggybank.OperationDao;
import com.coffeearmy.piggybank.PiggybankActivity;

/** Class accessing the BD implement OperationManagerInterface.java, with GreenDAO
 *  @see https://github.com/greenrobot/greenDAO
 */
public class OperationSQLProvider implements OperationsManagerInterface {
	
	private SQLiteDatabase db;
	private DaoMaster daoMaster;
	private DaoSession daoSession;
	private AccountDao accountDao;
	private OperationDao operationDao;

	public OperationSQLProvider() {
		//Create Schema 
		DevOpenHelper helper = new DaoMaster.DevOpenHelper(PiggybankActivity.getContext(), "notes-db-v2", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        accountDao= daoSession.getAccountDao();
        operationDao= daoSession.getOperationDao();
	}

	
	@Override
	public List<Account> getAccountList() {
		daoSession.clear();
		return accountDao.loadAll();
	}
	
	@Override
	public Account getAccount(Long id) {
		accountDao.refresh(accountDao.load(id));
		return accountDao.load(id);
	}
	

	@Override
	public void newAccount(Account a) {
		accountDao.insertOrReplace(a);
		
	}

	@Override
	public void deleteAccount(Account a) {
		List<Operation> listOperations = getOperationsList(a);
		for (Operation o: listOperations){
			operationDao.delete(o);
		}
		accountDao.delete(a);

	}

	@Override
	public void modifyAcccount(Account a) {
		accountDao.update(a);	
		
	}

	
	@Override
	public List<Operation> getOperationsList(Account account) {
		daoSession.clear();
		return operationDao.queryBuilder().where(OperationDao.Properties.AccountId.eq(account.getId()))
				.orderDesc(OperationDao.Properties.Date)
				.list();
		}

	@Override
	public void newOperation(Account account, Operation operation) {
		//In greenDAO document: ensure the list is cached
		List<Operation> operationList = getOperationsList(account);
		operation.setAccount(account);
		operation.setAccountId(account.getId());
		operationDao.insert(operation);		
		operationList.add(operation);	
		accountDao.update(account);
	}

	@Override
	public void modifyOperation(Operation o) {
		operationDao.insertOrReplace(o);
		
	}

	@Override
	public void deleteOperation(Operation o) {
		operationDao.delete(o);
	}


	
	public List<Operation> getLastOperationList(){
	 return operationDao.queryBuilder()
				.orderDesc(OperationDao.Properties.Date)
				.list();
		
	}

	@Override
	public void deleteOperation(long operationID) {
		operationDao.deleteByKey(operationID);
		
	}

	@Override
	public Operation getOperation(long operationID) {
		return operationDao.load(operationID);
	}

}
