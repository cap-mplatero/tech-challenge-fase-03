package br.com.fiap.techchallenge.orderservice.infrastructure.adapters.messaging;

import br.com.fiap.techchallenge.orderservice.application.dtos.OrderCreatedEvent;
import br.com.fiap.techchallenge.orderservice.application.ports.output.OrderEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderKafkaProducer implements OrderEventPublisher {

    private static final String TOPIC = "pedido.criado";

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    @Override
    public void publishOrderCreated(OrderCreatedEvent event) {
        log.info("Publishing pedido.criado event for orderId={}", event.orderId());
        kafkaTemplate.send(TOPIC, String.valueOf(event.orderId()), event);
    }
}
