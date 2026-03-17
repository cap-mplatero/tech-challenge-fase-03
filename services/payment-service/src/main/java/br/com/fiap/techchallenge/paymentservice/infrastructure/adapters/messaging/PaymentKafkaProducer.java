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

    private final KafkaTemplate<String, PaymentResultEvent> kafkaTemplate;

    @Override
    public void publishPaymentApproved(PaymentResultEvent event) {
        log.info("Publishing pagamento.aprovado for orderId={}", event.orderId());
        kafkaTemplate.send("pagamento.aprovado", String.valueOf(event.orderId()), event);
    }

    @Override
    public void publishPaymentPending(PaymentResultEvent event) {
        log.info("Publishing pagamento.pendente for orderId={}", event.orderId());
        kafkaTemplate.send("pagamento.pendente", String.valueOf(event.orderId()), event);
    }
}
