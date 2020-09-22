package com.shop.dao.factory;

import com.shop.config.Constants;
import com.shop.dao.*;
import com.shop.entity.Address;
import com.shop.entity.Delivery;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The {@code DaoFactory} class is a base class for SQL DAO factories.
 * The main purpose of the class is to get an appropriate type of factory according to the database type.
 * @author Anton Makarenko
 */

public abstract class DaoFactory {
    private static final Logger logger = LogManager.getLogger(DaoFactory.class);

    public static DaoFactory getDaoFactory() {
        switch (Constants.DATABASE_TYPE.toLowerCase()) {
            case Constants.MYSQL:
                return MySQLFactory.getInstance();
            case Constants.H2:
                return H2Factory.getInstance();
            default:
                logger.fatal("Database {} is not supported", Constants.DATABASE_TYPE);
                System.exit(-1);
                return null;
        }
    }

    public static DaoFactory getDaoFactory(String databaseType) {
        switch (databaseType.toLowerCase()) {
            case Constants.MYSQL:
                return MySQLFactory.getInstance();
            case Constants.H2:
                return H2Factory.getInstance();
            default:
                logger.fatal("Database {} is not supported", Constants.DATABASE_TYPE);
                System.exit(-1);
                return null;
        }
    }

    public abstract UserDao getUserDao();
    public abstract ProductDao getProductDao();
    public abstract CategoryDao getCategoryDao();
    public abstract PageDao<Address> getAddressDao();
    public abstract Dao<Delivery> getDeliveryDao();
    public abstract OrderDao getOrderDao();
}
