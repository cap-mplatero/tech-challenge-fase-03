package br.com.fiap.techchallenge.orderservice.infrastructure.database.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_menu_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderMenuItemEntity {

    @EmbeddedId
    private OrderMenuItemId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("menuItemId")
    @JoinColumn(name = "menu_item_id")
    private MenuItemEntity menuItem;

    @Column(nullable = false)
    private Integer quantity;
}

