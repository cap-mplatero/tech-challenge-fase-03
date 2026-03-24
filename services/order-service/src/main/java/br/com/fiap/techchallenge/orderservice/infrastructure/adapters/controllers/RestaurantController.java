package br.com.fiap.techchallenge.orderservice.infrastructure.adapters.controllers;

import br.com.fiap.techchallenge.orderservice.application.dtos.RestaurantDTO;
import br.com.fiap.techchallenge.orderservice.application.usecases.RestaurantUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantUseCase restaurantUseCase;

    @PostMapping
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody @Valid RestaurantDTO dto, Authentication auth) {
        String ownerId = (String) auth.getPrincipal();
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantUseCase.createRestaurant(dto, ownerId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantUseCase.getRestaurantById(id));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants(Authentication auth) {
        String ownerId = (String) auth.getPrincipal();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            return ResponseEntity.ok(restaurantUseCase.getAllRestaurants());
        }
        return ResponseEntity.ok(restaurantUseCase.getRestaurantsByOwnerId(ownerId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Long id, @RequestBody @Valid RestaurantDTO dto, Authentication auth) {
        String ownerId = (String) auth.getPrincipal();
        return ResponseEntity.ok(restaurantUseCase.updateRestaurant(id, dto, ownerId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id, Authentication auth) {
        String ownerId = (String) auth.getPrincipal();
        restaurantUseCase.deleteRestaurant(id, ownerId);
        return ResponseEntity.noContent().build();
    }
}
