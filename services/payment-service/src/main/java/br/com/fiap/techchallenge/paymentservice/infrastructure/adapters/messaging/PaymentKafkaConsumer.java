package br.com.fiap.techchallenge.paymentservice.infrastructure.adapters.messaging;

import br.com.fiap.techchallenge.paymentservice.application.dtos.OrderCreatedEvent;
import br.com.fiap.techchallenge.paymentservice.application.dtos.PaymentResultEvent;
import br.com.fiap.techchallenge.paymentservice.application.dtos.ProcessPaymentRequest;
import br.com.fiap.techchallenge.paymentservice.application.ports.output.PaymentEventPublisher;
import br.com.fiap.techchallenge.paymentservice.application.ports.output.PaymentRepositoryPort;
import br.com.fiap.techchallenge.paymentservice.application.usecases.ProcessPaymentUseCase;
import br.com.fiap.techchallenge.paymentservice.domain.entities.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentKafkaConsumer {

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final PaymentEventPublisher paymentEventPublisher;
    private final PaymentRepositoryPort paymentRepository;

    @KafkaListener(topics = "order-created", groupId = "payment-service-group")
    public void onOrderCreated(OrderCreatedEvent event) {
        log.info("Received order-created event for orderId={}", event.orderId());

        String paymentId = "PAY-" + event.orderId();
        String amount = event.totalAmount().toPlainString();
        String status;

        try {
            processPaymentUseCase.execute(new ProcessPaymentRequest(
                    paymentId, String.valueOf(event.orderId()), event.totalAmount().longValue()
            ));
            status = "APPROVED";
        } catch (Exception e) {
            log.error("Payment failed for orderId={}: {}", event.orderId(), e.getMessage());
            status = "PENDING";
        }

        paymentRepository.save(Payment.create(paymentId, String.valueOf(event.orderId()), amount, status));
        paymentEventPublisher.publishPaymentResult(new PaymentResultEvent(event.orderId(), status));
    }
}
