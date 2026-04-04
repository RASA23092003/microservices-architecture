package com.infy.repository;

import org.springframework.data.repository.CrudRepository;

import com.infy.entity.CustomerAddress;

public interface CustomerAddressRepository extends CrudRepository<CustomerAddress, Integer> {
	//Write the appropriate methods if required
}
