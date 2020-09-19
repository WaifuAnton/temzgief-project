package com.shop.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<E> {
    Optional<E> getById(long id) throws SQLException;
    List<E> findAll() throws SQLException;
    void insert(E element) throws SQLException;
    void update(E element) throws SQLException;
    void delete(E element) throws SQLException;
}
