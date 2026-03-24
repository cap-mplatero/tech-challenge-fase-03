package br.com.fiap.techchallenge.orderservice.domain.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
public class Restaurant {
    private Long id;
    private String name;
    private String address;
    private String cuisineType;
    private String ownerId;

    public static Restaurant create(String name, String address, String cuisineType) {
        return Restaurant.create(null, name, address, cuisineType, null);
    }

    public static Restaurant create(Long id, String name, String address, String cuisineType) {
        return Restaurant.create(id, name, address, cuisineType, null);
    }

    public static Restaurant create(String name, String address, String cuisineType, String ownerId) {
        return Restaurant.create(null, name, address, cuisineType, ownerId);
    }

    public static Restaurant create(Long id, String name, String address, String cuisineType, String ownerId) {
        validateRestaurant(name, address, cuisineType);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setCuisineType(cuisineType);
        restaurant.setOwnerId(ownerId);

        return restaurant;
    }

    private static void validateRestaurant(String name, String address, String cuisineType) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Restaurant name is required");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Restaurant name must have at least 3 characters");
        }
        if (address == null) {
            throw new IllegalArgumentException("Address is required");
        }
        if (cuisineType == null || cuisineType.trim().isEmpty()) {
            throw new IllegalArgumentException("Cuisine type is required");
        }
    }

}
