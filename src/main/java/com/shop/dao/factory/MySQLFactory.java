package com.shop.dao.factory;

import com.shop.dao.UserDao;
import com.shop.dao.impl.mysql.MySQLUserDao;

public class MySQLFactory extends DaoFactory {
    private static MySQLFactory instance;

    private MySQLFactory() {
        //object created via getInstance()
    }

    public static synchronized MySQLFactory getInstance() {
        if (instance == null)
            instance = new MySQLFactory();
        return instance;
    }

    public UserDao getUserDao() {
        return new MySQLUserDao();
    }
}
