package com.infy.message.service;

import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import com.infy.message.dto.CardDTO;
import com.infy.message.dto.CardType;
import com.infy.message.dto.PaymentDTO;
import com.infy.message.exception.EPharmacyException;

@Service
public class PaymentConsumer {

    //@KafkaListener(topics="payment-event",groupId = "payment-group")
    public void consume(PaymentDTO event) {
        System.out.println("Received event: " + event);

        // Example: send email/SMS
    }

    @RetryableTopic(attempts = "3", backoff = @Backoff(delay = 2000))
    @KafkaListener(topics="card-event",groupId = "card-group")
    public void consumeCardDetails(CardDTO event) throws EPharmacyException {
        System.out.println("Received card details: " + event);
        if(event.getCardType() != CardType.CREDIT_CARD) {
            throw new EPharmacyException("Only credit cards are allowed");
        }
        System.out.println("Card details processed successfully: " + event);
    }

    @DltHandler
    public void handleCardProcessingFailure(CardDTO event, Throwable ex) {
        System.out.println("Failed to process card details after retries: " + event);
        // Log the failure or take alternative actions
    }
}
