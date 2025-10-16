package ru.arapov.somerestjpafitches.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "item_name", nullable = false)
    String itemName;

    String description;

    @Column(name = "item_price", nullable = false)
    BigDecimal price;

    @Column(name = "stock_quantity")
    Integer stockQuantity;
}
