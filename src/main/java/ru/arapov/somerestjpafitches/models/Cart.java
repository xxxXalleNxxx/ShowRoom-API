package ru.arapov.somerestjpafitches.models;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    User user;

    // каскад если чё определяет какие операции распространяет родитель на пиздюков -> PERSIST, MERGE, REMOVE, REFRESH, DETACH
    // orphan - удаление дочерних при удалении предков, юзать когда дочерние зависят от предков
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    List<CartItem> items = new ArrayList<>();

    public BigDecimal getTotalPrice() {
        return items.stream()
                .map(CartItem::getSubTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Integer getTotalItems() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
    }
}
