package br.com.fiap.techchallenge.orderservice.infrastructure.database.repositories;

import br.com.fiap.techchallenge.orderservice.infrastructure.database.entities.RestaurantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, Long> {

    List<RestaurantEntity> findByNameContainingIgnoreCase(String name);

    List<RestaurantEntity> findByCuisineTypeIgnoreCase(String cuisineType);
}

