package br.com.fiap.techchallenge.paymentservice.infrastructure.adapters.messaging;

import br.com.fiap.techchallenge.paymentservice.application.dtos.PaymentResultEvent;
import br.com.fiap.techchallenge.paymentservice.application.ports.output.PaymentEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentKafkaProducer implements PaymentEventPublisher {

    private static final String TOPIC = "payment-result";

    private final KafkaTemplate<String, PaymentResultEvent> kafkaTemplate;

    @Override
    public void publishPaymentResult(PaymentResultEvent event) {
        log.info("Publishing payment-result for orderId={} status={}", event.orderId(), event.status());
        kafkaTemplate.send(TOPIC, String.valueOf(event.orderId()), event);
    }
}
