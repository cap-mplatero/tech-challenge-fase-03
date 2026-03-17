package br.com.fiap.techchallenge.paymentservice.infrastructure.adapters.external;

import br.com.fiap.techchallenge.paymentservice.application.dtos.ProcessPaymentRequest;
import br.com.fiap.techchallenge.paymentservice.application.ports.output.ExternalPaymentProcessorClient;
import br.com.fiap.techchallenge.paymentservice.infrastructure.util.ExternalPaymentProcessorMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

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
    public void processPayment(ProcessPaymentRequest paymentRequest) {
        Map<String, Object> body = ExternalPaymentProcessorMapper.toRequestBody(paymentRequest);

        log.info("=== EXTERNAL PAYMENT REQUEST ===");
        log.info("URL: {}", paymentUrl);
        log.info("Request Body: {}", body);

        ResponseEntity<String> responseEntity = webClient.post()
                .bodyValue(body)
                .exchangeToMono(response -> {
                    log.info("=== EXTERNAL PAYMENT RESPONSE ===");
                    log.info("HTTP Status: {}", response.statusCode());
                    log.info("Headers: {}", response.headers().asHttpHeaders());
                    return response.toEntity(String.class);
                })
                .block();

        if (responseEntity != null) {
            log.info("Response Body: {}", responseEntity.getBody());
            log.info("Is Successful: {}", responseEntity.getStatusCode().is2xxSuccessful());
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Payment processing failed with status: " + responseEntity.getStatusCode());
            }
            return;
        }

        throw new RuntimeException("Payment processing failed: response entity is null");
    }
}
