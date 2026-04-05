package com.infy.api;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.ChangePasswordDTO;
import com.infy.dto.CustomerAddressDTO;
import com.infy.dto.CustomerDTO;
import com.infy.exception.EPharmacyException;
import com.infy.service.CustomerService;

@CrossOrigin
@RestController
@RequestMapping(value = "/customer-api")
public class CustomerAPI {

	@Autowired
	private CustomerService customerService;

	static Log logger = LogFactory.getLog(CustomerAPI.class);

	@PostMapping(value = "customer/login")
	public ResponseEntity<CustomerDTO> authenticateCustomer(@RequestBody CustomerDTO customerDTO) throws Exception {
		CustomerDTO customerDTOFromDB = customerService.authenticateCustomer(customerDTO.getCustomerEmailId(),
				customerDTO.getPassword());
		return new ResponseEntity<>(customerDTOFromDB, HttpStatus.OK);
	}
	
	@PostMapping("/customer/register")
	public ResponseEntity<String> registerCustomer(@RequestBody @Valid CustomerDTO customerDTO) throws Exception {
		String successMessage=customerService.registerNewCustomer(customerDTO);
		return new ResponseEntity<>(successMessage,HttpStatus.OK);
	}

	
	public ResponseEntity<CustomerDTO> viewCustomer(Integer customerId) throws EPharmacyException {
		//write your logic here
		return null;
	}
	

	public ResponseEntity<List<CustomerAddressDTO>> viewAllAddress(Integer customerId)
			throws EPharmacyException {
		//write your logic here
		return null;
	}

	
	public ResponseEntity<String> addAddress(CustomerAddressDTO caDTO,Integer customerId) throws EPharmacyException {
		//write your logic here
		return null;
	}


	public ResponseEntity<String> updateCustomer(CustomerDTO cdto) throws EPharmacyException {

		//write your logic here
		return null;
	}

	
	public ResponseEntity<String> deleteAddress(Integer addressId) throws EPharmacyException {
		//write your logic here
		return null;
	}
	
	public ResponseEntity<String> changePassword(ChangePasswordDTO cpDTO) throws Exception {
		//write your logic here
		return null;

	}
	

	public ResponseEntity<String> upgradeCustomer(CustomerDTO custDTO) throws Exception {
	
		//write your logic here
		return null;
	}
}
