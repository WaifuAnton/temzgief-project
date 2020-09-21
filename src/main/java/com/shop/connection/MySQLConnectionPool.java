package com.shop.connection;

import com.shop.reader.DatabasePropertyReader;
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
        dataSource.setUrl("jdbc:mysql://" + DatabasePropertyReader.readUrl());
        dataSource.setUsername(DatabasePropertyReader.readUser());
        dataSource.setPassword(DatabasePropertyReader.readPassword());
        dataSource.setInitialSize(Integer.parseInt(DatabasePropertyReader.readPoolSize()));
    }

    @Override
    public BasicDataSource getDataSource() {
        return dataSource;
    }
}
