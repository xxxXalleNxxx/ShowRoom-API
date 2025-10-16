package ru.arapov.somerestjpafitches.dtos;

import lombok.Data;
import ru.arapov.somerestjpafitches.models.CartItem;

import java.math.BigDecimal;

@Data
public class CartItemDto {

    private Long id;
    private Long itemId;
    private String itemName;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subTotal;

    public static CartItemDto from(CartItem cartItem) {
        CartItemDto dto = new CartItemDto();
        dto.setId(cartItem.getId());
        dto.setItemId(cartItem.getItem().getId());
        dto.setItemName(cartItem.getItem().getItemName());
        dto.setQuantity(cartItem.getQuantity());
        dto.setPrice(cartItem.getPrice());
        dto.setSubTotal(cartItem.getSubTotal());
        return dto;
    }
}
