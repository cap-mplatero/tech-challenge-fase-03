package br.com.fiap.techchallenge.orderservice.domain.repositories;

import br.com.fiap.techchallenge.orderservice.domain.entities.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(Long id);

    void deleteById(Long id);

    boolean existsById(Long id);
}

