package br.com.fiap.techchallenge.orderservice.application.usecases;

import br.com.fiap.techchallenge.orderservice.application.dtos.MenuItemDTO;
import br.com.fiap.techchallenge.orderservice.application.ports.output.MenuItemRepository;
import br.com.fiap.techchallenge.orderservice.application.ports.output.RestaurantRepository;
import br.com.fiap.techchallenge.orderservice.domain.entities.MenuItem;
import br.com.fiap.techchallenge.orderservice.domain.entities.Restaurant;
import br.com.fiap.techchallenge.orderservice.domain.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MenuItemUseCase {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final RestaurantUseCase restaurantUseCase;

    public MenuItemDTO createMenuItem(MenuItemDTO dto, String ownerId) {
        Restaurant restaurant = findRestaurantOrThrow(dto.getRestaurantId());
        restaurantUseCase.validateOwnership(restaurant, ownerId);
        MenuItem menuItem = MenuItem.create(dto.getName(), dto.getPrice(), dto.getRestaurantId());
        MenuItem saved = menuItemRepository.save(menuItem);
        return toDTO(saved);
    }

    public MenuItemDTO getMenuItemById(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found with id: " + id));
        return toDTO(menuItem);
    }

    public List<MenuItemDTO> getAllMenuItems() {
        return menuItemRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MenuItemDTO> getMenuItemsByRestaurantId(Long restaurantId) {
        findRestaurantOrThrow(restaurantId);
        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MenuItemDTO updateMenuItem(Long id, MenuItemDTO dto, String ownerId) {
        if (!menuItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Menu item not found with id: " + id);
        }
        Restaurant restaurant = findRestaurantOrThrow(dto.getRestaurantId());
        restaurantUseCase.validateOwnership(restaurant, ownerId);
        MenuItem menuItem = MenuItem.create(id, dto.getName(), dto.getPrice(), dto.getRestaurantId());
        MenuItem updated = menuItemRepository.update(menuItem);
        return toDTO(updated);
    }

    public void deleteMenuItem(Long id, String ownerId) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Menu item not found with id: " + id));
        Restaurant restaurant = findRestaurantOrThrow(menuItem.getRestaurantId());
        restaurantUseCase.validateOwnership(restaurant, ownerId);
        menuItemRepository.deleteById(id);
    }

    private Restaurant findRestaurantOrThrow(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new EntityNotFoundException("Restaurant not found with id: " + restaurantId));
    }

    private MenuItemDTO toDTO(MenuItem menuItem) {
        return MenuItemDTO.builder()
                .id(menuItem.getId())
                .restaurantId(menuItem.getRestaurantId())
                .name(menuItem.getName())
                .price(menuItem.getPrice())
                .build();
    }
}
