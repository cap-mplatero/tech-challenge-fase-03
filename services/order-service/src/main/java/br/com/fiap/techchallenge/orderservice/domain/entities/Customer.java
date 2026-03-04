package br.com.fiap.techchallenge.orderservice.domain.entities;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PUBLIC)
@NoArgsConstructor
public class Customer {

    private Long id;

    public static Customer create() {
        return Customer.create(null);
    }

    public static Customer create(Long id) {
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }

}
