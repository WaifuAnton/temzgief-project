package com.shop.dao;

import com.shop.entity.Order;
import com.shop.entity.Product;
import com.shop.entity.User;

import java.sql.SQLException;
import java.util.List;

public interface OrderDao extends PageDao<Order> {
    List<Order> getByUser(User user) throws SQLException;
    void addCountOfProductProduct(Order order, Product product, int newCount) throws SQLException;
}
