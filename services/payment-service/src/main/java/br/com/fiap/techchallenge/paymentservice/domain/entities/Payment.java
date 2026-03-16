package br.com.fiap.techchallenge.paymentservice.domain.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Payment {
    private String id;
    private String orderId;
    private String amount;

    public static Payment create(String id, String orderId, String amount) {
        validatePayment(orderId, amount);

        Payment payment = new Payment();
        payment.setId(id);
        payment.setOrderId(orderId);
        payment.setAmount(amount);

        return payment;
    }

    private static void validatePayment(String orderId, String amount) {
        if (orderId == null || orderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Order ID is required");
        }
        if (amount == null || amount.trim().isEmpty()) {
            throw new IllegalArgumentException("Amount is required");
        }
    }
}
