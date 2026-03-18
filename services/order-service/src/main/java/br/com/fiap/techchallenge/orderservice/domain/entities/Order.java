package br.com.fiap.techchallenge.orderservice.domain.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
public class Order {
    private Long id;
    private Long customerId;
    private Long restaurantId;
    private Map<Long, Integer> menuItemQuantities; // menuItemId -> quantity
    private String status;

    public static Order create(Long customerId, Long restaurantId, String status, Map<Long, Integer> menuItemQuantities) {
        return Order.create(null, customerId, restaurantId, status, menuItemQuantities);
    }

    public static Order create(Long id, Long customerId, Long restaurantId, String status, Map<Long, Integer> menuItemQuantities) {
        validateOrder(customerId, restaurantId, status, menuItemQuantities);

        Order order = new Order();
        order.setId(id);
        order.setCustomerId(customerId);
        order.setRestaurantId(restaurantId);
        order.setStatus(status);
        order.setMenuItemQuantities(menuItemQuantities);

        return order;
    }

    private static void validateOrder(Long customerId, Long restaurantId, String status, Map<Long, Integer> menuItemQuantities) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        if (restaurantId == null) {
            throw new IllegalArgumentException("Restaurant ID is required");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Order status is required");
        }
        if (menuItemQuantities == null || menuItemQuantities.isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one menu item");
        }
        menuItemQuantities.forEach((itemId, quantity) -> {
            if (quantity == null || quantity <= 0) {
                throw new IllegalArgumentException("Quantity for menu item " + itemId + " must be greater than zero");
            }
        });
    }
}
