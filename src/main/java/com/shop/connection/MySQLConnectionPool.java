package com.shop.connection;

import com.shop.reader.MySQLPropertyReader;
import org.apache.commons.dbcp2.BasicDataSource;

public class MySQLConnectionPool implements ConnectionPool {
    private static MySQLConnectionPool connectionPool;

    private final BasicDataSource dataSource;

    public static synchronized MySQLConnectionPool getInstance() {
        if (connectionPool == null)
            connectionPool = new MySQLConnectionPool();
        return connectionPool;
    }

    private MySQLConnectionPool() {
        dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://" + MySQLPropertyReader.readUrl());
        dataSource.setUsername(MySQLPropertyReader.readUser());
        dataSource.setPassword(MySQLPropertyReader.readPassword());
        dataSource.setInitialSize(Integer.parseInt(MySQLPropertyReader.readPoolSize()));
    }

    @Override
    public BasicDataSource getDataSource() {
        return dataSource;
    }
}
