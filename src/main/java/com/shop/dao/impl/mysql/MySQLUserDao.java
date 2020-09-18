package com.shop.dao.impl.mysql;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.shop.connection.MySQLConnectionPool;
import com.shop.dao.UserDao;
import com.shop.entity.User;
import com.shop.enumeration.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class MySQLUserDao implements UserDao {
    private final ComboPooledDataSource dataSource = MySQLConnectionPool.getInstance().getDataSource();

    @Override
    public User getByEmail(String email) {
        return null;
    }

    @Override
    public User getById(long id) throws SQLException {
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERS WHERE ID = ?")) {
            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User();
                    user.setId(resultSet.getLong("id"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPasswordHash(resultSet.getBytes("password_hash"));
                    user.setSalt(resultSet.getString("salt"));
                    user.setRole(Role.valueOf(resultSet.getString("role").toUpperCase()));
                    user.setCreateDate(new Date(resultSet.getTimestamp("create_date").getTime()));
                    user.setLastUpdate(new Date(resultSet.getTimestamp("last_update").getTime()));
                }
            }
        }
        return user;
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

    private Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
