package br.com.fiap.techchallenge.paymentservice.infrastructure.adapters.external;

import br.com.fiap.techchallenge.paymentservice.application.dtos.ProcessPaymentRequest;
import br.com.fiap.techchallenge.paymentservice.application.exceptions.FallbackException;
import br.com.fiap.techchallenge.paymentservice.application.ports.output.ExternalPaymentProcessorClient;
import br.com.fiap.techchallenge.paymentservice.infrastructure.util.ExternalPaymentProcessorMapper;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class ExternalPaymentProcessorClientImpl implements ExternalPaymentProcessorClient {

    private final WebClient webClient;
    private final String paymentUrl;

    public ExternalPaymentProcessorClientImpl(
            WebClient.Builder webClientBuilder,
            @Value("${payment.external.url}") String paymentUrl) {
        this.paymentUrl = paymentUrl;
        this.webClient = webClientBuilder.baseUrl(paymentUrl).build();
    }

    @Override
    @TimeLimiter(name = "externalPaymentProcessor")
    @CircuitBreaker(name = "externalPaymentProcessor", fallbackMethod = "fallbackProcessPayment")
    @Retry(name = "externalPaymentProcessor")
    public CompletableFuture<Void> processPayment(ProcessPaymentRequest paymentRequest) {
        return CompletableFuture.runAsync(() -> {
            Map<String, Object> body = ExternalPaymentProcessorMapper.toRequestBody(paymentRequest);

            log.info("Calling external URL to process payment: {}", paymentUrl);

            ResponseEntity<String> responseEntity = webClient.post()
                    .bodyValue(body)
                    .exchangeToMono(response -> {
                        log.info("External payment service response HTTP status: {}", response.statusCode());
                        return response.toEntity(String.class);
                    })
                    .block();

            if (responseEntity != null && responseEntity.getStatusCode().is2xxSuccessful()) {
                log.info("Payment processed successfully!");
                return;
            }

            throw new RuntimeException("Payment processing failed");
        });
    }

    public CompletableFuture<Void> fallbackProcessPayment(ProcessPaymentRequest paymentRequest, Throwable throwable) {
        log.error("Fallback triggered for payment {}: {}", paymentRequest.paymentId(), throwable.getMessage());
        return CompletableFuture.failedFuture(new FallbackException(throwable.getMessage()));
    }
}
