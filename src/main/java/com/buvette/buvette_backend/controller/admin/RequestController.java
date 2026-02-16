package com.buvette.buvette_backend.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.model.shared.Order;
import com.buvette.buvette_backend.model.worker.RequestRegister;
import com.buvette.buvette_backend.services.shared.OrderService;
import com.buvette.buvette_backend.services.worker.RequestService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class RequestController {
    private final RequestService requestService;
    private final OrderService orderService;

    public RequestController(RequestService requestService, OrderService orderService) {
        this.requestService = requestService;
        this.orderService = orderService;
    }

    @GetMapping("/requests")
    public List<RequestRegister> getrequets() {
        return requestService.getAllRequets();
    }

    @PostMapping("worker-requests/{requestId}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable String requestId) {
        requestService.approveRequest(requestId);
        return ResponseEntity.ok("request approved");
    }

    @PostMapping("worker-requests/{requestId}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable String requestId) {
        requestService.rejectRequest(requestId);
        return ResponseEntity.ok("request rejected");
    }

     @GetMapping("{requestId}/user-details")
    public User getUserDetails( @PathVariable String requestId){
        return requestService.getUserDetails(requestId);

    }
    
    @GetMapping("/orders")
    public List<Order>getOrders(){
        return orderService.getOrders();
    }

    @PutMapping("workers/{email}/{endpoint}")
    public ResponseEntity<?> toggleStatusofUser(@PathVariable String email,@PathVariable String endpoint) {
        requestService.updateUserStatus(email, endpoint);
        return ResponseEntity.ok("status updated");
    }
    

    

   
    

}
