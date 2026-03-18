package br.com.fiap.techchallenge.orderservice.infrastructure.database.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderMenuItemId implements Serializable {

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "menu_item_id")
    private Long menuItemId;
}

