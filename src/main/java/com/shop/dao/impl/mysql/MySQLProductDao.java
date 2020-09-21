package com.shop.dao.impl.mysql;

import com.shop.config.Constants;
import com.shop.connection.ConnectionPool;
import com.shop.connection.ConnectionPoolFactory;
import com.shop.dao.CategoryDao;
import com.shop.dao.ProductDao;
import com.shop.dao.factory.DaoFactory;
import com.shop.entity.Product;
import com.shop.enumeration.Color;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MySQLProductDao implements ProductDao {
    private final ConnectionPool connectionPool = ConnectionPoolFactory.getConnectionPool();
    private final BasicDataSource dataSource = connectionPool.getDataSource();

    private final DaoFactory factory = DaoFactory.getDaoFactory(Constants.MYSQL);
    private final CategoryDao categoryDao = factory.getCategoryDao();

    @Override
    public Optional<Product> getByName(String name) throws SQLException {
        Product product;
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT * FROM PRODUCTS")) {

        }
        return Optional.empty();
    }

    @Override
    public Optional<Product> getById(long id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() throws SQLException {
        return null;
    }

    @Override
    public void insert(Product element) throws SQLException {

    }

    @Override
    public void update(Product element) throws SQLException {

    }

    @Override
    public void delete(Product element) throws SQLException {

    }

    private void setUpFields(Product product, ResultSet resultSet) throws SQLException {
        product.setId(resultSet.getLong("id"));
        product.setAmount(resultSet.getInt("amount"));
        product.setColor(Color.valueOf(resultSet.getString("color").toUpperCase()));
    }
}
