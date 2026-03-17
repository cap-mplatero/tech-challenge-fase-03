package br.com.fiap.techchallenge.orderservice.application.dtos;

import java.math.BigDecimal;
import java.util.Set;

public record OrderCreatedEvent(
        Long orderId,
        Long customerId,
        Long restaurantId,
        Set<Long> menuItemIds,
        BigDecimal totalAmount
) {}
