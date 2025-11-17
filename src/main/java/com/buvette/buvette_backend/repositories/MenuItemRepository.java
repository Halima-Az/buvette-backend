package com.buvette.buvette_backend.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.buvette.buvette_backend.model.MenuItem;

public interface MenuItemRepository extends MongoRepository<MenuItem, String> {
}
