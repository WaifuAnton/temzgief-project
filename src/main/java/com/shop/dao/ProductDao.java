package com.shop.dao;

import com.shop.entity.Product;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface ProductDao extends PageDao<Product> {
    Optional<Product> getByName(String name) throws SQLException;
    List<Product> findLimitedByCategoryName(String categoryName, int offset) throws SQLException;
    List<Product> findLimitedByCategoryNameBetweenSortBy(String categoryName, String field, double from, double to, boolean desc, int offset) throws SQLException;
    double[] minAndMaxPrice() throws SQLException;
    long count(double from, double to) throws SQLException;
}
