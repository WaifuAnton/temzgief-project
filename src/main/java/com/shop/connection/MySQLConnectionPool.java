package com.shop.connection;

import com.shop.reader.PropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyVetoException;

public class MySQLConnectionPool extends ConnectionPool {
    private static final Logger logger = LogManager.getLogger(MySQLConnectionPool.class);

    private static MySQLConnectionPool connectionPool;

    public static synchronized MySQLConnectionPool getInstance() {
        if (connectionPool == null)
            connectionPool = new MySQLConnectionPool();
        return connectionPool;
    }

    private MySQLConnectionPool() {
        try {
            dataSource.setDriverClass("com.mysql.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://" + PropertyReader.readUrl() + ':' + PropertyReader.readPort());
            dataSource.setUser(PropertyReader.readUser());
            dataSource.setPassword(PropertyReader.readPassword());
        } catch (PropertyVetoException e) {
            logger.fatal("Error loading driver", e);
            System.exit(-1);
        }
    }
}
