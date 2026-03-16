package br.com.fiap.techchallenge.userservice.infrastructure.adapters.messaging;

import br.com.fiap.techchallenge.userservice.application.ports.output.UserEventPublisherPort;
import br.com.fiap.techchallenge.userservice.domain.entities.User;

import org.springframework.kafka.core.KafkaTemplate;

public class KafkaUserProducer implements UserEventPublisherPort {

    private final KafkaTemplate<String,Object> kafkaTemplate;

    public KafkaUserProducer(KafkaTemplate<String,Object> kafkaTemplate){

        this.kafkaTemplate=kafkaTemplate;

    }

    @Override
    public void publishUserCreated(User user){

        kafkaTemplate.send("user-created",user);

    }

}