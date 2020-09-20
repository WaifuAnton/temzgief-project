package com.shop.connection;

import org.apache.commons.dbcp2.BasicDataSource;

public interface ConnectionPool {
    BasicDataSource getDataSource();
}
