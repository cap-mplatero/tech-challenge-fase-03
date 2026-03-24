package br.com.fiap.techchallenge.orderservice.application.usecases;

import br.com.fiap.techchallenge.orderservice.application.dtos.RestaurantDTO;
import br.com.fiap.techchallenge.orderservice.application.ports.output.RestaurantRepository;
import br.com.fiap.techchallenge.orderservice.domain.entities.Restaurant;
import br.com.fiap.techchallenge.orderservice.domain.exceptions.AccessDeniedException;
import br.com.fiap.techchallenge.orderservice.domain.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RestaurantUseCase {

    private final RestaurantRepository restaurantRepository;

    public RestaurantDTO createRestaurant(RestaurantDTO dto, String ownerId) {
        Restaurant restaurant = Restaurant.create(dto.getName(), dto.getAddress(), dto.getCuisineType(), ownerId);
        Restaurant saved = restaurantRepository.save(restaurant);
        return toDTO(saved);
    }

    public RestaurantDTO getRestaurantById(Long id) {
        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + id));
        return toDTO(restaurant);
    }

    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<RestaurantDTO> getRestaurantsByOwnerId(String ownerId) {
        return restaurantRepository.findByOwnerId(ownerId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public RestaurantDTO updateRestaurant(Long id, RestaurantDTO dto, String ownerId) {
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + id));
        validateOwnership(existing, ownerId);
        Restaurant restaurant = Restaurant.create(id, dto.getName(), dto.getAddress(), dto.getCuisineType(), existing.getOwnerId());
        Restaurant updated = restaurantRepository.update(restaurant);
        return toDTO(updated);
    }

    public void deleteRestaurant(Long id, String ownerId) {
        Restaurant existing = restaurantRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + id));
        validateOwnership(existing, ownerId);
        restaurantRepository.deleteById(id);
    }

    public void validateOwnership(Restaurant restaurant, String ownerId) {
        if (!restaurant.getOwnerId().equals(ownerId)) {
            throw new AccessDeniedException("You do not have access to this restaurant");
        }
    }

    private RestaurantDTO toDTO(Restaurant restaurant) {
        return RestaurantDTO.builder()
                .id(restaurant.getId())
                .name(restaurant.getName())
                .address(restaurant.getAddress())
                .cuisineType(restaurant.getCuisineType())
                .ownerId(restaurant.getOwnerId())
                .build();
    }
}
