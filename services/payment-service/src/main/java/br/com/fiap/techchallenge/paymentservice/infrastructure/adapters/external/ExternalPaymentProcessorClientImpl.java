package br.com.fiap.techchallenge.paymentservice.infrastructure.adapters.external;

import br.com.fiap.techchallenge.paymentservice.application.dtos.ProcessPaymentRequest;
import br.com.fiap.techchallenge.paymentservice.application.ports.output.ExternalPaymentProcessorClient;
import br.com.fiap.techchallenge.paymentservice.infrastructure.util.ExternalPaymentProcessorMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Component
public class ExternalPaymentProcessorClientImpl implements ExternalPaymentProcessorClient {

    //TODO Mover pra uma variável de ambiente??
    private static final String PAYMENT_URL = "http://localhost:8089/requisicao";

    private final WebClient webClient;

    public ExternalPaymentProcessorClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(PAYMENT_URL).build();
    }

    @Override
    public void processPayment(ProcessPaymentRequest paymentRequest) {
        Map<String, Object> body = ExternalPaymentProcessorMapper.toRequestBody(paymentRequest);

        System.out.println("=== EXTERNAL PAYMENT REQUEST ===");
        System.out.println("URL: " + PAYMENT_URL);
        System.out.println("Request Body: " + body);

        ResponseEntity<String> responseEntity = webClient.post()
                .bodyValue(body)
                .exchangeToMono(response -> {
                    System.out.println("=== EXTERNAL PAYMENT RESPONSE ===");
                    System.out.println("HTTP Status: " + response.statusCode());
                    System.out.println("Headers: " + response.headers().asHttpHeaders());
                    return response.toEntity(String.class);
                })
                .block();

        if (responseEntity != null) {
            System.out.println("Response Body: " + responseEntity.getBody());
            System.out.println("Is Successful: " + responseEntity.getStatusCode().is2xxSuccessful());
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("Payment processing failed with status: " + responseEntity.getStatusCode());
            }
            return;
        }

        throw new RuntimeException("Payment processing failed: response entity is null");
    }
}
