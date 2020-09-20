package com.shop.connection;

import com.shop.reader.H2PropertyReader;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class H2ConnectionPool implements ConnectionPool {
    private static final Logger logger = LogManager.getLogger(H2ConnectionPool.class);

    private static H2ConnectionPool connectionPool;

    private final BasicDataSource dataSource;

    public static synchronized H2ConnectionPool getInstance() {
        if (connectionPool == null)
            connectionPool = new H2ConnectionPool();
        return connectionPool;
    }

    private H2ConnectionPool() {
        dataSource = new BasicDataSource();
        dataSource.setUrl(H2PropertyReader.readUrl() + ";INIT=RUNSCRIPT FROM '" + H2PropertyReader.readScript() + "'");
    }

    @Override
    public BasicDataSource getDataSource() {
        return dataSource;
    }
}
