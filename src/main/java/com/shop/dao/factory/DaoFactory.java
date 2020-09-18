package com.shop.dao.factory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DaoFactory {
    private static final Logger logger = LogManager.getLogger(DaoFactory.class);

    private static final String MYSQL = "mysql";

    public static DaoFactory getDaoFactory() {
        return MySQLFactory.getInstance();
    }

    public static DaoFactory getDaoFactory(String databaseType) {
        switch (databaseType.toLowerCase()) {
            case MYSQL:
                return MySQLFactory.getInstance();
            default:
                logger.fatal("Database {} is not supported", databaseType);
                System.exit(-1);
                return null;
        }
    }
}
