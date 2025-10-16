package ru.arapov.somerestjpafitches.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddToCartRequest(
        @NotNull Long itemId,
        @NotNull @Min(1) Integer quantity
) {
    public AddToCartRequest {
        if (quantity == null) {
            quantity = 1;
        }
    }
}
