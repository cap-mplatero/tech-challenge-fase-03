package br.com.fiap.techchallenge.orderservice.domain.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
public class Order {
    private Long id;
    private Long customerId;
    private Long restaurantId;
    private Set<Long> menuItemSet;
    private String status;

    public static Order create(Long customerId, Long restaurantId, String status, Set<Long> menuItemSet) {
        return Order.create(null, customerId, restaurantId, status, menuItemSet);
    }

    public static Order create(Long id, Long customerId, Long restaurantId, String status, Set<Long> menuItemSet) {
        validateOrder(customerId, restaurantId, status, menuItemSet);

        Order order = new Order();
        order.setId(id);
        order.setCustomerId(customerId);
        order.setRestaurantId(restaurantId);
        order.setStatus(status);
        order.setMenuItemSet(menuItemSet);

        return order;
    }

    private static void validateOrder(Long customerId, Long restaurantId, String status, Set<Long> menuItemSet) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID is required");
        }
        if (restaurantId == null) {
            throw new IllegalArgumentException("Restaurant ID is required");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Order status is required");
        }
    }
}
