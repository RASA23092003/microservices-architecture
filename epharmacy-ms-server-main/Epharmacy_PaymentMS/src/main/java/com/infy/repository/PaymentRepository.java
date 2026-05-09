package com.infy.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infy.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment,Integer> {
	//Write appropriate methods if required
}
