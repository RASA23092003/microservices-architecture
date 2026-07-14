package com.infy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource(value = { "classpath:messages.properties" })
@EnableCaching
public class OrderMSApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderMSApplication.class, args);
	}

}
