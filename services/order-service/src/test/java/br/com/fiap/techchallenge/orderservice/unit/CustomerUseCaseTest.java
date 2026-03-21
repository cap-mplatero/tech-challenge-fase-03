package br.com.fiap.techchallenge.orderservice.unit;

import br.com.fiap.techchallenge.orderservice.application.dtos.CustomerDTO;
import br.com.fiap.techchallenge.orderservice.application.ports.output.CustomerRepository;
import br.com.fiap.techchallenge.orderservice.application.usecases.CustomerUseCase;
import br.com.fiap.techchallenge.orderservice.domain.entities.Customer;
import br.com.fiap.techchallenge.orderservice.domain.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerUseCaseTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerUseCase customerUseCase;

    @Nested
    @DisplayName("createCustomer")
    class CreateCustomer {

        @Test
        @DisplayName("Should create a customer successfully")
        void shouldCreateCustomerSuccessfully() {
            Customer saved = Customer.create(1L, "John Doe", "123 Main St");
            when(customerRepository.save(any(Customer.class))).thenReturn(saved);

            CustomerDTO input = CustomerDTO.builder().name("John Doe").address("123 Main St").build();
            CustomerDTO result = customerUseCase.createCustomer(input);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("John Doe", result.getName());
            assertEquals("123 Main St", result.getAddress());
            verify(customerRepository).save(any(Customer.class));
        }
    }

    @Nested
    @DisplayName("getCustomerById")
    class GetCustomerById {

        @Test
        @DisplayName("Should return customer when found")
        void shouldReturnCustomerWhenFound() {
            Customer customer = Customer.create(1L, "John Doe", "123 Main St");
            when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

            CustomerDTO result = customerUseCase.getCustomerById(1L);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("John Doe", result.getName());
            assertEquals("123 Main St", result.getAddress());
            verify(customerRepository).findById(1L);
        }

        @Test
        @DisplayName("Should throw exception when customer not found")
        void shouldThrowExceptionWhenCustomerNotFound() {
            when(customerRepository.findById(99L)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> customerUseCase.getCustomerById(99L));

            assertEquals("Customer not found with id: 99", exception.getMessage());
            verify(customerRepository).findById(99L);
        }
    }

    @Nested
    @DisplayName("getAllCustomers")
    class GetAllCustomers {

        @Test
        @DisplayName("Should return all customers")
        void shouldReturnAllCustomers() {
            List<Customer> customers = List.of(
                    Customer.create(1L, "John Doe", "123 Main St"),
                    Customer.create(2L, "Jane Doe", "456 Oak Ave")
            );
            when(customerRepository.findAll()).thenReturn(customers);

            List<CustomerDTO> result = customerUseCase.getAllCustomers();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(1L, result.get(0).getId());
            assertEquals("John Doe", result.get(0).getName());
            assertEquals(2L, result.get(1).getId());
            assertEquals("Jane Doe", result.get(1).getName());
            verify(customerRepository).findAll();
        }

        @Test
        @DisplayName("Should return empty list when no customers")
        void shouldReturnEmptyListWhenNoCustomers() {
            when(customerRepository.findAll()).thenReturn(Collections.emptyList());

            List<CustomerDTO> result = customerUseCase.getAllCustomers();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(customerRepository).findAll();
        }
    }

    @Nested
    @DisplayName("updateCustomer")
    class UpdateCustomer {

        @Test
        @DisplayName("Should update customer successfully")
        void shouldUpdateCustomerSuccessfully() {
            Customer existing = Customer.create(1L, "John Doe", "123 Main St");
            Customer updated = Customer.create(1L, "John Updated", "789 New Ave");
            when(customerRepository.findById(1L)).thenReturn(Optional.of(existing));
            when(customerRepository.update(any(Customer.class))).thenReturn(updated);

            CustomerDTO input = CustomerDTO.builder().name("John Updated").address("789 New Ave").build();
            CustomerDTO result = customerUseCase.updateCustomer(1L, input);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("John Updated", result.getName());
            assertEquals("789 New Ave", result.getAddress());
            verify(customerRepository).findById(1L);
            verify(customerRepository).update(any(Customer.class));
        }

        @Test
        @DisplayName("Should throw exception when customer not found for update")
        void shouldThrowExceptionWhenCustomerNotFoundForUpdate() {
            when(customerRepository.findById(99L)).thenReturn(Optional.empty());

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> customerUseCase.updateCustomer(99L, new CustomerDTO()));

            assertEquals("Customer not found with id: 99", exception.getMessage());
            verify(customerRepository).findById(99L);
            verify(customerRepository, never()).update(any());
        }
    }

    @Nested
    @DisplayName("deleteCustomer")
    class DeleteCustomer {

        @Test
        @DisplayName("Should delete customer successfully")
        void shouldDeleteCustomerSuccessfully() {
            when(customerRepository.existsById(1L)).thenReturn(true);
            doNothing().when(customerRepository).deleteById(1L);

            assertDoesNotThrow(() -> customerUseCase.deleteCustomer(1L));

            verify(customerRepository).existsById(1L);
            verify(customerRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Should throw exception when customer not found for delete")
        void shouldThrowExceptionWhenCustomerNotFoundForDelete() {
            when(customerRepository.existsById(99L)).thenReturn(false);

            EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                    () -> customerUseCase.deleteCustomer(99L));

            assertEquals("Customer not found with id: 99", exception.getMessage());
            verify(customerRepository).existsById(99L);
            verify(customerRepository, never()).deleteById(any());
        }
    }
}

