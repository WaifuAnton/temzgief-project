package com.shop.dao;

import com.shop.entity.User;

public interface UserDao extends Dao<User> {
    User getByEmail(String email);
}
