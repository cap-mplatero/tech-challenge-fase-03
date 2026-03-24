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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderUseCase {

    private static final String PENDING_STATUS = "AGUARDANDO_PAGAMENTO";

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
                dto.getMenuItemQuantities()
        );

        Order saved = orderRepository.save(order);

        BigDecimal total = calculateTotal(dto.getMenuItemQuantities());
        orderEventPublisher.publishOrderCreated(new OrderCreatedEvent(
                saved.getId(),
                saved.getCustomerId(),
                saved.getRestaurantId(),
                saved.getMenuItemQuantities().keySet(),
                total
        ));

        return toDTO(saved);
    }

    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
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
                dto.getMenuItemQuantities()
        );

        Order updated = orderRepository.update(order);
        return toDTO(updated);
    }

    public OrderDTO updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));

        Order updatedOrder = Order.create(
                order.getId(),
                order.getCustomerId(),
                order.getRestaurantId(),
                status,
                order.getMenuItemQuantities()
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

    private BigDecimal calculateTotal(Map<Long, Integer> menuItemQuantities) {
        if (menuItemQuantities == null || menuItemQuantities.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return menuItemQuantities.entrySet().stream()
                .map(entry -> {
                    MenuItem item = menuItemRepository.findById(entry.getKey())
                            .orElseThrow(() -> new EntityNotFoundException("Menu item not found: " + entry.getKey()));
                    return item.getPrice().multiply(BigDecimal.valueOf(entry.getValue()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateOrderReferences(OrderDTO dto) {
        if (!customerRepository.existsById(dto.getCustomerId())) {
            throw new EntityNotFoundException("Customer not found with id: " + dto.getCustomerId());
        }
        if (!restaurantRepository.existsById(dto.getRestaurantId())) {
            throw new EntityNotFoundException("Restaurant not found with id: " + dto.getRestaurantId());
        }
        if (dto.getMenuItemQuantities() != null) {
            for (Long menuItemId : dto.getMenuItemQuantities().keySet()) {
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
                .menuItemQuantities(order.getMenuItemQuantities())
                .status(order.getStatus())
                .build();
    }
}
