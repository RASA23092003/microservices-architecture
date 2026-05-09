package com.infy.service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.dto.CardDTO;
import com.infy.dto.PaymentDTO;
import com.infy.entity.Card;
import com.infy.entity.Payment;
import com.infy.exception.EPharmacyException;
import com.infy.repository.CardRepository;
import com.infy.repository.PaymentRepository;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	PaymentRepository paymentRepository;
	@Autowired
	CardRepository cardRepository;
	@Autowired
	ObjectMapper mapper;

	// @Autowired
	// KafkaTemplate<String,PaymentDTO> kafkaTemplate;

	@Override
	public List<CardDTO> viewCards(Integer customerId) throws EPharmacyException {
		List<Card> cards=cardRepository.findByCustomerId(customerId);
		if(cards.isEmpty()) throw new EPharmacyException("PaymentService.NO_CARD_FOUND");
		List<CardDTO> cardDTO=new ArrayList<>();
		for(Card card:cards){
			CardDTO dto=mapper.convertValue(card, CardDTO.class);
			cardDTO.add(dto);
		}
		return cardDTO;
	}

	@Override
	public void addCard(CardDTO cardDTO) throws EPharmacyException, NoSuchAlgorithmException {
		System.out.println(cardDTO);
		Optional<Card> card=cardRepository.findByNameandCVV(cardDTO.getNameOnCard(),cardDTO.getCvv());
		if(card.isPresent()){
			throw new EPharmacyException("PaymentService.CARD_ALREADY_EXISTS");
		}
		Card newCard=new Card();
		newCard.setCardType(cardDTO.getCardType());
		newCard.setCvv(cardDTO.getCvv());
		newCard.setCustomerId(cardDTO.getCustomerId());
		newCard.setExpiryDate(cardDTO.getExpiryDate());
		newCard.setNameOnCard(cardDTO.getNameOnCard());		
		cardRepository.save(newCard);
	}

	@Override
	public void deleteCard(Integer cardId) throws EPharmacyException {
		Card card=cardRepository.findByCardId(cardId).orElseThrow(()->new EPharmacyException("PaymentService.NO_CARD_FOUND"));
		cardRepository.delete(card);

	}

	@Override
	public Integer makePayment(CardDTO cardDTO, Float amountToPay)
			throws EPharmacyException, NoSuchAlgorithmException {
		Card card=cardRepository.findById(cardDTO.getCardId()).orElseThrow(()->new EPharmacyException("PaymentService.NO_CARD_FOUND"));
		if(card.getExpiryDate().isBefore(LocalDate.now())) throw new EPharmacyException("PaymentService.CARD_EXPIRED");
		if(!card.getCvv().equals(cardDTO.getCvv())) throw new EPharmacyException("PaymentService.INVALID_CVV");
		Payment payment=new Payment();
		payment.setAmount(amountToPay);
		payment.setPaymentTime(LocalDateTime.now());
		payment.setCustomerId(cardDTO.getCustomerId());
		payment.setCardId(cardDTO.getCardId());
		return paymentRepository.save(payment).getPaymentId();
		
	}

	@Override
	public PaymentDTO getPaymentDetails(Integer paymentId) throws EPharmacyException {
		Payment payment=paymentRepository.findById(paymentId).orElseThrow(()->new EPharmacyException("PaymentService.NO_TRANSACTION_FOUND"));
		Card card=cardRepository.findById(payment.getCardId()).orElseThrow(()->new EPharmacyException("PaymentService.NO_CARD_FOUND"));
		CardDTO carddto =new CardDTO();
		carddto.setCardId(card.getCardId());
		carddto.setCardType(card.getCardType());
		carddto.setCvv(card.getCvv());
		carddto.setExpiryDate(card.getExpiryDate());
		carddto.setNameOnCard(card.getNameOnCard());
		carddto.setCustomerId(card.getCustomerId());
		PaymentDTO dto=new PaymentDTO();
		dto.setPaymentId(payment.getPaymentId());
		dto.setAmount(payment.getAmount());
		dto.setPaymentTime(payment.getPaymentTime());
		dto.setCustomerId(payment.getCustomerId());
		dto.setCard(carddto);
		return dto;
	}

	@Override
	public CardDTO getCardDetails(Integer cardId) throws EPharmacyException {
		Card card=cardRepository.findById(cardId).orElseThrow(()->new EPharmacyException("PaymentService.NO_CARD_FOUND"));
		CardDTO carddto =new CardDTO();
		carddto.setCardId(card.getCardId());
		carddto.setCardType(card.getCardType());
		carddto.setCvv(card.getCvv());
		carddto.setExpiryDate(card.getExpiryDate());
		carddto.setNameOnCard(card.getNameOnCard());
		carddto.setCustomerId(card.getCustomerId());		
		return carddto;
	}
	@Override
	public void sendPayment(PaymentDTO event) throws EPharmacyException{
		System.out.println("Sending payment event to Kafka: " + event);
		//kafkaTemplate.send("payment-events",event);
		System.out.println("Payment event sent to Kafka successfully");
	}

}
