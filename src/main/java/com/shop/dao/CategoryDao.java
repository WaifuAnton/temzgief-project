package com.shop.dao;

import com.shop.entity.Category;

public interface CategoryDao extends Dao<Category> {
    Category getByName(String name);
}
