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
    private String name;
    private String address;

    public static Customer create(String name, String address) {
        return Customer.create(null, null, name, address);
    }

    public static Customer create(Long id, String name, String address) {
        return Customer.create(id, null, name, address);
    }

    public static Customer create(Long id, String externalUserId, String name, String address) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setExternalUserId(externalUserId);
        customer.setName(name);
        customer.setAddress(address);
        return customer;
    }
}
