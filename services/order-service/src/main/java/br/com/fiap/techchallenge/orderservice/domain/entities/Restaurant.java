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

    public static Restaurant create(String name, String address, String cuisineType) {
        return Restaurant.create(null, name, address, cuisineType);
    }

    public static Restaurant create(Long id, String name, String address, String cuisineType) {
        validateRestaurant(name, address, cuisineType);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(id);
        restaurant.setName(name);
        restaurant.setAddress(address);
        restaurant.setCuisineType(cuisineType);

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
