package br.com.fiap.techchallenge.orderservice.application.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDTO {
    private Long id;
    private Long restaurantId;
    private String name;
    private Integer quantity;
    private BigDecimal price;
}

