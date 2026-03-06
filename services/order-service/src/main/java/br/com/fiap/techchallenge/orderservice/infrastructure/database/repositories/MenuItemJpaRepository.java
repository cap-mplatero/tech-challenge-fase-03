package br.com.fiap.techchallenge.orderservice.infrastructure.database.repositories;

import br.com.fiap.techchallenge.orderservice.infrastructure.database.entities.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuItemJpaRepository extends JpaRepository<MenuItemEntity, Long> {

    List<MenuItemEntity> findByRestaurantId(Long restaurantId);

    List<MenuItemEntity> findByNameContainingIgnoreCase(String name);
}

