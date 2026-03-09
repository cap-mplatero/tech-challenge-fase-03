package br.com.fiap.techchallenge.orderservice.infrastructure.adapters.repositories;

import br.com.fiap.techchallenge.orderservice.application.ports.output.RestaurantRepository;
import br.com.fiap.techchallenge.orderservice.domain.entities.Restaurant;
import br.com.fiap.techchallenge.orderservice.infrastructure.database.entities.RestaurantEntity;
import br.com.fiap.techchallenge.orderservice.infrastructure.database.repositories.RestaurantJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RestaurantRepositoryImpl implements RestaurantRepository {

    private final RestaurantJpaRepository restaurantJpaRepository;

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantEntity entity = toEntity(restaurant);
        RestaurantEntity saved = restaurantJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return restaurantJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        return restaurantJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Restaurant update(Restaurant restaurant) {
        RestaurantEntity entity = toEntity(restaurant);
        RestaurantEntity updated = restaurantJpaRepository.save(entity);
        return toDomain(updated);
    }

    @Override
    public void deleteById(Long id) {
        restaurantJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return restaurantJpaRepository.existsById(id);
    }

    private RestaurantEntity toEntity(Restaurant restaurant) {
        return RestaurantEntity.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .cuisineType(restaurant.getCuisineType())
                .build();
    }

    private Restaurant toDomain(RestaurantEntity entity) {
        return Restaurant.create(
                entity.getId(),
                entity.getName(),
                entity.getAddress(),
                entity.getCuisineType()
        );
    }
}

