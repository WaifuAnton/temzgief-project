package com.shop.dao.impl.mysql;

import com.shop.connection.ConnectionPool;
import com.shop.connection.ConnectionPoolFactory;
import com.shop.dao.ProductDao;
import com.shop.entity.Product;
import com.shop.enumeration.Color;
import org.apache.commons.dbcp2.BasicDataSource;
import com.shop.config.Constants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MySQLProductDao implements ProductDao {
    private final ConnectionPool connectionPool = ConnectionPoolFactory.getConnectionPool();
    private final BasicDataSource dataSource = connectionPool.getDataSource();

    @Override
    public Optional<Product> getByName(String name) throws SQLException {
        Product product;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM PRODUCTS WHERE NAME = ?")) {
            statement.setString(1, name);
            product = createProductFromStatement(connection, statement);
        }
        return Optional.of(product);
    }

    @Override
    public List<Product> findLimitedByCategoryName(String categoryName, int offset) throws SQLException {
        List<Product> products;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement categoryStatement = connection.prepareStatement("SELECT ID FROM CATEGORIES WHERE NAME = ?");
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM PRODUCTS WHERE CATEGORY_ID = ? LIMIT ?, ?")) {
            categoryStatement.setString(1, categoryName);
            long id = 0;
            try (ResultSet resultSet = categoryStatement.executeQuery()) {
                if (resultSet.next())
                    id = resultSet.getLong(1);
            }
            statement.setLong(1, id);
            statement.setInt(3, Constants.PRODUCT_LIMIT);
            statement.setInt(2, offset);
            products = createProductsFromStatement(connection, statement);
        }
        return products;
    }

    @Override
    public List<Product> findLimited(int offset) throws SQLException {
        List<Product> products;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM PRODUCTS LIMIT ?, ?")) {
            statement.setInt(2, Constants.PRODUCT_LIMIT);
            statement.setInt(1, offset);
            products = createProductsFromStatement(connection, statement);
        }
        return products;
    }

    @Override
    public long count() throws SQLException {
        long count = 0;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM PRODUCTS");
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next())
                count = resultSet.getLong(1);
        }
        return count;
    }

    @Override
    public Optional<Product> getById(long id) throws SQLException {
        Product product;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM PRODUCTS WHERE ID = ?")) {
            statement.setLong(1, id);
            product = createProductFromStatement(connection, statement);
        }
        return Optional.of(product);
    }

    @Override
    public List<Product> findAll() throws SQLException {
        List<Product> products;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM PRODUCTS")) {
            products = createProductsFromStatement(connection, statement);
        }
        return products;
    }

    @Override
    public void insert(Product element) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO PRODUCTS (NAME, PICTURE, COLOR, DESCRIPTION, PRICE, AMOUNT, CATEGORY_ID, MANUFACTURE_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertOrUpdate(element, statement);
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next())
                    element.setId(resultSet.getLong(1));
            }
        }
    }

    @Override
    public void update(Product element) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE PRODUCTS SET NAME = ?, PICTURE = ?, COLOR = ?, DESCRIPTION = ?, PRICE = ?, AMOUNT = ?, CATEGORY_ID = ?, MANUFACTURE_DATE = ? WHERE ID = ?")) {
            statement.setLong(8, element.getId());
            insertOrUpdate(element, statement);
        }
    }

    @Override
    public void delete(Product element) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM CATEGORIES WHERE ID = ?")) {
            statement.setLong(1, element.getId());
            statement.execute();
        }
    }

    private static void insertOrUpdate(Product element, PreparedStatement statement) throws SQLException {
        statement.setString(1, element.getName());
        statement.setString(2, element.getPicture());
        statement.setString(3, element.getColor().toString());
        statement.setString(4, element.getDescription());
        statement.setDouble(5, element.getPrice());
        statement.setInt(6, element.getAmount());
        statement.setLong(7, element.getCategory().getId());
        statement.setDate(8, new java.sql.Date(element.getManufactureDate().getTime()));
        statement.execute();
    }

    static Product createProductFromStatement(Connection connection, PreparedStatement statement) throws SQLException {
        Product product = null;
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                product = new Product();
                setUpFields(connection, product, resultSet);
            }
        }
        return product;
    }

    static List<Product> createProductsFromStatement(Connection connection, PreparedStatement statement) throws SQLException {
        List<Product> products = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Product product = new Product();
                setUpFields(connection, product, resultSet);
                products.add(product);
            }
        }
        return products;
    }

    static void setUpFields(Connection connection, Product product, ResultSet resultSet) throws SQLException {
        product.setId(resultSet.getLong("id"));
        product.setName(resultSet.getString("name"));
        product.setPicture(resultSet.getString("picture"));
        product.setColor(Color.valueOf(resultSet.getString("color").toUpperCase()));
        product.setManufactureDate(new Date(resultSet.getDate("manufacture_date").getTime()));
        product.setDescription(resultSet.getString("description"));
        product.setPrice(resultSet.getDouble("price"));
        product.setAmount(resultSet.getInt("amount"));
        product.setCategory(MySQLCategoryDao.getById(connection, resultSet.getLong("category_id")).orElseThrow(() -> new SQLException("Product cannot exist without its category")));
        product.setCreateDate(new Date(resultSet.getTimestamp("create_date").getTime()));
        product.setLastUpdate(new Date(resultSet.getTimestamp("last_update").getTime()));
    }
}
