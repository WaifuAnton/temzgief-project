package com.shop.dao.factory;

import com.shop.config.Constants;
import com.shop.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DaoFactory {
    private static final Logger logger = LogManager.getLogger(DaoFactory.class);

    public static DaoFactory getDaoFactory() {
        switch (PropertyReader.readType().toLowerCase()) {
            case Constants.MYSQL:
                return MySQLFactory.getInstance();
            default:
                logger.fatal("Database {} is not supported", PropertyReader.readType());
                System.exit(-1);
                return null;
        }
    }

    public static DaoFactory getDaoFactory(String databaseType) {
        switch (databaseType.toLowerCase()) {
            case Constants.MYSQL:
                return MySQLFactory.getInstance();
            default:
                return getDaoFactory();
        }
    }
}
