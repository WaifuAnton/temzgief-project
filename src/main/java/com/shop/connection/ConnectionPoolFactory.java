package com.shop.connection;

import com.shop.config.Constants;
import com.shop.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionPoolFactory {
    private static final Logger logger = LogManager.getLogger(ConnectionPoolFactory.class);

    public ConnectionPool getConnectionPool(String databaseType) {
        switch (databaseType.toLowerCase()) {
            case Constants.MYSQL:
                return MySQLConnectionPool.getInstance();
            default:
                return getConnectionPool();
        }
    }

    public ConnectionPool getConnectionPool() {
        switch (PropertyReader.readType()) {
            case Constants.MYSQL:
                return MySQLConnectionPool.getInstance();
            default:
                logger.fatal("Database {} is not supported", PropertyReader.readType());
                System.exit(-1);
                return null;
        }
    }

    private ConnectionPoolFactory() {

    }
}
