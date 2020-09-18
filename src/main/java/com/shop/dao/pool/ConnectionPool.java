package com.shop.dao.pool;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyVetoException;

public class ConnectionPool {
    private static final Logger logger = LogManager.getLogger(ConnectionPool.class);

    private static ConnectionPool dataSource;

    private ComboPooledDataSource pooledDataSource;

    private static final String MYSQL = "mysql";

    private ConnectionPool(String databaseType) {
        StringBuilder jdbcUrl = new StringBuilder("jdbc:" + databaseType + ":");
        pooledDataSource = new ComboPooledDataSource();
        try {
            switch (databaseType.toLowerCase()) {
                case MYSQL:
                    pooledDataSource.setDriverClass("com.mysql.jdbc.Driver");

                    break;
            }
        }
        catch (PropertyVetoException e) {
            logger.fatal("Can`t load MySQL driver", e);
            System.exit(-1);
        }
    }
}
