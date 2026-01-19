package com.buvette.buvette_backend.controller.client;

import com.buvette.buvette_backend.model.client.CartItem;
import com.buvette.buvette_backend.model.client.CartUpdateRequest;
import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.repository.shared.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final UserRepository userRepository;

    public CartController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET CART
    @GetMapping
    public List<CartItem> getCart(@AuthenticationPrincipal String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getCart();
    }

    // UPDATE QUANTITY
    @PostMapping("/update")
    public ResponseEntity<?> updateCart(
            @RequestBody CartUpdateRequest request,
            @AuthenticationPrincipal String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cart = user.getCart();

        CartItem existing = cart.stream()
            .filter(c -> c.getItemId().equals(request.getItemId()))
            .findFirst()
            .orElse(null);

        if (request.getQuantity() <= 0) {
            if (existing != null) cart.remove(existing);
        } else {
            if (existing != null) {
                existing.setQuantity(request.getQuantity());
            } else {
                cart.add(new CartItem(request.getItemId(), request.getQuantity()));
            }
        }

        userRepository.save(user);

        return ResponseEntity.ok(cart);
    }

    // CLEAR CART
    @PostMapping("/clear")
    public ResponseEntity<?> clearCart(@AuthenticationPrincipal String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.getCart().clear();
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}
