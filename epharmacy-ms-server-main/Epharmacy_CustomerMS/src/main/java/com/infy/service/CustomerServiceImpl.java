package com.infy.service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.infy.dto.ChangePasswordDTO;
import com.infy.dto.CustomerAddressDTO;
import com.infy.dto.CustomerDTO;
import com.infy.entity.Customer;
import com.infy.entity.CustomerAddress;
import com.infy.exception.EPharmacyException;
import com.infy.repository.CustomerRepository;
import com.infy.utility.HashingUtility;

@Service(value = "customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	
	@Override
	public CustomerDTO authenticateCustomer(String emailId, String password)
			throws EPharmacyException, NoSuchAlgorithmException {
		CustomerDTO customerDTO = null;
		Customer customer = customerRepository.findByCustomerEmailId(emailId);
		if (customer != null) {
			customerDTO = new CustomerDTO();
			customerDTO.setCustomerId(customer.getCustomerId());
			customerDTO.setCustomerEmailId(customer.getCustomerEmailId());
			customerDTO.setCustomerName(customer.getCustomerName());
			customerDTO.setContactNumber(customer.getContactNumber());
			customerDTO.setPassword(customer.getPassword());

			List<CustomerAddressDTO> addressList = new ArrayList<>();
			for (CustomerAddress ca : customer.getAddressList()) {
				CustomerAddressDTO caDTO = new CustomerAddressDTO();
				caDTO.setAddressId(ca.getAddressId());
				caDTO.setAddressName(ca.getAddressName());
				caDTO.setAddressLine1(ca.getAddressLine1());
				caDTO.setAddressLine2(ca.getAddressLine2());
				caDTO.setArea(ca.getArea());
				caDTO.setCity(ca.getCity());
				caDTO.setState(ca.getState());
				caDTO.setPincode(ca.getPincode());

				addressList.add(caDTO);
			}
			customerDTO.setAddressList(addressList);
		}
		if (customerDTO == null) {
			throw new EPharmacyException("CustomerService.INVALID_CREDENTIALS");
		}

		String passwordFromDB = customer.getPassword();
		if (passwordFromDB != null) {
			String hashedPassword = HashingUtility.getHashValue(password);

			if (hashedPassword.equals(passwordFromDB)) {
				customerDTO.setPassword(null);
				return customerDTO;
			} else {
				throw new EPharmacyException("CustomerService.INVALID_CREDENTIALS");
			}
		} else {
			throw new EPharmacyException("CustomerService.INVALID_CREDENTIALS");
		}

	}

	// This method will add a new customer
	@Override
	public String registerNewCustomer(CustomerDTO customerDTO) throws EPharmacyException, NoSuchAlgorithmException {
		//write your logic here
		return null;
	}

	@Override
	public void addCustomerAddress(CustomerAddressDTO addressDTO, Integer customerId) throws EPharmacyException {
		
	}

	@Override
	public CustomerDTO viewCustomer(Integer CustomerId) throws EPharmacyException {
		//write your logic here
		return null;
	}

	@Override
	public void deleteAddress(Integer addressId) throws EPharmacyException {
		//write your logic here

	}

	@Override
	public List<CustomerAddressDTO> viewAllAddress(Integer customerId) throws EPharmacyException {
		//write your logic here
		return null;
	}

	@Override
	public String updateCustomerDetails(CustomerDTO dto) throws EPharmacyException {
		//write your logic here
		return null;
	}

	@Override
	public void changePassword(ChangePasswordDTO changePasswordDTO) throws EPharmacyException{
		//write your logic here
	}

	@Override
	public LocalDate upgradeCustomerToPrime(CustomerDTO customerDTO) throws EPharmacyException {
		//write your logic here
		return null;
	}
}
