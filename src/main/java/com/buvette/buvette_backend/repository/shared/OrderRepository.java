package com.buvette.buvette_backend.repository.shared;

import com.buvette.buvette_backend.model.shared.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {}
