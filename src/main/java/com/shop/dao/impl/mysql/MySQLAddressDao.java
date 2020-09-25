package com.shop.dao.impl.mysql;

import com.shop.config.Constants;
import com.shop.connection.ConnectionPool;
import com.shop.connection.ConnectionPoolFactory;
import com.shop.dao.PageDao;
import com.shop.entity.Address;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MySQLAddressDao implements PageDao<Address> {
    private final ConnectionPool connectionPool = ConnectionPoolFactory.getConnectionPool();
    private final BasicDataSource dataSource = connectionPool.getDataSource();

    @Override
    public Optional<Address> getById(long id) throws SQLException {
        Address address;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM ADDRESSES WHERE ID = ?")) {
            statement.setLong(1, id);
            address = createAddressFromStatement(statement);
        }
        return Optional.of(address);
    }

    @Override
    public List<Address> findAll() throws SQLException {
        List<Address> addresses;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM ADDRESSES")) {
            addresses = createAddressesFromStatement(statement);
        }
        return addresses;
    }

    @Override
    public void insert(Address element) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO ADDRESSES (COUNTRY, CITY, BUILDING, APARTMENT) VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            insertOrUpdate(element, statement);
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next())
                    element.setId(resultSet.getLong(1));
            }
        }
    }

    @Override
    public void update(Address element) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE ADDRESSES SET COUNTRY = ?, CITY = ?, BUILDING = ?, APARTMENT = ? WHERE ID = ?")) {
            statement.setLong(5, element.getId());
            insertOrUpdate(element, statement);
        }
    }

    @Override
    public void delete(Address element) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("DELETE FROM ADDRESSES WHERE ID = ?")) {
            statement.setLong(1, element.getId());
            statement.execute();
        }
    }

    private static void insertOrUpdate(Address element, PreparedStatement statement) throws SQLException {
        statement.setString(1, element.getCountry());
        statement.setString(2, element.getCity());
        statement.setString(3, element.getBuilding());
        statement.setInt(4, element.getApartment());
        statement.execute();
    }

    static Address createAddressFromStatement(PreparedStatement statement) throws SQLException {
        Address address = null;
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                address = new Address();
                setUpFields(address, resultSet);
            }
        }
        return address;
    }

    static List<Address> createAddressesFromStatement(PreparedStatement statement) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        try (ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Address address = new Address();
                setUpFields(address, resultSet);
                addresses.add(address);
            }
        }
        return addresses;
    }

    static void setUpFields(Address address, ResultSet resultSet) throws SQLException {
        address.setId(resultSet.getLong("id"));
        address.setCountry(resultSet.getString("country"));
        address.setBuilding(resultSet.getString("building"));
        address.setApartment(resultSet.getInt("apartment"));
        address.setCreateDate(new Date(resultSet.getTimestamp("create_date").getTime()));
        address.setLastUpdate(new Date(resultSet.getTimestamp("last_update").getTime()));
    }

    @Override
    public List<Address> findLimited(int offset) throws SQLException {
        List<Address> addresses;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM PRODUCTS LIMIT ?, ?")) {
            statement.setInt(2, Constants.PRODUCT_LIMIT);
            statement.setInt(1, offset);
            addresses = createAddressesFromStatement(statement);
        }
        return addresses;
    }

    @Override
    public long count() throws SQLException {
        return 0;
    }
}
