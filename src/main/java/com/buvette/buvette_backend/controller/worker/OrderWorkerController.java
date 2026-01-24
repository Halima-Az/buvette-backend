package com.buvette.buvette_backend.controller.worker;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buvette.buvette_backend.dto.worker.UpdateOrderStatusRequest;
import com.buvette.buvette_backend.model.shared.Order;
import com.buvette.buvette_backend.services.shared.OrderService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/worker/orders")
@PreAuthorize("hasRole('WORKER')")

public class OrderWorkerController {
    private final OrderService orderService;
    public OrderWorkerController(OrderService orderService){
        this.orderService=orderService;
    }
    @GetMapping
    public List<Order>getOrders(){
        return orderService.getOrders();
    } 
    @PutMapping("/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(
        @PathVariable String id,
        @RequestBody UpdateOrderStatusRequest request) {

        Order order = orderService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(order);
    }

}
