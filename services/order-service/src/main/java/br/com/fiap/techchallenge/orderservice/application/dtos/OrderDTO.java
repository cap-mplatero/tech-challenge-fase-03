package br.com.fiap.techchallenge.orderservice.application.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long customerId;
    @NotNull
    private Long restaurantId;
    @NotEmpty
    private Map<Long, Integer> menuItemQuantities; // menuItemId -> quantity
    private String status;
}
