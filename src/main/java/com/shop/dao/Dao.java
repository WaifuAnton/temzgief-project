package com.shop.dao;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.util.List;

public interface Dao<E> {
    E getById(long id);
    List<E> findAll();
    boolean insert(E element);
    boolean update(E element);
    void delete(E element);
}
