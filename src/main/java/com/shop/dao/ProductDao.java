package com.shop.dao;

import com.shop.entity.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductDao extends Dao<Product> {
    Optional<Product> getByName(String name) throws SQLException;
    List<Product> getLimited(int offset) throws SQLException;
}
