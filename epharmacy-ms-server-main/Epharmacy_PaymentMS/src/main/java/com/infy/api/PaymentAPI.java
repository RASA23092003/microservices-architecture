package com.infy.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infy.dto.CardDTO;
import com.infy.dto.PaymentDTO;
import com.infy.exception.EPharmacyException;
import com.infy.service.PaymentService;

@RequestMapping(value = "payment-api")
public class PaymentAPI {
	
	@Autowired
	private PaymentService paymentService;


	public ResponseEntity<Integer> makePayment(CardDTO cardDTO,Float amountToPay) throws Exception {
		//write logic here
		return null;
	}

	@GetMapping(value = "payment/details/{paymentId}")
	public ResponseEntity<PaymentDTO> getPaymentDetails(@PathVariable Integer paymentId) throws EPharmacyException {
		return new ResponseEntity<PaymentDTO>(paymentService.getPaymentDetails(paymentId), HttpStatus.OK);
	}
	

	public ResponseEntity<CardDTO> getCardDetails(String cardId) throws EPharmacyException {
		//write logic here
		return null;
	}
	
	public ResponseEntity<List<CardDTO>> viewCards(Integer customerId) throws EPharmacyException {
		//write logic here
		return null;
	}

	public ResponseEntity<String> addCardForPayment(CardDTO cDTO)throws Exception {
		//write logic here
		return null;
	}
	
	public ResponseEntity<String> deleteCard(String cardId) throws EPharmacyException{
		//write logic here
		return null;
	}
}
