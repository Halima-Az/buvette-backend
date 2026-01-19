package com.buvette.buvette_backend.repository.employee;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.buvette.buvette_backend.model.employee.Order;

@Repository
public interface OrderRepository extends MongoRepository<Order,String>{
    Optional<Order> findFirstByUserIdOrderByCreatedAtDesc(String userId);
}
