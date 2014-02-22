package com.coffeearmy.piggybank;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.coffeearmy.piggybank.Account;
import com.coffeearmy.piggybank.Operation;

import com.coffeearmy.piggybank.AccountDao;
import com.coffeearmy.piggybank.OperationDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig accountDaoConfig;
    private final DaoConfig operationDaoConfig;

    private final AccountDao accountDao;
    private final OperationDao operationDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        accountDaoConfig = daoConfigMap.get(AccountDao.class).clone();
        accountDaoConfig.initIdentityScope(type);

        operationDaoConfig = daoConfigMap.get(OperationDao.class).clone();
        operationDaoConfig.initIdentityScope(type);

        accountDao = new AccountDao(accountDaoConfig, this);
        operationDao = new OperationDao(operationDaoConfig, this);

        registerDao(Account.class, accountDao);
        registerDao(Operation.class, operationDao);
    }
    
    public void clear() {
        accountDaoConfig.getIdentityScope().clear();
        operationDaoConfig.getIdentityScope().clear();
    }

    public AccountDao getAccountDao() {
        return accountDao;
    }

    public OperationDao getOperationDao() {
        return operationDao;
    }

}
