package com.buvette.buvette_backend.repository;

import java.util.List;
import java.util.Locale.Category;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.buvette.buvette_backend.enumAttribute.ItemCategory;
import com.buvette.buvette_backend.model.MenuItem;

public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
    List<MenuItem> findByItemCategory(ItemCategory itemCategory);
}
