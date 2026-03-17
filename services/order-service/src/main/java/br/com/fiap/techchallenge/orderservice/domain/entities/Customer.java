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
    private String externalUserId;

    public static Customer create() {
        return Customer.create(null, null);
    }

    public static Customer create(Long id) {
        return Customer.create(id, null);
    }

    public static Customer create(Long id, String externalUserId) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setExternalUserId(externalUserId);
        return customer;
    }
}
