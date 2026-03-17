package br.com.fiap.techchallenge.orderservice.infrastructure.adapters.controllers;

import br.com.fiap.techchallenge.orderservice.application.dtos.MenuItemDTO;
import br.com.fiap.techchallenge.orderservice.application.usecases.MenuItemUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemUseCase menuItemUseCase;

    @PostMapping
    public ResponseEntity<MenuItemDTO> createMenuItem(@RequestBody @Valid MenuItemDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemUseCase.createMenuItem(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long id) {
        return ResponseEntity.ok(menuItemUseCase.getMenuItemById(id));
    }

    @GetMapping
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItems() {
        return ResponseEntity.ok(menuItemUseCase.getAllMenuItems());
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByRestaurantId(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuItemUseCase.getMenuItemsByRestaurantId(restaurantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable Long id, @RequestBody @Valid MenuItemDTO dto) {
        return ResponseEntity.ok(menuItemUseCase.updateMenuItem(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemUseCase.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}
