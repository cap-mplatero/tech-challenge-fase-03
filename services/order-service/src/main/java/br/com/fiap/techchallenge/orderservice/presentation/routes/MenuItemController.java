package br.com.fiap.techchallenge.orderservice.presentation.routes;

import br.com.fiap.techchallenge.orderservice.application.dtos.MenuItemDTO;
import br.com.fiap.techchallenge.orderservice.application.usecases.MenuItemUseCase;
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
    public ResponseEntity<MenuItemDTO> createMenuItem(@RequestBody MenuItemDTO dto) {
        MenuItemDTO created = menuItemUseCase.createMenuItem(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> getMenuItemById(@PathVariable Long id) {
        MenuItemDTO menuItem = menuItemUseCase.getMenuItemById(id);
        return ResponseEntity.ok(menuItem);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemDTO>> getAllMenuItems() {
        List<MenuItemDTO> menuItems = menuItemUseCase.getAllMenuItems();
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemDTO>> getMenuItemsByRestaurantId(@PathVariable Long restaurantId) {
        List<MenuItemDTO> menuItems = menuItemUseCase.getMenuItemsByRestaurantId(restaurantId);
        return ResponseEntity.ok(menuItems);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemDTO dto) {
        MenuItemDTO updated = menuItemUseCase.updateMenuItem(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemUseCase.deleteMenuItem(id);
        return ResponseEntity.noContent().build();
    }
}

