package com.shop.dao.factory;

import com.shop.config.Constants;
import com.shop.dao.CategoryDao;
import com.shop.dao.ProductDao;
import com.shop.dao.UserDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code DaoFactory} class is a base class for SQL DAO factories.
 * The main purpose of the class is to get an appropriate type of factory according to the database type.
 * @author Anton Makarenko
 */

public abstract class DaoFactory {
    private static final Logger logger = LogManager.getLogger(DaoFactory.class);

    /**
     * Returns {@code DaoFactory} for MySQL database.
     * @return instance of {@code MySQLFactory}
     */
    public static DaoFactory getDaoFactory() {
        return MySQLFactory.getInstance();
    }

    /**
     * Returns {@code DaoFactory} for one of supported databases. If an appropriate DAO factory is not found, the program exits with status code -1.
     * @param databaseType name of one of supported database types. Supported types are MySQL for deployment and H2 for test.
     * @return instance of {@code MySQLFactory} or {@code H2Factory}
     */
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
    public abstract ProductDao getProductDao();
    public abstract CategoryDao getCategoryDao();
}
