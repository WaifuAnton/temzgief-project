package com.shop.dao.factory;

import com.shop.dao.*;
import com.shop.dao.impl.h2.H2UserDao;
import com.shop.entity.Address;
import com.shop.entity.Delivery;

public class H2Factory extends DaoFactory {
    private static H2Factory instance;

    private H2Factory() {
        //object created via getInstance()
    }

    public static H2Factory getInstance() {
        if (instance == null)
            instance = new H2Factory();
        return instance;
    }

    @Override
    public UserDao getUserDao() {
        return new H2UserDao();
    }

    @Override
    public ProductDao getProductDao() {
        return null;
    }

    @Override
    public CategoryDao getCategoryDao() {
        return null;
    }

    @Override
    public PageDao<Address> getAddressDao() {
        return null;
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
