package com.infy.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.CardDTO;
import com.infy.dto.PaymentDTO;
import com.infy.exception.EPharmacyException;
import com.infy.service.PaymentService;

@RestController
@RequestMapping(value = "/payment-api")
public class PaymentAPI {
	
	@Autowired
	private PaymentService paymentService;
	@Autowired
	Environment environment;

	@Autowired
	KafkaTemplate<String,PaymentDTO> kafkaTemplate;


	@PostMapping("/payment/amount/{amountToPay}")
	public ResponseEntity<String> makePayment(@RequestBody CardDTO cardDTO,@PathVariable Float amountToPay) throws Exception {
		Integer paymentId=paymentService.makePayment(cardDTO, amountToPay);
		String successMsg=environment.getProperty("PaymentAPI.PAYMENT_SUCCESS")+" "+paymentId;
		PaymentDTO event=paymentService.getPaymentDetails(paymentId);
		Message<PaymentDTO> message=MessageBuilder.withPayload(event).setHeader(KafkaHeaders.TOPIC, "payment-event").build();
		kafkaTemplate.send(message);
		//paymentService.sendPayment(event);
		return new ResponseEntity<>(successMsg,HttpStatus.CREATED);
	}

	@GetMapping(value = "/payment/details/{paymentId}")
	public ResponseEntity<PaymentDTO> getPaymentDetails(@PathVariable Integer paymentId) throws EPharmacyException {
		return new ResponseEntity<PaymentDTO>(paymentService.getPaymentDetails(paymentId), HttpStatus.OK);
	}
	

	@GetMapping(value = "/payment/card/{cardId}")
	public ResponseEntity<CardDTO> getCardDetails(@PathVariable Integer cardId) throws EPharmacyException {
		CardDTO card=paymentService.getCardDetails(cardId);
		return new ResponseEntity<>(card,HttpStatus.OK);
	}
    @GetMapping(value = "/payment/view-cards/{customerId}")
	public ResponseEntity<List<CardDTO>> viewCards(@PathVariable Integer customerId) throws EPharmacyException {
		List<CardDTO> cards=paymentService.viewCards(customerId);
		for(CardDTO c:cards) {
			Message<CardDTO> message=MessageBuilder.withPayload(c).setHeader(KafkaHeaders.TOPIC, "card-event").build();
			kafkaTemplate.send(message);
		}
		//kafkaTemplate.send("card-topic", null);
		return new ResponseEntity<>(cards,HttpStatus.OK);
	}

	@PostMapping(value="/payment/add-card")
	public ResponseEntity<String> addCardForPayment(@RequestBody CardDTO cDTO)throws Exception {
		paymentService.addCard(cDTO);
		String successMsg=environment.getProperty("CardAPI.ADD_CARD_SUCCESS");
		return new ResponseEntity<>(successMsg,HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/payment/delete-card/{cardId}")
	public ResponseEntity<String> deleteCard(@PathVariable Integer cardId) throws EPharmacyException{
		String successMsg=environment.getProperty("CardAPI.DELETE_CARD_SUCCESS");
		return new ResponseEntity<>(successMsg,HttpStatus.OK);
	}
}
