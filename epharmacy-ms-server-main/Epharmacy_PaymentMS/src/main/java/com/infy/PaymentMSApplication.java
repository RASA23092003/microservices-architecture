package com.infy;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@PropertySource(value = { "classpath:messages.properties" })
public class PaymentMSApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentMSApplication.class, args);
	}

	@Bean
	public NewTopic paymentTopic() {
		return TopicBuilder.name("payment-topic").build();
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
