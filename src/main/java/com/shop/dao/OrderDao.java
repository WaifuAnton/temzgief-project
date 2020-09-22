package com.shop.dao;

import com.shop.entity.Order;
import com.shop.entity.User;

import java.util.List;

public interface OrderDao extends Dao<Order> {
    List<Order> getByUser(User user);
}
