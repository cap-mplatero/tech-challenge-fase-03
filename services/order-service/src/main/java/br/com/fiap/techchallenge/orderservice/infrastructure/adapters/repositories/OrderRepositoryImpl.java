package br.com.fiap.techchallenge.orderservice.infrastructure.adapters.repositories;

import br.com.fiap.techchallenge.orderservice.application.ports.output.OrderRepository;
import br.com.fiap.techchallenge.orderservice.domain.entities.Order;
import br.com.fiap.techchallenge.orderservice.infrastructure.database.entities.CustomerEntity;
import br.com.fiap.techchallenge.orderservice.infrastructure.database.entities.MenuItemEntity;
import br.com.fiap.techchallenge.orderservice.infrastructure.database.entities.OrderEntity;
import br.com.fiap.techchallenge.orderservice.infrastructure.database.entities.RestaurantEntity;
import br.com.fiap.techchallenge.orderservice.infrastructure.database.repositories.OrderJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        OrderEntity saved = orderJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Order> findAll() {
        return orderJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Order update(Order order) {
        OrderEntity entity = toEntity(order);
        OrderEntity updated = orderJpaRepository.save(entity);
        return toDomain(updated);
    }

    @Override
    public void deleteById(Long id) {
        orderJpaRepository.deleteById(id);
    }

    @Override
    public List<Order> findByCustomerId(Long customerId) {
        return orderJpaRepository.findByCustomerId(customerId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Order> findByStatus(String status) {
        return orderJpaRepository.findByStatus(status).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Order> findByRestaurantId(Long restaurantId) {
        return orderJpaRepository.findByRestaurantId(restaurantId).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsById(Long id) {
        return orderJpaRepository.existsById(id);
    }

    private OrderEntity toEntity(Order order) {
        CustomerEntity customerRef = new CustomerEntity();
        customerRef.setId(order.getCustomerId());

        RestaurantEntity restaurantRef = new RestaurantEntity();
        restaurantRef.setId(order.getRestaurantId());

        Set<MenuItemEntity> menuItemEntities = Collections.emptySet();
        if (order.getMenuItemSet() != null) {
            menuItemEntities = order.getMenuItemSet().stream()
                    .map(menuItemId -> {
                        MenuItemEntity menuItemRef = new MenuItemEntity();
                        menuItemRef.setId(menuItemId);
                        return menuItemRef;
                    })
                    .collect(Collectors.toSet());
        }

        return OrderEntity.builder()
                .id(order.getId())
                .status(order.getStatus())
                .customer(customerRef)
                .restaurant(restaurantRef)
                .menuItems(menuItemEntities)
                .build();
    }

    private Order toDomain(OrderEntity entity) {
        Set<Long> menuItemIds = Collections.emptySet();
        if (entity.getMenuItems() != null) {
            menuItemIds = entity.getMenuItems().stream()
                    .map(MenuItemEntity::getId)
                    .collect(Collectors.toSet());
        }

        return Order.create(
                entity.getId(),
                entity.getCustomer().getId(),
                entity.getRestaurant().getId(),
                entity.getStatus(),
                menuItemIds
        );
    }
}

