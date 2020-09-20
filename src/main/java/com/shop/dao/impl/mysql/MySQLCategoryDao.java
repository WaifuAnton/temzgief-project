package com.shop.dao.impl.mysql;

import com.shop.config.Constants;
import com.shop.connection.ConnectionPool;
import com.shop.connection.ConnectionPoolFactory;
import com.shop.dao.CategoryDao;
import com.shop.entity.Category;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MySQLCategoryDao implements CategoryDao {
    private final ConnectionPool connectionPool = ConnectionPoolFactory.getConnectionPool(Constants.MYSQL);
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
            category = createCategoryFromStatement(statement);
        }
        return Optional.ofNullable(category);
    }

    @Override
    public List<Category> findAll() throws SQLException {
        return null;
    }

    @Override
    public void insert(Category element) throws SQLException {

    }

    @Override
    public void update(Category element) throws SQLException {

    }

    @Override
    public void delete(Category element) throws SQLException {

    }

    private Category createCategoryFromStatement(PreparedStatement statement) throws SQLException {
        Category category = null;
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                category = new Category();
                setUpFields(category, resultSet);
            }
        }
        return category;
    }

    private void setUpFields(Category category, ResultSet resultSet) throws SQLException {
        category.setId(resultSet.getLong("id"));
        category.setName(resultSet.getString("name"));
        category.setParentCategory(getById(resultSet.getLong("parent_id")).orElse(null));
        category.setCreateDate(new Date(resultSet.getTimestamp("create_date").getTime()));
        category.setLastUpdate(new Date(resultSet.getTimestamp("last_update").getTime()));
    }
}
