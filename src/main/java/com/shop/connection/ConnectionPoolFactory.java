package com.shop.connection;

import com.shop.config.Constants;
import com.shop.reader.MySQLPropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConnectionPoolFactory {
    private static final Logger logger = LogManager.getLogger(ConnectionPoolFactory.class);

    public ConnectionPool getConnectionPool(String databaseType) {
        switch (databaseType.toLowerCase()) {
            case Constants.MYSQL:
                return MySQLConnectionPool.getInstance();
            default:
                logger.fatal("Database {} is not supported", databaseType);
                System.exit(-1);
                return null;
        }
    }

    public ConnectionPool getConnectionPool() {
        return MySQLConnectionPool.getInstance();
    }

    private ConnectionPoolFactory() {

    }
}
