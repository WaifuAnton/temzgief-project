package com.shop.dao.impl.mysql;

import com.shop.config.Constants;
import com.shop.connection.ConnectionPool;
import com.shop.connection.ConnectionPoolFactory;
import com.shop.dao.CategoryDao;
import com.shop.dao.OrderDao;
import com.shop.dao.UserDao;
import com.shop.dao.factory.DaoFactory;
import com.shop.entity.Order;
import com.shop.entity.Product;
import com.shop.entity.User;
import com.shop.enumeration.Color;
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

    private final DaoFactory daoFactory = DaoFactory.getDaoFactory();
    private final UserDao userDao = daoFactory.getUserDao();
    private final CategoryDao categoryDao = daoFactory.getCategoryDao();

    @Override
    public List<Order> getByUser(User user) throws SQLException {
        List<Order> orders;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM ORDERS WHERE USER_ID = ?")) {
            statement.setLong(1, user.getId());
            orders = createOrdersFromStatement(statement);
        }
        return orders;
    }

    @Override
    public void addProduct(Order order, Product product, int count) throws SQLException {

    }

    @Override
    public void removeProduct(Order order, Product product, int count) throws SQLException {

    }

    @Override
    public Optional<Order> getById(long id) throws SQLException {
        Order order;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM ORDERS WHERE ID = ?")) {
            statement.setLong(1, id);
            order = createOrderFromStatement(statement);
        }
        return Optional.of(order);
    }

    @Override
    public List<Order> findAll() throws SQLException {
        List<Order> orders;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM ORDERS")) {
            orders = createOrdersFromStatement(statement);
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
            orders = createOrdersFromStatement(statement);
        }
        return orders;
    }

    private void insertOrUpdate(Order element, PreparedStatement statement) throws SQLException {
        statement.setLong(1, element.getUser().getId());
        statement.setDouble(2, element.getTotal());
        statement.setString(3, element.getStatus().toString());
        statement.execute();
    }

    private Order createOrderFromStatement(PreparedStatement statement) throws SQLException {
        Order order = null;
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                order = new Order();
                setUpFields(order, resultSet);
            }
        }
        return order;
    }

    private List<Order> createOrdersFromStatement(PreparedStatement statement) throws SQLException {
        List<Order> orders = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Order order = new Order();
                setUpFields(order, resultSet);
                orders.add(order);
            }
        }
        return orders;
    }

    private void setUpFields(Order order, ResultSet resultSet) throws SQLException {
        order.setId(resultSet.getLong("id"));
        order.setUser(userDao.getById(resultSet.getLong("user_id")).orElseThrow(() -> new SQLException("Order cannot exist without user!")));
        order.setTotal(resultSet.getDouble("total"));
        order.setProducts(findProductsByOrder(order));
        order.setTotal(resultSet.getDouble("total"));
        order.setStatus(Status.valueOf(resultSet.getString("status").toUpperCase()));
        order.setCreateDate(new Date(resultSet.getTimestamp("create_date").getTime()));
        order.setLastUpdate(new Date(resultSet.getTimestamp("last_update").getTime()));
    }

    private List<Product> findProductsByOrder(Order order) throws SQLException {
        List<Product> products = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); PreparedStatement statement = connection.prepareStatement("SELECT P.ID, P.NAME, P.PICTURE, P.COLOR, P.DESCRIPTION, P.PRICE, P.AMOUNT, P.CATEGORY_ID, P.CREATE_DATE, P.LAST_UPDATE FROM PRODUCTS P " +
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
                    product.setCategory(categoryDao.getById(resultSet.getLong("category_id")).orElseThrow(() -> new SQLException("Product cannot exist without its category")));
                    product.setCreateDate(new Date(resultSet.getTimestamp("create_date").getTime()));
                    product.setLastUpdate(new Date(resultSet.getTimestamp("last_update").getTime()));
                    products.add(product);
                }
                return products;
            }
        }
    }
}
