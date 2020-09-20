package com.shop.dao.factory;

import com.shop.config.Constants;
import com.shop.dao.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DaoFactory {
    private static final Logger logger = LogManager.getLogger(DaoFactory.class);

    public static DaoFactory getDaoFactory() {
        return MySQLFactory.getInstance();
    }

    public static DaoFactory getDaoFactory(String databaseType) {
        switch (databaseType.toLowerCase()) {
            case Constants.MYSQL:
                return MySQLFactory.getInstance();
            case Constants.H2:
                return H2Factory.getInstance();
            default:
                logger.fatal("Database {} is not supported", databaseType);
                System.exit(-1);
                return null;
        }
    }

    public abstract UserDao getUserDao();
}
