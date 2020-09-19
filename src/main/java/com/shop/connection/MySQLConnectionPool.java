package com.shop.connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.shop.reader.MySQLPropertyReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.beans.PropertyVetoException;

public class MySQLConnectionPool implements ConnectionPool {
    private static final Logger logger = LogManager.getLogger(MySQLConnectionPool.class);

    private static MySQLConnectionPool connectionPool;

    private final ComboPooledDataSource dataSource;

    public static synchronized MySQLConnectionPool getInstance() {
        if (connectionPool == null)
            connectionPool = new MySQLConnectionPool();
        return connectionPool;
    }

    private MySQLConnectionPool() {
        dataSource = new ComboPooledDataSource();
        try {
            dataSource.setDriverClass("com.mysql.jdbc.Driver");
            dataSource.setJdbcUrl("jdbc:mysql://" + MySQLPropertyReader.readUrl() + ':' + MySQLPropertyReader.readPort());
            dataSource.setUser(MySQLPropertyReader.readUser());
            dataSource.setPassword(MySQLPropertyReader.readPassword());
        } catch (PropertyVetoException e) {
            logger.fatal("Error loading driver", e);
            System.exit(-1);
        }
    }

    @Override
    public ComboPooledDataSource getDataSource() {
        return dataSource;
    }
}
