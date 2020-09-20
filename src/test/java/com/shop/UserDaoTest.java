package com.shop;

import com.shop.config.Constants;
import com.shop.dao.UserDao;
import com.shop.dao.factory.DaoFactory;
import com.shop.entity.User;
import com.shop.enumeration.Role;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class UserDaoTest {
    UserDao userDao;

    @Before
    public void beforeEach() {
        userDao = DaoFactory.getDaoFactory(Constants.H2).getUserDao();
    }

    @Test
    public void shouldInsertUsers() throws SQLException {
        User user = new User();
        user.setEmail("mmmm@dd.ua");
        user.setSalt("AAA");
        user.setPasswordHash(new byte[0]);
        user.setRole(Role.CUSTOMER);
        userDao.insert(user);
        assertEquals(1, userDao.findAll().size());
    }
}