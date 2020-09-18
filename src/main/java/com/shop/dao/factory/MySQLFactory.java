package com.shop.dao.factory;

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
}
