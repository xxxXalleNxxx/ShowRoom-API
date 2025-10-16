package ru.arapov.somerestjpafitches.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.arapov.somerestjpafitches.dtos.AddToCartRequest;
import ru.arapov.somerestjpafitches.dtos.CartDto;
import ru.arapov.somerestjpafitches.dtos.UpdateCartItemRequest;
import ru.arapov.somerestjpafitches.services.CartService;

@RestController
@RequestMapping("/api/v1/users/{userId}/cart")
@Validated
@Slf4j
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartDto> getOrCreateCart(@PathVariable Long userId) {
        CartDto cart = cartService.getCart(userId);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    public ResponseEntity<CartDto> addItem(
            @PathVariable Long userId,
            @Valid @RequestBody AddToCartRequest request) {
        CartDto cart = cartService.addItem(userId, request);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartDto> updateItem(
            @PathVariable Long userId,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        CartDto cart = cartService.updateItemQuantity(userId, itemId, request);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartDto> removeItem(
            @PathVariable Long userId,
            @PathVariable Long itemId) {
        CartDto cart = cartService.removeItem(userId, itemId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping
    public ResponseEntity<CartDto> clearCart(@PathVariable Long userId) {
        CartDto cart = cartService.clearCart(userId);
        return ResponseEntity.ok(cart);
    }
}