package com.coffeearmy.piggybank;

import java.util.List;
import com.coffeearmy.piggybank.DaoSession;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table ACCOUNT.
 */
public class Account {

    private Long id;
    /** Not-null value. */
    private String name;
    private double money;
    private Integer extraType;
    private Integer icon;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient AccountDao myDao;

    private List<Operation> operations;

    public Account() {
    }

    public Account(Long id) {
        this.id = id;
    }

    public Account(Long id, String name, double money, Integer extraType, Integer icon) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.extraType = extraType;
        this.icon = icon;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getAccountDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Integer getExtraType() {
        return extraType;
    }

    public void setExtraType(Integer extraType) {
        this.extraType = extraType;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<Operation> getOperations() {
        if (operations == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            OperationDao targetDao = daoSession.getOperationDao();
            List<Operation> operationsNew = targetDao._queryAccount_Operations(id);
            synchronized (this) {
                if(operations == null) {
                    operations = operationsNew;
                }
            }
        }
        return operations;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetOperations() {
        operations = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
