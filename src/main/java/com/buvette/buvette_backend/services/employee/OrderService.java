package com.buvette.buvette_backend.services.employee;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.buvette.buvette_backend.model.shared.Order;
import com.buvette.buvette_backend.repository.employee.OrderRepository;
@Service
public class OrderService {
    @Autowired
    private OrderRepository repo;
    public void save(Order o){
        repo.save(o);
    }
    public Optional<Order> getLastOrderByUser(String userId){
        return repo.findFirstByUserIdOrderByCreatedAtDesc(userId);

    }
}
