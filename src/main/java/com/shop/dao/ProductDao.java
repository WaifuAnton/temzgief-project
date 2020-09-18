package com.shop.dao;

import com.shop.entity.Product;

public interface ProductDao extends Dao<Product> {
    Product getByName(String name);
}
