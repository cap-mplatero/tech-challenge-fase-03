package br.com.fiap.techchallenge.paymentservice.infrastructure.adapters.scheduling;

import br.com.fiap.techchallenge.paymentservice.application.dtos.PaymentResultEvent;
import br.com.fiap.techchallenge.paymentservice.application.dtos.ProcessPaymentRequest;
import br.com.fiap.techchallenge.paymentservice.application.ports.output.PaymentEventPublisher;
import br.com.fiap.techchallenge.paymentservice.application.ports.output.PaymentRepositoryPort;
import br.com.fiap.techchallenge.paymentservice.application.usecases.ProcessPaymentUseCase;
import br.com.fiap.techchallenge.paymentservice.domain.entities.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRetryScheduler {

    private final PaymentRepositoryPort paymentRepository;
    private final ProcessPaymentUseCase processPaymentUseCase;
    private final PaymentEventPublisher paymentEventPublisher;

    @Scheduled(fixedDelayString = "${payment.retry.interval-ms:60000}")
    public void retryPendingPayments() {
        List<Payment> pending = paymentRepository.findByStatus("PENDING");
        if (pending.isEmpty()) return;

        log.info("Retrying {} pending payments", pending.size());

        for (Payment payment : pending) {
            try {
                processPaymentUseCase.execute(new ProcessPaymentRequest(
                        payment.getId(), payment.getOrderId(), new BigDecimal(payment.getAmount()).longValue())
                );
                payment.setStatus("APPROVED");
                paymentRepository.save(payment);
                paymentEventPublisher.publishPaymentApproved(
                        new PaymentResultEvent(Long.parseLong(payment.getOrderId()), "APPROVED"));
                log.info("Pending payment {} approved on retry", payment.getId());
            } catch (Exception e) {
                log.error("Retry failed for payment {}", payment.getId(), e);
            }
        }
    }
}
