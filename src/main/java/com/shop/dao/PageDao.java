package com.shop.dao;

import com.shop.entity.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PageDao<E> extends Dao<E> {
    List<E> findLimited(int offset) throws SQLException;
    long count() throws SQLException;
}
