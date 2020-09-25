package com.shop.dao;

import com.shop.entity.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductDao extends PageDao<Product> {
    Optional<Product> getByName(String name) throws SQLException;
    List<Product> findLimitedByCategoryName(String categoryName, int offset) throws SQLException;
}
