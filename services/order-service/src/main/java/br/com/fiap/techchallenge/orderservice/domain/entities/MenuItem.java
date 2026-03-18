package br.com.fiap.techchallenge.orderservice.domain.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
public class MenuItem {
    private Long id;
    private Long restaurantId;
    private String name;
    private BigDecimal price;

    public static MenuItem create(String name, BigDecimal price, Long restaurantId) {
        return MenuItem.create(null, name, price, restaurantId);
    }

    public static MenuItem create(Long id, String name, BigDecimal price, Long restaurantId) {
        validateMenuItem(name, price, restaurantId);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(id);
        menuItem.setName(name);
        menuItem.setPrice(price);
        menuItem.setRestaurantId(restaurantId);

        return menuItem;
    }

    private static void validateMenuItem(String name, BigDecimal price, Long restaurantId) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Menu item name is required");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero");
        }
        if (restaurantId == null) {
            throw new IllegalArgumentException("Restaurant ID is required");
        }
    }
}
