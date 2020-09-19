package com.shop.dao;

import com.shop.entity.Category;

import java.util.Optional;

public interface CategoryDao extends Dao<Category> {
    Optional<Category> getByName(String name);
}
