package br.com.fiap.techchallenge.paymentservice.application.usecases;

import br.com.fiap.techchallenge.paymentservice.application.dtos.ProcessPaymentRequest;
import br.com.fiap.techchallenge.paymentservice.application.ports.output.ExternalPaymentProcessorClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessPaymentUseCase {

    private final ExternalPaymentProcessorClient externalPaymentProcessorClient;

    public void execute(ProcessPaymentRequest paymentRequest) {
        externalPaymentProcessorClient.processPayment(paymentRequest);
    }
}
