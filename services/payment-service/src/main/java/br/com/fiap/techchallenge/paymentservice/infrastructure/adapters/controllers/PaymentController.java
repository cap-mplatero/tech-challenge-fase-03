package br.com.fiap.techchallenge.paymentservice.infrastructure.adapters.controllers;

import br.com.fiap.techchallenge.paymentservice.application.dtos.ProcessPaymentRequest;
import br.com.fiap.techchallenge.paymentservice.application.usecases.ProcessPaymentUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;

    @PostMapping
    public ResponseEntity<Map<String, Object>> processPayment(@RequestBody ProcessPaymentRequest request) {
        try {
            processPaymentUseCase.execute(request);

            return ResponseEntity.ok(Map.of(
                    "status", "APPROVED",
                    "message", "Payment Processed Successfully",
                    "paymentId", request.paymentId(),
                    "orderId", request.orderId()
            ));
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(Map.of(
                    "status", "REJECTED",
                    "message", "Payment has failed. Try again.",
                    "paymentId", request.paymentId(),
                    "orderId", request.orderId()
            ));
        }
    }
}

