package com.buvette.buvette_backend.controller.client;

import com.buvette.buvette_backend.model.client.CartItem;
import com.buvette.buvette_backend.model.client.CartUpdateRequest;
import com.buvette.buvette_backend.model.client.MenuItem;
import com.buvette.buvette_backend.model.client.User;
import com.buvette.buvette_backend.repository.client.MenuItemRepository;
import com.buvette.buvette_backend.repository.shared.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client/cart")
@PreAuthorize("hasRole('CLIENT')")
public class CartController {

    private final UserRepository userRepository;
    private final MenuItemRepository itemRepository;

    public CartController(UserRepository userRepository,MenuItemRepository itemRepository) {
        this.userRepository = userRepository;
        this.itemRepository = itemRepository;
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

        MenuItem item = itemRepository.findById(request.getItemId())
                .orElseThrow(() -> new RuntimeException("Item not found"));

        if (!item.getAvailability()) {
            return ResponseEntity
                    .badRequest()
                    .body("This item is currently unavailable and cannot be added to cart");
        }

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
