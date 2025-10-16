package ru.arapov.somerestjpafitches.dtos;

import lombok.Data;
import ru.arapov.somerestjpafitches.models.Cart;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CartDto {

    private Long id;
    private Long userId;
    private List<CartItemDto> items;
    private Integer totalItems;
    private BigDecimal totalPrice;

    public static CartDto from(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setItems(cart.getItems().stream()
                .map(CartItemDto::from)
                .collect(Collectors.toList()));
        dto.setTotalItems(cart.getTotalItems());
        dto.setTotalPrice(cart.getTotalPrice());
        return dto;
    }

}
