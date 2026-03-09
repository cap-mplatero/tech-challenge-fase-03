package br.com.fiap.techchallenge.orderservice.infrastructure.adapters.repositories;

import br.com.fiap.techchallenge.orderservice.application.ports.output.CustomerRepository;
import br.com.fiap.techchallenge.orderservice.domain.entities.Customer;
import br.com.fiap.techchallenge.orderservice.infrastructure.database.entities.CustomerEntity;
import br.com.fiap.techchallenge.orderservice.infrastructure.database.repositories.CustomerJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerJpaRepository customerJpaRepository;

    @Override
    public Customer save(Customer customer) {
        CustomerEntity entity = toEntity(customer);
        CustomerEntity saved = customerJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Customer> findAll() {
        return customerJpaRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Customer update(Customer customer) {
        CustomerEntity entity = toEntity(customer);
        CustomerEntity updated = customerJpaRepository.save(entity);
        return toDomain(updated);
    }

    @Override
    public void deleteById(Long id) {
        customerJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return customerJpaRepository.existsById(id);
    }

    private CustomerEntity toEntity(Customer customer) {
        return CustomerEntity.builder()
                .id(customer.getId())
                .build();
    }

    private Customer toDomain(CustomerEntity entity) {
        return Customer.create(entity.getId());
    }
}

