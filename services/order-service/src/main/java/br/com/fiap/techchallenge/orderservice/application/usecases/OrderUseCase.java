package br.com.fiap.techchallenge.orderservice.application.usecases;

import br.com.fiap.techchallenge.orderservice.application.dtos.OrderCreatedEvent;
import br.com.fiap.techchallenge.orderservice.application.dtos.OrderDTO;
import br.com.fiap.techchallenge.orderservice.application.ports.output.CustomerRepository;
import br.com.fiap.techchallenge.orderservice.application.ports.output.MenuItemRepository;
import br.com.fiap.techchallenge.orderservice.application.ports.output.OrderEventPublisher;
import br.com.fiap.techchallenge.orderservice.application.ports.output.OrderRepository;
import br.com.fiap.techchallenge.orderservice.application.ports.output.RestaurantRepository;
import br.com.fiap.techchallenge.orderservice.domain.entities.MenuItem;
import br.com.fiap.techchallenge.orderservice.domain.entities.Order;
import br.com.fiap.techchallenge.orderservice.domain.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderUseCase {

    private static final String PENDING_STATUS = "PENDING";

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderEventPublisher orderEventPublisher;

    public OrderDTO createOrder(OrderDTO dto) {
        validateOrderReferences(dto);

        Order order = Order.create(
                dto.getCustomerId(),
                dto.getRestaurantId(),
                PENDING_STATUS,
                dto.getMenuItemIds()
        );

        Order saved = orderRepository.save(order);

        BigDecimal total = calculateTotal(dto.getMenuItemIds());
        orderEventPublisher.publishOrderCreated(new OrderCreatedEvent(
                saved.getId(),
                saved.getCustomerId(),
                saved.getRestaurantId(),
                saved.getMenuItemSet(),
                total
        ));

        return toDTO(saved);
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return toDTO(order);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public OrderDTO updateOrder(Long id, OrderDTO dto) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with id: " + id);
        }
        validateOrderReferences(dto);

        Order order = Order.create(
                id,
                dto.getCustomerId(),
                dto.getRestaurantId(),
                dto.getStatus(),
                dto.getMenuItemIds()
        );

        Order updated = orderRepository.update(order);
        return toDTO(updated);
    }

    public OrderDTO updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        Order updatedOrder = Order.create(
                order.getId(),
                order.getCustomerId(),
                order.getRestaurantId(),
                status,
                order.getMenuItemSet()
        );

        Order saved = orderRepository.update(updatedOrder);
        return toDTO(saved);
    }

    public List<OrderDTO> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream().map(this::toDTO).toList();
    }

    public List<OrderDTO> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status).stream().map(this::toDTO).toList();
    }

    public List<OrderDTO> getOrdersByRestaurantId(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId).stream().map(this::toDTO).toList();
    }

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    private BigDecimal calculateTotal(java.util.Set<Long> menuItemIds) {
        if (menuItemIds == null || menuItemIds.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return menuItemIds.stream()
                .map(menuItemRepository::findById)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .map(MenuItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateOrderReferences(OrderDTO dto) {
        if (!customerRepository.existsById(dto.getCustomerId())) {
            throw new EntityNotFoundException("Customer not found with id: " + dto.getCustomerId());
        }
        if (!restaurantRepository.existsById(dto.getRestaurantId())) {
            throw new EntityNotFoundException("Restaurant not found with id: " + dto.getRestaurantId());
        }
        if (dto.getMenuItemIds() != null) {
            for (Long menuItemId : dto.getMenuItemIds()) {
                if (!menuItemRepository.existsById(menuItemId)) {
                    throw new EntityNotFoundException("Menu item not found with id: " + menuItemId);
                }
            }
        }
    }

    private OrderDTO toDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .customerId(order.getCustomerId())
                .restaurantId(order.getRestaurantId())
                .menuItemIds(order.getMenuItemSet())
                .status(order.getStatus())
                .build();
    }
}
