package com.shop.dao.factory;

import com.shop.dao.CategoryDao;
import com.shop.dao.ProductDao;
import com.shop.dao.UserDao;
import com.shop.dao.impl.h2.H2UserDao;

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
}
