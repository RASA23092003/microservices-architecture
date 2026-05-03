package com.infy.message.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.infy.message.dto.PaymentDTO;

@Service
public class PaymentConsumer {

    @KafkaListener(topics="payment-events",groupId = "message-group")
    public void consume(PaymentDTO event) {
        System.out.println("Received event: " + event);

        // Example: send email/SMS
    }
}
