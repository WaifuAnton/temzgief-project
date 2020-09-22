package com.shop.dao.impl.mysql;

import com.shop.config.Constants;
import com.shop.connection.ConnectionPool;
import com.shop.connection.ConnectionPoolFactory;
import com.shop.dao.OrderDao;
import com.shop.entity.Category;
import com.shop.entity.Order;
import com.shop.entity.Product;
import com.shop.entity.User;
import com.shop.enumeration.Color;
import com.shop.enumeration.Role;
import com.shop.enumeration.Status;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MySQLOrderDao implements OrderDao {
    private final ConnectionPool connectionPool = ConnectionPoolFactory.getConnectionPool();
    private final BasicDataSource dataSource = connectionPool.getDataSource();

    @Override
    public List<Order> getByUser(User user) throws SQLException {
        List<Order> orders;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM ORDERS WHERE USER_ID = ?")) {
            statement.setLong(1, user.getId());
            orders = createOrdersFromStatement(connection, statement);
        }
        return orders;
    }

    @Override
    public void addCountOfProductProduct(Order order, Product product, int newCount) throws SQLException {
        if (newCount == 0)
            return;
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("SELECT * FROM ORDER_HAS_PRODUCT WHERE ORDER_ID = ? AND PRODUCT_ID = ?");
            statement.setLong(1, order.getId());
            statement.setLong(2, product.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    statement = connection.prepareStatement("UPDATE ORDER_HAS_PRODUCT SET COUNT = ?");
                    statement.setInt(1, newCount);
                }
                else {
                    statement = connection.prepareStatement("INSERT INTO ORDER_HAS_PRODUCT VALUES (?, ?, ?, ?)");
                    statement.setLong(1, order.getId());
                    statement.setLong(2, product.getId());
                    statement.setInt(3, newCount);
                    statement.setDouble(4, product.getPrice());
                }
                statement.execute();
            }
        }
        finally {
            if (statement != null)
                statement.close();
            if (connection != null)
                connection.close();
        }
    }

    @Override
    public Optional<Order> getById(long id) throws SQLException {
        Order order;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM ORDERS WHERE ID = ?")) {
            statement.setLong(1, id);
            order = createOrderFromStatement(connection, statement);
        }
        return Optional.of(order);
    }

    @Override
    public List<Order> findAll() throws SQLException {
        List<Order> orders;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM ORDERS")) {
            orders = createOrdersFromStatement(connection, statement);
        }
        return orders;
    }

    @Override
    public void insert(Order element) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO ORDERS (USER_ID, TOTAL, STATUS) VALUES (?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, element.getUser().getId());
            statement.setDouble(2, 0);
            statement.setString(3, Status.IN_PROCESS.toString());
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next())
                    element.setId(resultSet.getLong(1));
            }
        }
    }

    @Override
    public void update(Order element) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE ORDERS SET STATUS = ?")) {
            statement.setString(1, element.getStatus().toString());
            statement.execute();
        }
    }

    @Override
    public void delete(Order element) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM ORDERS WHERE ID = ?")) {
            statement.setLong(1, element.getId());
            statement.execute();
        }
    }

    @Override
    public List<Order> findLimited(int offset) throws SQLException {
        List<Order> orders;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM ORDERS LIMIT ?, ?")) {
            statement.setInt(2, Constants.PRODUCT_LIMIT);
            statement.setInt(1, offset);
            orders = createOrdersFromStatement(connection, statement);
        }
        return orders;
    }

    private Order createOrderFromStatement(Connection connection, PreparedStatement statement) throws SQLException {
        Order order = null;
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                order = new Order();
                setUpFields(connection, order, resultSet);
            }
        }
        return order;
    }

    private List<Order> createOrdersFromStatement(Connection connection, PreparedStatement statement) throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Order order = new Order();
                setUpFields(connection, order, resultSet);
                orders.add(order);
            }
        }
        return orders;
    }

    private void setUpFields(Connection connection, Order order, ResultSet resultSet) throws SQLException {
        order.setId(resultSet.getLong("id"));
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM USERS WHERE ID = ?")) {
            statement.setLong(1, resultSet.getLong("user_id"));
            try (ResultSet resultSet1 = statement.executeQuery()) {
                User user = new User();
                user.setId(resultSet1.getLong("id"));
                user.setEmail(resultSet1.getString("email"));
                user.setPasswordHash(resultSet1.getBytes("password_hash"));
                user.setSalt(resultSet1.getString("salt"));
                user.setRole(Role.valueOf(resultSet1.getString("role").toUpperCase()));
                user.setCreateDate(new Date(resultSet1.getTimestamp("create_date").getTime()));
                user.setLastUpdate(new Date(resultSet1.getTimestamp("last_update").getTime()));
                order.setUser(user);
            }
        }
        order.setTotal(resultSet.getDouble("total"));
        order.setProducts(getProductsInOrder(connection, order));
        order.setTotal(resultSet.getDouble("total"));
        order.setStatus(Status.valueOf(resultSet.getString("status").toUpperCase()));
        order.setCreateDate(new Date(resultSet.getTimestamp("create_date").getTime()));
        order.setLastUpdate(new Date(resultSet.getTimestamp("last_update").getTime()));
    }

    private List<Product> getProductsInOrder(Connection connection, Order order) throws SQLException {
        List<Product> products = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement("SELECT P.ID, P.NAME, P.PICTURE, P.COLOR, P.DESCRIPTION, P.PRICE, P.AMOUNT, P.CATEGORY_ID, P.CREATE_DATE, P.LAST_UPDATE FROM PRODUCTS P " +
                "INNER JOIN ORDER_HAS_PRODUCT OP ON P.ID = OP.product_id " +
                "INNER JOIN ORDERS O ON O.ID = OP.order_id " +
                "WHERE O.ID = ?")) {
            statement.setLong(1, order.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Product product = new Product();
                    product.setId(resultSet.getLong("id"));
                    product.setName(resultSet.getString("name"));
                    product.setPicture(resultSet.getString("picture"));
                    product.setColor(Color.valueOf(resultSet.getString("color").toUpperCase()));
                    product.setDescription(resultSet.getString("description"));
                    product.setPrice(resultSet.getDouble("price"));
                    product.setAmount(resultSet.getInt("amount"));
                    product.setCategory(getById(connection, resultSet.getLong("category_id")).orElseThrow(() -> new SQLException("Product cannot exist without its category")));
                    product.setCreateDate(new Date(resultSet.getTimestamp("create_date").getTime()));
                    product.setLastUpdate(new Date(resultSet.getTimestamp("last_update").getTime()));
                    products.add(product);
                }
                return products;
            }
        }
    }

    private Optional<Category> getById(Connection connection, long id) throws SQLException {
        Category category;
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM CATEGORIES WHERE ID = ?")) {
            statement.setLong(1, id);
            category = createCategoryFromStatement(connection, statement);
        }
        return Optional.ofNullable(category);
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

    private void setUpFields(Connection connection, Category category, ResultSet resultSet) throws SQLException {
        category.setId(resultSet.getLong("id"));
        category.setName(resultSet.getString("name"));
        category.setParentCategory(getById(connection, resultSet.getLong("parent_id")).orElse(null));
        category.setCreateDate(new Date(resultSet.getTimestamp("create_date").getTime()));
        category.setLastUpdate(new Date(resultSet.getTimestamp("last_update").getTime()));
    }
}
