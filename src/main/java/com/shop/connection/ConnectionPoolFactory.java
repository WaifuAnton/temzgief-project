package com.shop.connection;

import com.shop.config.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class ConnectionPoolFactory {
    private static final Logger logger = LogManager.getLogger(ConnectionPoolFactory.class);

    public static ConnectionPool getConnectionPool(String databaseType) {
        switch (databaseType.toLowerCase()) {
            case Constants.MYSQL:
                return MySQLConnectionPool.getInstance();
            case Constants.H2:
                return H2ConnectionPool.getInstance();
            default:
                logger.fatal("Database {} is not supported", databaseType);
                System.exit(-1);
                return null;
        }
    }

    public static ConnectionPool getConnectionPool() {
        return MySQLConnectionPool.getInstance();
    }

    private ConnectionPoolFactory() {

    }
}
