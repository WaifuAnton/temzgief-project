package com.shop.connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public abstract class ConnectionPool {
    protected ComboPooledDataSource dataSource = new ComboPooledDataSource();

    public ComboPooledDataSource getDataSource() {
        return dataSource;
    }
}
