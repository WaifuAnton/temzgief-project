package com.shop.connection;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public interface ConnectionPool {
    ComboPooledDataSource getDataSource();
}
