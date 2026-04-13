package com.infy.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.CustomerCartDTO;
import com.infy.exception.EPharmacyException;
import com.infy.service.CustomerCartService;
@RestController
@RequestMapping(value = "/cart-api")
public class CartAPI {
	
	@Autowired
	private CustomerCartService customerCartService;
	@Autowired 
	private Environment environment;
	
	@GetMapping("/cart/add-medicine/{medicineId}/customer/{customerId}")
	public ResponseEntity<String> addMedicinesToCart(@RequestBody CustomerCartDTO customerCartDTO, @PathVariable("medicineId") Integer medicineId,@PathVariable("customerId") Integer customerId)
			throws EPharmacyException {
		customerCartService.addMedicinesToCart(customerCartDTO, medicineId, customerId);
		String successMsg=environment.getProperty("CustomerCartAPI.ADD_TO_CART_SUCCESS");
		return new ResponseEntity<>(successMsg,HttpStatus.CREATED);
	}

	@GetMapping(value = "/cart/medicines/customer/{customerId}")
	public ResponseEntity<List<CustomerCartDTO>> getMedicinesFromCart(@PathVariable("customerId") Integer customerId)
			throws EPharmacyException {
		List<CustomerCartDTO> customerCartDTOs = customerCartService.getMedicinesFromCart(customerId);
		return new ResponseEntity<>(customerCartDTOs, HttpStatus.OK);

	}

	@PutMapping("/cart/update-quantity/medicine/{medicineId}/customer/{customerId}")
	public ResponseEntity<String> modifyQuantityOfMedicineInCart(@PathVariable("customerId") Integer customerId,
			@PathVariable("medicineId") Integer medicineId,@RequestBody Integer quantity) throws EPharmacyException {
		customerCartService.modifyQuantityOfMedicinesInCart(customerId, medicineId, quantity);
		String successMsg=environment.getProperty("CustomerCartAPI.MEDICINE_QUANTITY_UPDATE_SUCCESS");
		return new ResponseEntity<>(successMsg,HttpStatus.OK);

	}

	@DeleteMapping("/cart/delete-medicine/{medicineId}/customer/{customerId}")
	public ResponseEntity<String> deleteMedicineFromCart(@PathVariable(value="customerId") Integer customerId,
			@PathVariable(value="medicineId") Integer medicineId) throws EPharmacyException {
		customerCartService.deleteAllMedicinesFromCart(customerId);
		String successMSg=environment.getProperty("CustomerCartAPI.MEDICINE_DELETE_FROM_CART_SUCCESS");
		return new ResponseEntity<>(successMSg,HttpStatus.OK);
	}

	@DeleteMapping("/cart/delete-medicines/customer/{customerId}")
	public ResponseEntity<String> deleteAllMedicinesFromCart(@PathVariable Integer customerId)
			throws EPharmacyException {
		customerCartService.deleteAllMedicinesFromCart(customerId);
		String successMsg=environment.getProperty("CustomerCartAPI.ALL_MEDICINES_DELETE_SUCCESS");
		return new ResponseEntity<>(successMsg,HttpStatus.OK);
	}

}