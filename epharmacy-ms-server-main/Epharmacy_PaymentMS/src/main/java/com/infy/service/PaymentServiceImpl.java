package com.infy.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.infy.dto.CardDTO;
import com.infy.dto.PaymentDTO;
import com.infy.exception.EPharmacyException;

@Service
public class PaymentServiceImpl implements PaymentService {
	

	@Override
	public List<CardDTO> viewCards(Integer customerId) throws EPharmacyException {
		//code here
		return null;
	}

	@Override
	public void addCard(CardDTO cardDTO) throws EPharmacyException, NoSuchAlgorithmException {
		//code here
	}

	@Override
	public void deleteCard(String cardId) throws EPharmacyException {
		//code here

	}

	@Override
	public Integer makePayment(CardDTO cardDTO, Float amountToPay)
			throws EPharmacyException, NoSuchAlgorithmException {
		//code here
		return null;
		
	}

	@Override
	public PaymentDTO getPaymentDetails(Integer paymentId) throws EPharmacyException {
		//code here
		return null;
	}

	@Override
	public CardDTO getCardDetails(String cardId) throws EPharmacyException {
		//code here
		return null;
	}

}
