package br.com.fiap.techchallenge.orderservice.application.usecases;

import br.com.fiap.techchallenge.orderservice.application.dtos.OrderDTO;
import br.com.fiap.techchallenge.orderservice.application.ports.output.CustomerRepository;
import br.com.fiap.techchallenge.orderservice.application.ports.output.MenuItemRepository;
import br.com.fiap.techchallenge.orderservice.application.ports.output.OrderRepository;
import br.com.fiap.techchallenge.orderservice.application.ports.output.RestaurantRepository;
import br.com.fiap.techchallenge.orderservice.domain.entities.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderUseCase {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public OrderDTO createOrder(OrderDTO dto) {
        validateOrderReferences(dto);

        Order order = Order.create(
                dto.getCustomerId(),
                dto.getRestaurantId(),
                dto.getStatus(),
                dto.getMenuItemIds()
        );

        Order saved = orderRepository.save(order);
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
            throw new RuntimeException("Order not found with id: " + id);
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

    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new RuntimeException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    private void validateOrderReferences(OrderDTO dto) {
        if (!customerRepository.existsById(dto.getCustomerId())) {
            throw new RuntimeException("Customer not found with id: " + dto.getCustomerId());
        }
        if (!restaurantRepository.existsById(dto.getRestaurantId())) {
            throw new RuntimeException("Restaurant not found with id: " + dto.getRestaurantId());
        }
        if (dto.getMenuItemIds() != null) {
            for (Long menuItemId : dto.getMenuItemIds()) {
                if (!menuItemRepository.existsById(menuItemId)) {
                    throw new RuntimeException("Menu item not found with id: " + menuItemId);
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
