package com.infy.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

//Write the necessary annotations to validate the fields 
public class CustomerAddressDTO {
	
	private Integer addressId;
	@Pattern(regexp = "(Home|Work|Office)",message="{address.name.notpresent}")
	@NotNull(message = "{address.name.invalid}")
	private String addressName;
	@NotNull(message="{address.line1.notpresent}")
	private String addressLine1;
	private String addressLine2;
	private String area;
	@NotNull(message = "{address.city.notpresent}")
	@Pattern(regexp="[a-zA-Z]+(\\s[a-zA-Z]+)*$",message="{address.city.invalid}")
	private String city;
	@NotNull(message = "{address.state.notpresent}")
	@Pattern(regexp="[a-zA-Z]+(\\s[a-zA-Z]+)*$",message="{address.state.invalid}")
	private String state;
	@NotNull(message = "{address.pincode.notpresent}")
	@Pattern(regexp = "[0-9]{6}",message="address.pincode.invalid")
	private String pincode;
	
	public CustomerAddressDTO() {
		super();
	}
	
	public CustomerAddressDTO(Integer addressId, String addressName, String addressLine1, String addressLine2,
			String area, String city, String state, String pincode) {
		super();
		this.addressId = addressId;
		this.addressName = addressName;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.area = area;
		this.city = city;
		this.state = state;
		this.pincode = pincode;
	}

	public Integer getAddressId() {
		return addressId;
	}
	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}
	public String getAddressName() {
		return addressName;
	}
	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getPincode() {
		return pincode;
	}
	public void setPincode(String pincode) {
		this.pincode = pincode;
	}

}
