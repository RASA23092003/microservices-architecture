package com.infy.dto;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
//Write the necessary annotations to validate the fields 
public class CustomerDTO {
	private Integer customerId;
	@Pattern(regexp="^[A-Za-z]+(//s[A-Za-z]+)*$",message = "{customer.name.invalid}")
	private String customerName;
	@Email(message ="{customer.email.invalid}")
	private String customerEmailId;
	@Pattern(regexp="^[6-7][0-9]{9}$",message="{customer.mobile.invalid}")
	private String contactNumber;
	@NotNull(message="{customer.password.invalid}")
	private String password;
	@Pattern(regexp = "^(Male|Female|Other)",message="{customer.gender.invalid}")
	private String gender;
	@NotNull(message = "{customer.dob.required}")
	private LocalDate dateOfBirth;
	@Valid
	private List<CustomerAddressDTO> addressList;
	
	private PrimePlansDTO plan;
	private LocalDate planExpiryDate;
	private Integer healthCoins;

	public Integer getHealthCoins() {
		return healthCoins;
	}

	public void setHealthCoins(Integer healthCoins) {
		this.healthCoins = healthCoins;
	}

	public LocalDate getPlanExpiryDate() {
		return planExpiryDate;
	}

	public void setPlanExpiryDate(LocalDate planExpiryDate) {
		this.planExpiryDate = planExpiryDate;
	}

	public PrimePlansDTO getPlan() {
		return plan;
	}

	public void setPlan(PrimePlansDTO plan) {
		this.plan = plan;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerEmailId() {
		return customerEmailId;
	}

	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<CustomerAddressDTO> getAddressList() {
		return addressList;
	}

	public void setAddressList(List<CustomerAddressDTO> addressList) {
		this.addressList = addressList;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

}
