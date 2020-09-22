package com.shop.dao.impl.mysql;

import com.shop.dao.Dao;
import com.shop.entity.Address;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class MySQLAddressDao implements Dao<Address> {
    @Override
    public Optional<Address> getById(long id) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<Address> findAll() throws SQLException {
        return null;
    }

    @Override
    public void insert(Address element) throws SQLException {

    }

    @Override
    public void update(Address element) throws SQLException {

    }

    @Override
    public void delete(Address element) throws SQLException {

    }

    private void insertOrUpdate(Address element, PreparedStatement statement) throws SQLException {
        statement.setString(1, element.getCountry());
        statement.setString(2, element.getCity());
        statement.setString(3, element.getBuilding());
        statement.setInt(4, element.getApartment());
        statement.execute();
    }

    private Address createUserFromStatement(PreparedStatement statement) throws SQLException {
        Address address = null;
        try (ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                address = new Address();
                setUpFields(address, resultSet);
            }
        }
        return address;
    }

    private List<Address> createUsersFromStatement(PreparedStatement statement) throws SQLException {
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

    private void setUpFields(Address address, ResultSet resultSet) throws SQLException {
        address.setId(resultSet.getLong("id"));
        address.setCountry(resultSet.getString("country"));
        address.setBuilding(resultSet.getString("building"));
        address.setApartment(resultSet.getInt("apartment"));
        address.setCreateDate(new Date(resultSet.getTimestamp("create_date").getTime()));
        address.setLastUpdate(new Date(resultSet.getTimestamp("last_update").getTime()));
    }
}
