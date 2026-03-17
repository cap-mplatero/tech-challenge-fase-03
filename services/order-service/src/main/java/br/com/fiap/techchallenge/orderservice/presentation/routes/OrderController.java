package br.com.fiap.techchallenge.orderservice.presentation.routes;

import br.com.fiap.techchallenge.orderservice.application.dtos.OrderDTO;
import br.com.fiap.techchallenge.orderservice.application.usecases.OrderUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderUseCase orderUseCase;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@RequestBody @Valid OrderDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderUseCase.createOrder(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderUseCase.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.ok(orderUseCase.getAllOrders());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomerId(@PathVariable Long customerId) {
        return ResponseEntity.ok(orderUseCase.getOrdersByCustomerId(customerId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable String status) {
        return ResponseEntity.ok(orderUseCase.getOrdersByStatus(status));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByRestaurantId(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(orderUseCase.getOrdersByRestaurantId(restaurantId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody @Valid OrderDTO dto) {
        return ResponseEntity.ok(orderUseCase.updateOrder(id, dto));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        String status = body.get("status");
        if (status == null || status.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(orderUseCase.updateOrderStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderUseCase.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}
