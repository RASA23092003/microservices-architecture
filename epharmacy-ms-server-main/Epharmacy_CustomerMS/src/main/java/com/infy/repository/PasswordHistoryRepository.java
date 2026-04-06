package com.infy.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.infy.entity.PasswordHistory;

public interface PasswordHistoryRepository extends CrudRepository<PasswordHistory, Integer>{
	//Write the appropriate methods if required
	@Query("Select p from passwordhistory p where p.customerId=:customerId")
	List<PasswordHistory> findByCustomer(@Param("customerId")int customerId);
}
