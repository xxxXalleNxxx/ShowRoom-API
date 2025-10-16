package ru.arapov.somerestjpafitches.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.arapov.somerestjpafitches.dtos.AddToCartRequest;
import ru.arapov.somerestjpafitches.dtos.CartDto;
import ru.arapov.somerestjpafitches.dtos.UpdateCartItemRequest;
import ru.arapov.somerestjpafitches.exceptions.UserExceptions;
import ru.arapov.somerestjpafitches.models.Cart;
import ru.arapov.somerestjpafitches.models.CartItem;
import ru.arapov.somerestjpafitches.models.Item;
import ru.arapov.somerestjpafitches.models.User;
import ru.arapov.somerestjpafitches.repos.CartItemRepository;
import ru.arapov.somerestjpafitches.repos.CartRepository;
import ru.arapov.somerestjpafitches.repos.ItemRepository;
import ru.arapov.somerestjpafitches.repos.UserRepository;

@Service
@Transactional
@Slf4j
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public CartDto addItem(Long userId, AddToCartRequest request) {
        log.info("ADDING ITEM {} TO CART FOR USER {}", request.itemId(), userId);

        Cart cart = getOrCreateCart(userId);
        Item item = getAvailableItem(request.itemId());
        validateStock(item, request.quantity());

        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setItem(item);
        newItem.setQuantity(request.quantity());
        newItem.setPrice(item.getPrice());

        cartItemRepository.save(newItem);

        return getCart(userId);
    }

    public CartDto updateItemQuantity(Long userId, Long itemId, UpdateCartItemRequest request) {
        log.info("Updating quantity for item {} in user {} cart to {}", itemId, userId, request.quantity());

        Cart cart = getCartByUserId(userId);
        Item item = getAvailableItem(itemId);

        if (request.quantity() == 0) {
            cartItemRepository.deleteByCartIdAndItemId(cart.getId(), itemId);
            log.info("Removed item {} from cart", itemId);
        } else {
            validateStock(item, request.quantity());

            CartItem cartItem = cartItemRepository.findByCartIdAndItemId(cart.getId(), itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found in cart"));

            cartItem.setQuantity(request.quantity());
            cartItemRepository.save(cartItem);
            log.info("Updated quantity for item {} to {}", itemId, request.quantity());
        }

        return getCart(userId);
    }

    public CartDto removeItem(Long userId, Long itemId) {
        log.info("Removing item {} from user {} cart", itemId, userId);

        Cart cart = getCartByUserId(userId);
        cartItemRepository.deleteByCartIdAndItemId(cart.getId(), itemId);

        log.info("Item {} removed from cart", itemId);
        return getCart(userId);
    }

    public CartDto clearCart(Long userId) {
        log.info("Clearing cart for user {}", userId);

        Cart cart = getCartByUserId(userId);
        cartItemRepository.deleteAllByCartId(cart.getId());

        log.info("Cart cleared for user {}", userId);
        return getCart(userId);
    }

    @Transactional(readOnly = true)
    public CartDto getCart(Long userId) {
        Cart cart = getCartByUserId(userId);
        return CartDto.from(cart);
    }

    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UserExceptions("User not found"));

                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });
    }

    private Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user"));
    }

    private Item getAvailableItem(Long itemId) {
        return itemRepository.findItemById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found or not available"));
    }

    private void validateStock(Item item, Integer requestedQuantity) {
        Integer stockQuantity = item.getStockQuantity();

        if (stockQuantity != null && stockQuantity < requestedQuantity) {
            throw new RuntimeException(
                    "Not enough stock. Available: " + stockQuantity +
                            ", requested: " + requestedQuantity
            );
        }
    }
}