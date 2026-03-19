package br.com.fiap.techchallenge.orderservice.infrastructure.adapters.repositories;

import br.com.fiap.techchallenge.orderservice.application.ports.output.OrderRepository;
import br.com.fiap.techchallenge.orderservice.domain.entities.Order;
import br.com.fiap.techchallenge.orderservice.infrastructure.database.entities.*;
import br.com.fiap.techchallenge.orderservice.infrastructure.database.repositories.OrderJpaRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final EntityManager entityManager;

    @Override
    @Transactional
    public Order save(Order order) {
        OrderEntity entity = toEntity(order);
        OrderEntity saved = orderJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Order> findById(Long id) {
        return orderJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Order update(Order order) {
        OrderEntity entity = toEntity(order);
        OrderEntity updated = orderJpaRepository.save(entity);
        return toDomain(updated);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        orderJpaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByCustomerId(Long customerId) {
        return orderJpaRepository.findByCustomerId(customerId).stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByStatus(String status) {
        return orderJpaRepository.findByStatus(status).stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByRestaurantId(Long restaurantId) {
        return orderJpaRepository.findByRestaurantId(restaurantId).stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return orderJpaRepository.existsById(id);
    }

    private OrderEntity toEntity(Order order) {
        CustomerEntity customerRef = entityManager.getReference(CustomerEntity.class, order.getCustomerId());
        RestaurantEntity restaurantRef = entityManager.getReference(RestaurantEntity.class, order.getRestaurantId());

        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId())
                .status(order.getStatus())
                .customer(customerRef)
                .restaurant(restaurantRef)
                .orderMenuItems(new ArrayList<>())
                .build();

        if (order.getMenuItemQuantities() != null) {
            List<OrderMenuItemEntity> items = order.getMenuItemQuantities().entrySet().stream()
                    .map(entry -> {
                        MenuItemEntity menuItemRef = entityManager.getReference(MenuItemEntity.class, entry.getKey());

                        return OrderMenuItemEntity.builder()
                                .id(new OrderMenuItemId(order.getId(), entry.getKey()))
                                .order(orderEntity)
                                .menuItem(menuItemRef)
                                .quantity(entry.getValue())
                                .build();
                    })
                    .collect(Collectors.toList());
            orderEntity.getOrderMenuItems().addAll(items);
        }

        return orderEntity;
    }

    private Order toDomain(OrderEntity entity) {
        Map<Long, Integer> menuItemQuantities = new HashMap<>();
        if (entity.getOrderMenuItems() != null) {
            entity.getOrderMenuItems().forEach(item ->
                    menuItemQuantities.put(item.getMenuItem().getId(), item.getQuantity())
            );
        }

        return Order.create(
                entity.getId(),
                entity.getCustomer().getId(),
                entity.getRestaurant().getId(),
                entity.getStatus(),
                menuItemQuantities
        );
    }
}


