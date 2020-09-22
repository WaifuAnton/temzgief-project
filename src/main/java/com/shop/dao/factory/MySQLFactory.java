package com.shop.dao.factory;

import com.shop.dao.*;
import com.shop.dao.impl.mysql.MySQLAddressDao;
import com.shop.dao.impl.mysql.MySQLCategoryDao;
import com.shop.dao.impl.mysql.MySQLProductDao;
import com.shop.dao.impl.mysql.MySQLUserDao;
import com.shop.entity.Address;
import com.shop.entity.Delivery;

public class MySQLFactory extends DaoFactory {
    private static MySQLFactory instance;

    private MySQLFactory() {
        //object created via getInstance()
    }

    public static synchronized MySQLFactory getInstance() {
        if (instance == null)
            instance = new MySQLFactory();
        return instance;
    }

    public UserDao getUserDao() {
        return new MySQLUserDao();
    }

    @Override
    public ProductDao getProductDao() {
        return new MySQLProductDao();
    }

    @Override
    public CategoryDao getCategoryDao() {
        return new MySQLCategoryDao();
    }

    @Override
    public PageDao<Address> getAddressDao() {
        return new MySQLAddressDao();
    }

    @Override
    public Dao<Delivery> getDeliveryDao() {
        return null;
    }

    @Override
    public OrderDao getOrderDao() {
        return null;
    }
}
