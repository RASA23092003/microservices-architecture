package com.infy.service;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.dto.ChangePasswordDTO;
import com.infy.dto.CustomerAddressDTO;
import com.infy.dto.CustomerDTO;
import com.infy.entity.Customer;
import com.infy.entity.CustomerAddress;
import com.infy.entity.PasswordHistory;
import com.infy.entity.PrimePlans;
import com.infy.exception.EPharmacyException;
import com.infy.repository.CustomerAddressRepository;
import com.infy.repository.CustomerRepository;
import com.infy.repository.PasswordHistoryRepository;
import com.infy.repository.PrimePlansRepository;
import com.infy.utility.HashingUtility;

@Service(value = "customerService")
@Transactional
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private Environment environment;
	@Autowired
	private CustomerAddressRepository customerAddressRepository;
	@Autowired
	private PasswordHistoryRepository passwordHistoryRepo;
	@Autowired
	private PrimePlansRepository planRepo;

	
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
		Customer customer=customerRepository.findByCustomerEmailId(customerDTO.getCustomerEmailId());
		if(customer!=null) throw new EPharmacyException("CustomerService.CUSTOMER_ALREADY_EXISTS");
		if((LocalDate.now().getDayOfYear()-customerDTO.getDateOfBirth().getDayOfYear())<18){
			throw new EPharmacyException("CustomerService.INVALID_DATE");
		}
		Customer customerEntity=objectMapper.convertValue(customerDTO, Customer.class);
		String hashedPassword = HashingUtility.getHashValue(customer.getPassword());
		customerEntity.setPassword(hashedPassword);
		customerEntity.getPlan().setPlanId(0);
		Integer customerId=customerRepository.save(customerEntity).getCustomerId();
		String successMessage=environment.getProperty("CustomerAPI.CUSTOMER_REGISTRATION_SUCCESS1"+"CustomerAPI.CUSTOMER_REGISTRATION_SUCCESS2"+" "+customerId);
		return successMessage;
	}

	@Override
	public void addCustomerAddress(CustomerAddressDTO addressDTO, Integer customerId) throws EPharmacyException {
		Customer customer=customerRepository.findById(customerId).orElseThrow(()->new EPharmacyException("CustomerService.NO_CUSTOMER_FOUND"));
		CustomerAddress address=objectMapper.convertValue(addressDTO, CustomerAddress.class);
		customer.getAddressList().add(address);
		return;
	}

	@Override
	public CustomerDTO viewCustomer(Integer CustomerId) throws EPharmacyException {
		Customer customer=customerRepository.findById(CustomerId).orElseThrow(()->new EPharmacyException("CustomerService.NO_CUSTOMER_FOUND"));
		List<CustomerAddressDTO> customerDTOList=new ArrayList<>();
		for(CustomerAddress address:customer.getAddressList()){
			customerDTOList.add(objectMapper.convertValue(address,CustomerAddressDTO.class));
		}
		CustomerDTO customerDto=objectMapper.convertValue(customer, CustomerDTO.class);
		customerDto.setAddressList(customerDTOList);
		return customerDto;
	}

	@Override
	public void deleteAddress(Integer addressId) throws EPharmacyException {
		CustomerAddress address=customerAddressRepository.findById(addressId).orElseThrow(()->new EPharmacyException("CustomerService.NO_ADDRESS_FOUND"));
		customerAddressRepository.delete(address);
		return;
	}

	@Override
	public List<CustomerAddressDTO> viewAllAddress(Integer customerId) throws EPharmacyException {
		Customer customer=customerRepository.findById(customerId).orElseThrow(()->new EPharmacyException("CustomerService.NO_CUSTOMER_FOUND"));
		List<CustomerAddressDTO> addressDTO=new ArrayList<>();
		for(CustomerAddress address:customer.getAddressList()){
			addressDTO.add(objectMapper.convertValue(address, CustomerAddressDTO.class));
		}
		return addressDTO;
	}

	@Override
	public String updateCustomerDetails(CustomerDTO dto) throws EPharmacyException {
		Customer customer=customerRepository.findById(dto.getCustomerId()).orElseThrow(()->new EPharmacyException("CustomerService.NO_CUSTOMER_FOUND"));
		customer.setCustomerName(dto.getCustomerName());
		customer.setCustomerEmailId(dto.getCustomerEmailId());
		customer.setContactNumber(dto.getContactNumber());
		String successMsg=environment.getProperty("CustomerAPI.UPGRADE_CUSTOMER_SUCCESS");
		return successMsg;
	}

	@Override
	public void changePassword(ChangePasswordDTO changePasswordDTO) throws EPharmacyException, NoSuchAlgorithmException{
		Customer customer=customerRepository.findById(changePasswordDTO.getCustomerId()).orElseThrow(()->new EPharmacyException("CustomerService.NO_CUSTOMER_FOUND"));
		String hashPassword=HashingUtility.getHashValue(changePasswordDTO.getOldPassword());
		if(hashPassword!=customer.getPassword()){
			throw new EPharmacyException("CustomerService.WRONG_PASSWORD");
		}
		if(!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())){
			throw new EPharmacyException("CustomerService.PASSWORDS_DO_NOT_MATCH");
		}
		String newhashPassword=HashingUtility.getHashValue(changePasswordDTO.getNewPassword());

		List<PasswordHistory> history=passwordHistoryRepo.findByCustomer(changePasswordDTO.getCustomerId());
		for(int i=history.size()-1;i>=history.size()-4;i--){
			if(newhashPassword.equals(history.get(i).getPassword())){
				throw new EPharmacyException("CustomerService.PASSWORD_FOUND_IN_HISTORY");
			}
		}
		customer.setPassword(newhashPassword);
		PasswordHistory password=objectMapper.convertValue(changePasswordDTO, PasswordHistory.class);
		passwordHistoryRepo.save(password);
		return;
	}

	@Override
	public LocalDate upgradeCustomerToPrime(CustomerDTO customerDTO) throws EPharmacyException {
		int planId=customerDTO.getPlan().getPlanId();
		Customer customer=customerRepository.findById(customerDTO.getCustomerId()).orElseThrow(()->new EPharmacyException("CustomerService.NO_CUSTOMER_FOUND"));
		if(planId==0) throw new EPharmacyException("CustomerService.NO_PLAN_SELECTED");
		if(customer.getPlan().getPlanId()<=0 && customer.getPlan().getPlanId()<4) throw new EPharmacyException("CustomerService.ALREADY_SUBSCRIBED");
		PrimePlans plans=planRepo.findById(customerDTO.getPlan().getPlanId()).orElseThrow(()->new EPharmacyException("CustomerService.NO_PLAN_FOUND"));
		customer.getPlan().setPlanId(planId);
		LocalDate expiaryDate = null;
		if(planId==1) expiaryDate.plusMonths(1);
		else if(planId==2) expiaryDate.plusMonths(4);
		else if(planId==3) expiaryDate.plusYears(1);
		customer.setPlanExpiryDate(expiaryDate);
		return expiaryDate;
	}
}
