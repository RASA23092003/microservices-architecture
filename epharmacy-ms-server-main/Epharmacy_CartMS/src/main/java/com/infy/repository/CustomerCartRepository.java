package com.infy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.infy.entity.CustomerCart;


public interface CustomerCartRepository extends CrudRepository<CustomerCart, Integer> {
	List<CustomerCart> findByCustomerId(Integer customerId);
	@Query("Select c from CustomerCart c where c.medicineId=:medicineId and c.customerId=:customerId")
	CustomerCart findByCustomerIdAndMedicineId(@Param("medicineId")Integer medicineId,@Param("customerId") Integer customerId);
}