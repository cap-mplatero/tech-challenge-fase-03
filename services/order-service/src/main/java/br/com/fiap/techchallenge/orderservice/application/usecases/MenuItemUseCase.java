package br.com.fiap.techchallenge.orderservice.application.usecases;

import br.com.fiap.techchallenge.orderservice.application.dtos.MenuItemDTO;
import br.com.fiap.techchallenge.orderservice.application.ports.output.MenuItemRepository;
import br.com.fiap.techchallenge.orderservice.application.ports.output.RestaurantRepository;
import br.com.fiap.techchallenge.orderservice.domain.entities.MenuItem;
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

    public MenuItemDTO createMenuItem(MenuItemDTO dto) {
        if (!restaurantRepository.existsById(dto.getRestaurantId())) {
            throw new EntityNotFoundException("Restaurant not found with id: " + dto.getRestaurantId());
        }

        MenuItem menuItem = MenuItem.create(dto.getName(), dto.getPrice(), dto.getRestaurantId());
        MenuItem saved = menuItemRepository.save(menuItem);
        return toDTO(saved);
    }

    public MenuItemDTO getMenuItemById(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item not found with id: " + id));
        return toDTO(menuItem);
    }

    public List<MenuItemDTO> getAllMenuItems() {
        return menuItemRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<MenuItemDTO> getMenuItemsByRestaurantId(Long restaurantId) {
        if (!restaurantRepository.existsById(restaurantId)) {
            throw new EntityNotFoundException("Restaurant not found with id: " + restaurantId);
        }

        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public MenuItemDTO updateMenuItem(Long id, MenuItemDTO dto) {
        if (!menuItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Menu item not found with id: " + id);
        }
        if (!restaurantRepository.existsById(dto.getRestaurantId())) {
            throw new EntityNotFoundException("Restaurant not found with id: " + dto.getRestaurantId());
        }

        MenuItem menuItem = MenuItem.create(id, dto.getName(), dto.getPrice(), dto.getRestaurantId());
        MenuItem updated = menuItemRepository.update(menuItem);
        return toDTO(updated);
    }

    public void deleteMenuItem(Long id) {
        if (!menuItemRepository.existsById(id)) {
            throw new EntityNotFoundException("Menu item not found with id: " + id);
        }
        menuItemRepository.deleteById(id);
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

