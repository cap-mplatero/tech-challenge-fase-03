package br.com.fiap.techchallenge.paymentservice.application.usecases;

import br.com.fiap.techchallenge.paymentservice.application.dtos.ProcessPaymentRequest;
import br.com.fiap.techchallenge.paymentservice.application.exceptions.FallbackException;
import br.com.fiap.techchallenge.paymentservice.application.ports.output.ExternalPaymentProcessorClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class ProcessPaymentUseCase {

    private final ExternalPaymentProcessorClient externalPaymentProcessorClient;

    public ProcessPaymentUseCase(ExternalPaymentProcessorClient externalPaymentProcessorClient) {
        this.externalPaymentProcessorClient = externalPaymentProcessorClient;
    }

    @CircuitBreaker(name = "externalPaymentProcessor", fallbackMethod = "fallbackProcessPayment")
    @Retry(name = "externalPaymentProcessor")
    public void execute(ProcessPaymentRequest paymentRequest) {
        System.out.println("Start payment processing of request: " + paymentRequest);

        externalPaymentProcessorClient.processPayment(paymentRequest);

        System.out.println("Payment processed successfully!");
    }

    public void fallbackProcessPayment(Throwable throwable) {
        System.err.println("Fallback triggered due to: " + throwable.getMessage());
        throw new FallbackException(throwable.getMessage());
    }
}
