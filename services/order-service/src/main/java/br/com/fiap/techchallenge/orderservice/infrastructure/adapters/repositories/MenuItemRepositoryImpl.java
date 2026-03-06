package br.com.fiap.techchallenge.orderservice.infrastructure.adapters.repositories;

import br.com.fiap.techchallenge.orderservice.domain.entities.MenuItem;
import br.com.fiap.techchallenge.orderservice.domain.repositories.MenuItemRepository;
import br.com.fiap.techchallenge.orderservice.infrastructure.database.entities.MenuItemEntity;
import br.com.fiap.techchallenge.orderservice.infrastructure.database.entities.RestaurantEntity;
import br.com.fiap.techchallenge.orderservice.infrastructure.database.repositories.MenuItemJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MenuItemRepositoryImpl implements MenuItemRepository {

    private final MenuItemJpaRepository menuItemJpaRepository;

    @Override
    public MenuItem save(MenuItem menuItem) {
        MenuItemEntity entity = toEntity(menuItem);
        MenuItemEntity saved = menuItemJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<MenuItem> findById(Long id) {
        return menuItemJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public void deleteById(Long id) {
        menuItemJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return menuItemJpaRepository.existsById(id);
    }

    private MenuItemEntity toEntity(MenuItem menuItem) {
        RestaurantEntity restaurantRef = new RestaurantEntity();
        restaurantRef.setId(menuItem.getRestaurantId());

        return MenuItemEntity.builder()
                .id(menuItem.getId())
                .name(menuItem.getName())
                .quantity(menuItem.getQuantity())
                .price(menuItem.getPrice())
                .restaurant(restaurantRef)
                .build();
    }

    private MenuItem toDomain(MenuItemEntity entity) {
        return MenuItem.create(
                entity.getId(),
                entity.getName(),
                entity.getQuantity(),
                entity.getPrice(),
                entity.getRestaurant().getId()
        );
    }
}

