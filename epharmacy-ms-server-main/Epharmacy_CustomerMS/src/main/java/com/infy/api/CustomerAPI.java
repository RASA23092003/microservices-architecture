package com.infy.api;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.ChangePasswordDTO;
import com.infy.dto.CustomerAddressDTO;
import com.infy.dto.CustomerDTO;
import com.infy.exception.EPharmacyException;
import com.infy.service.CustomerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@CrossOrigin
@RestController
@RequestMapping(value = "/customer-api")
public class CustomerAPI {

	@Autowired
	private CustomerService customerService;

	@Autowired 
	private Environment environment;
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
		return new ResponseEntity<>(successMessage,HttpStatus.CREATED);
	}

	@GetMapping("/customer/{customerId}")
	public ResponseEntity<CustomerDTO> viewCustomer(@PathVariable Integer customerId) throws EPharmacyException {
		CustomerDTO customer=customerService.viewCustomer(customerId);
		return new ResponseEntity<>(customer,HttpStatus.OK);
	}
	
	@GetMapping("/customer/view-addresses/{customerId}")
	public ResponseEntity<List<CustomerAddressDTO>> viewAllAddress(@PathVariable Integer customerId)
			throws EPharmacyException {
		List<CustomerAddressDTO> addresslist=customerService.viewAllAddress(customerId);
		return new ResponseEntity<>(addresslist,HttpStatus.OK);
	}

	@PostMapping("/customer/add-address/{customerId}")
	public ResponseEntity<String> addAddress(@RequestBody @Valid CustomerAddressDTO caDTO,@PathVariable Integer customerId) throws EPharmacyException {
		customerService.addCustomerAddress(caDTO, customerId);
		String successMessage=environment.getProperty("CustomerAPI.ADDRESS_ADDED_SUCCESS");
		return new ResponseEntity<>(successMessage,HttpStatus.CREATED);
	}

	@PutMapping("/customer/update-profile")
	public ResponseEntity<String> updateCustomer(@RequestBody CustomerDTO cdto) throws EPharmacyException {
		String successMessage=customerService.updateCustomerDetails(cdto);
		return new ResponseEntity<>(successMessage,HttpStatus.OK);
	}

	@DeleteMapping("/customer/delete-address/{customerId}")
	public ResponseEntity<String> deleteAddress(@PathVariable Integer addressId) throws EPharmacyException {
		customerService.deleteAddress(addressId);
		String successMsg=environment.getProperty("CustomerAPI.DELETE_CUSTOMER_ADDRESS_SUCCESS");
		return new ResponseEntity<>(successMsg,HttpStatus.OK);
	}
	@PutMapping("/customer/change-password")
	public ResponseEntity<String> changePassword(@RequestBody ChangePasswordDTO cpDTO) throws Exception {
		customerService.changePassword(cpDTO);
		String successMsg=environment.getProperty("CustomerAPI.CHANGE_PASSWORD_SUCCESS");
		return new ResponseEntity<>(successMsg,HttpStatus.OK);

	}
	@PutMapping("/customer/upgrade")
	public ResponseEntity<String> upgradeCustomer(@RequestBody CustomerDTO custDTO) throws Exception {
		LocalDate expiaryDate=customerService.upgradeCustomerToPrime(custDTO);
		String successMsg=environment.getProperty("CustomerAPI.UPGRADE_CUSTOMER_SUCCESS");
		return new ResponseEntity<>(successMsg,HttpStatus.OK);
	}
}
