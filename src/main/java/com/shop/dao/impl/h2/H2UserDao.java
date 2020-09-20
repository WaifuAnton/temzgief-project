package com.shop.dao.impl.h2;

import com.shop.config.Constants;
import com.shop.connection.ConnectionPoolFactory;
import com.shop.dao.UserDao;
import com.shop.entity.User;
import com.shop.enumeration.Role;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class H2UserDao implements UserDao {
    private final BasicDataSource dataSource = ConnectionPoolFactory.getConnectionPool(Constants.H2).getDataSource();

    @Override
    public Optional<User> getByEmail(String email) throws SQLException {
        User user;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"users\" WHERE \"email\" = ?")) {
            statement.setString(1, email);
            user = createUserFromStatement(statement);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> getById(long id) throws SQLException {
        User user;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"users\" WHERE \"id\" = ?")) {
            statement.setLong(1, id);
            user = createUserFromStatement(statement);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM \"users\"")) {
            users = createUsersFromStatement(statement);
        }
        return users;
    }

    @Override
    public void insert(User element) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO \"users\" (\"email\", \"password_hash\", \"salt\", \"role\") VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {

            insertOrUpdate(element, statement);
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next())
                    element.setId(resultSet.getLong(1));
            }
        }
    }

    @Override
    public void update(User element) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE \"users\" SET \"email\" = ?, \"password_hash\" = ?, \"salt\" = ?, \"role\" = ? WHERE \"id\" = ?")) {
            insertOrUpdate(element, statement);
        }
    }

    @Override
    public void delete(User element) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM \"users\" WHERE \"id\" = ?")) {
            statement.setLong(1, element.getId());
            statement.execute();
        }
    }

    private void insertOrUpdate(User element, PreparedStatement statement) throws SQLException {
        statement.setString(1, element.getEmail());
        statement.setBytes(2, element.getPasswordHash());
        statement.setString(3, element.getSalt());
        statement.setString(4, element.getRole().toString());
        statement.setLong(5, element.getId());
        statement.execute();
    }

    private User createUserFromStatement(PreparedStatement statement) throws SQLException {
        User user = null;
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                user = new User();
                setUpFieldsFromResultSet(user, resultSet);
            }
        }
        return user;
    }

    private List<User> createUsersFromStatement(PreparedStatement statement) throws SQLException {
        List<User> users = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User();
                setUpFieldsFromResultSet(user, resultSet);
                users.add(user);
            }
        }
        return users;
    }

    private void setUpFieldsFromResultSet(User user, ResultSet resultSet) throws SQLException {
        user.setId(resultSet.getLong("id"));
        user.setEmail(resultSet.getString("email"));
        user.setPasswordHash(resultSet.getBytes("password_hash"));
        user.setSalt(resultSet.getString("salt"));
        user.setRole(Role.valueOf(resultSet.getString("role").toUpperCase()));
        user.setCreateDate(new Date(resultSet.getTimestamp("create_date").getTime()));
        user.setLastUpdate(new Date(resultSet.getTimestamp("last_update").getTime()));
    }
}
