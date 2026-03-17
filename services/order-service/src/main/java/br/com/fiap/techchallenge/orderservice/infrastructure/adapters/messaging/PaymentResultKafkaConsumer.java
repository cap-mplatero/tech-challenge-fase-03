package br.com.fiap.techchallenge.orderservice.infrastructure.adapters.messaging;

import br.com.fiap.techchallenge.orderservice.application.dtos.PaymentResultEvent;
import br.com.fiap.techchallenge.orderservice.application.usecases.OrderUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentResultKafkaConsumer {

    private final OrderUseCase orderUseCase;

    @KafkaListener(topics = "payment-result", groupId = "order-service-group")
    public void onPaymentResult(PaymentResultEvent event) {
        log.info("Received payment-result for orderId={} status={}", event.orderId(), event.status());

        String orderStatus = "APPROVED".equals(event.status()) ? "CONFIRMED" : "PAYMENT_FAILED";
        orderUseCase.updateOrderStatus(event.orderId(), orderStatus);

        log.info("Order {} updated to {}", event.orderId(), orderStatus);
    }
}
