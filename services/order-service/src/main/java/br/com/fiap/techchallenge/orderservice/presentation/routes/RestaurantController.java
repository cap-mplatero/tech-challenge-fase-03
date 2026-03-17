package br.com.fiap.techchallenge.orderservice.presentation.routes;

import br.com.fiap.techchallenge.orderservice.application.dtos.RestaurantDTO;
import br.com.fiap.techchallenge.orderservice.application.usecases.RestaurantUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantUseCase restaurantUseCase;

    @PostMapping
    public ResponseEntity<RestaurantDTO> createRestaurant(@RequestBody @Valid RestaurantDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantUseCase.createRestaurant(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(restaurantUseCase.getRestaurantById(id));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> getAllRestaurants() {
        return ResponseEntity.ok(restaurantUseCase.getAllRestaurants());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> updateRestaurant(@PathVariable Long id, @RequestBody @Valid RestaurantDTO dto) {
        return ResponseEntity.ok(restaurantUseCase.updateRestaurant(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(@PathVariable Long id) {
        restaurantUseCase.deleteRestaurant(id);
        return ResponseEntity.noContent().build();
    }
}
