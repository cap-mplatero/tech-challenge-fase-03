package br.com.fiap.techchallenge.paymentservice.infrastructure.util;

import br.com.fiap.techchallenge.paymentservice.application.dtos.ProcessPaymentRequest;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ExternalPaymentProcessorMapper {
    private static String STRING_VALUE = "\"%s\"";

    public static Map<String, Object> toRequestBody(ProcessPaymentRequest paymentRequest) {
       return Map.of(
                "valor", paymentRequest.amount(),
                "pagamento_id", STRING_VALUE.formatted(paymentRequest.paymentId()),
                "cliente_id", STRING_VALUE.formatted(paymentRequest.orderId())
        );
    }
}
