package com.shop.dao;

import com.shop.entity.User;

import java.sql.SQLException;
import java.util.Optional;

public interface UserDao extends Dao<User> {
    Optional<User> getByEmail(String email) throws SQLException;
}
