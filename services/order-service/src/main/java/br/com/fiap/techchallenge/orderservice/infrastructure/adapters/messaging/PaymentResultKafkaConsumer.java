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

    @KafkaListener(topics = "pagamento.aprovado", groupId = "order-service-group")
    public void onPaymentApproved(PaymentResultEvent event) {
        log.info("Received pagamento.aprovado for orderId={}", event.orderId());
        orderUseCase.updateOrderStatus(event.orderId(), "PAGO");
        log.info("Order {} updated to PAGO", event.orderId());
    }

    @KafkaListener(topics = "pagamento.pendente", groupId = "order-service-group")
    public void onPaymentPending(PaymentResultEvent event) {
        log.info("Received pagamento.pendente for orderId={}", event.orderId());
        orderUseCase.updateOrderStatus(event.orderId(), "PENDENTE_PAGAMENTO");
        log.info("Order {} updated to PENDENTE_PAGAMENTO", event.orderId());
    }
}
