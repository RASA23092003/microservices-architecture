package com.infy.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infy.dto.CustomerCartDTO;
import com.infy.exception.EPharmacyException;
import com.infy.service.CustomerCartService;

@RequestMapping(value = "/cart-api")
public class CartAPI {
	
	@Autowired
	private CustomerCartService customerCartService;
	
	public ResponseEntity<String> addMedicinesToCart(CustomerCartDTO customerCartDTO, Integer medicineId, Integer customerId)
			throws EPharmacyException {
		
		// write your logic here
		return null;
	}

	@GetMapping(value = "cart/medicines/customer/{customerId}")
	public ResponseEntity<List<CustomerCartDTO>> getMedicinesFromCart(@PathVariable("customerId") Integer customerId)
			throws EPharmacyException {
		List<CustomerCartDTO> customerCartDTOs = customerCartService.getMedicinesFromCart(customerId);
		return new ResponseEntity<>(customerCartDTOs, HttpStatus.OK);

	}

	
	public ResponseEntity<String> modifyQuantityOfMedicineInCart(Integer customerId,
			Integer medicineId, Integer quantity) throws EPharmacyException {
		
		// write your logic here
		return null;

	}

	
	public ResponseEntity<String> deleteMedicineFromCart(Integer customerId,
			Integer medicineId) throws EPharmacyException {
		
		// write your logic here
		return null;
	}

	
	public ResponseEntity<String> deleteAllMedicinesFromCart(Integer customerId)
			throws EPharmacyException {
		// write your logic here
		return null;
	}

}