package com.shop.dao.impl.mysql;

import com.shop.dao.UserDao;
import com.shop.entity.User;

import java.util.List;

public class MySQLUserDao implements UserDao {
    @Override
    public User getByEmail(String email) {
        return null;
    }

    @Override
    public User getById(long id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public boolean insert(User element) {
        return false;
    }

    @Override
    public boolean update(User element) {
        return false;
    }

    @Override
    public void delete(User element) {

    }
}
