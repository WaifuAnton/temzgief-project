package com.shop.dao.impl.mysql;

import com.shop.connection.ConnectionPool;
import com.shop.connection.ConnectionPoolFactory;
import com.shop.dao.CategoryDao;
import com.shop.entity.Category;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MySQLCategoryDao implements CategoryDao {
    private final ConnectionPool connectionPool = ConnectionPoolFactory.getConnectionPool();
    private final BasicDataSource dataSource = connectionPool.getDataSource();

    @Override
    public Optional<Category> getByName(String name) {
        return Optional.empty();
    }

    @Override
    public Optional<Category> getById(long id) throws SQLException {
        Category category;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM CATEGORIES WHERE ID = ?")) {
            statement.setLong(1, id);
            category = createCategoryFromStatement(connection, statement);
        }
        return Optional.ofNullable(category);
    }

    @Override
    public List<Category> findAll() throws SQLException {
        List<Category> categories;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM CATEGORIES")) {
            categories = createCategoriesFromStatement(connection, statement);
        }
        return categories;
    }

    @Override
    public void insert(Category element) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO CATEGORIES (NAME, PARENT_ID) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertOrUpdate(element, statement);
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next())
                    element.setId(resultSet.getLong(1));
            }
        }
    }

    @Override
    public void update(Category element) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE CATEGORIES SET NAME = ?, PARENT_ID = ? WHERE ID = ?")) {
            statement.setLong(3, element.getId());
            insertOrUpdate(element, statement);
        }
    }

    @Override
    public void delete(Category element) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM CATEGORIES WHERE ID = ?")) {
            statement.setLong(1, element.getId());
            statement.execute();
        }
    }

    private Category createCategoryFromStatement(Connection connection, PreparedStatement statement) throws SQLException {
        Category category = null;
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                category = new Category();
                setUpFields(connection, category, resultSet);
            }
        }
        return category;
    }

    private List<Category> createCategoriesFromStatement(Connection connection, PreparedStatement statement) throws SQLException {
        List<Category> categories = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Category category = new Category();
                setUpFields(connection, category, resultSet);
                categories.add(category);
            }
        }
        return categories;
    }

    private void setUpFields(Connection connection, Category category, ResultSet resultSet) throws SQLException {
        category.setId(resultSet.getLong("id"));
        category.setName(resultSet.getString("name"));
        category.setParentCategory(getById(connection, resultSet.getLong("parent_id")).orElse(null));
        category.setCreateDate(new Date(resultSet.getTimestamp("create_date").getTime()));
        category.setLastUpdate(new Date(resultSet.getTimestamp("last_update").getTime()));
    }

    private void insertOrUpdate(Category category, PreparedStatement statement) throws SQLException {
        statement.setString(1, category.getName());
        statement.setLong(2, category.getParentCategory().getId());
        statement.execute();
    }

    private Optional<Category> getById(Connection connection, long id) throws SQLException {
        Category category;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM CATEGORIES WHERE ID = ?")) {
            statement.setLong(1, id);
            category = createCategoryFromStatement(connection, statement);
        }
        return Optional.ofNullable(category);
    }
}
