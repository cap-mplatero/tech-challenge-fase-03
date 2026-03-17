package br.com.fiap.techchallenge.orderservice.application.usecases;

import br.com.fiap.techchallenge.orderservice.application.dtos.RestaurantDTO;
import br.com.fiap.techchallenge.orderservice.application.ports.output.RestaurantRepository;
import br.com.fiap.techchallenge.orderservice.domain.entities.Restaurant;
import br.com.fiap.techchallenge.orderservice.domain.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantUseCase {

    private final RestaurantRepository restaurantRepository;

    public RestaurantDTO createRestaurant(RestaurantDTO dto) {
        Restaurant restaurant = Restaurant.create(dto.getName(), dto.getAddress(), dto.getCuisineType());
        Restaurant saved = restaurantRepository.save(restaurant);
        return toDTO(saved);
    }

    public RestaurantDTO getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));
        return toDTO(restaurant);
    }

    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public RestaurantDTO updateRestaurant(Long id, RestaurantDTO dto) {
        if (!restaurantRepository.existsById(id)) {
            throw new EntityNotFoundException("Restaurant not found with id: " + id);
        }
        Restaurant restaurant = Restaurant.create(id, dto.getName(), dto.getAddress(), dto.getCuisineType());
        Restaurant updated = restaurantRepository.update(restaurant);
        return toDTO(updated);
    }

    public void deleteRestaurant(Long id) {
        if (!restaurantRepository.existsById(id)) {
            throw new EntityNotFoundException("Restaurant not found with id: " + id);
        }
        restaurantRepository.deleteById(id);
    }

    private RestaurantDTO toDTO(Restaurant restaurant) {
        return RestaurantDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .cuisineType(restaurant.getCuisineType())
                .build();
    }
}

