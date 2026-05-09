package com.infy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.infy.entity.Order;
import java.util.List;


public interface OrderRepository extends JpaRepository<Order,Integer>{
	//Write the appropriate methods if required
	@Query("Select o from Order o where o.customerId=:customerId")
	List<Order> findByCustomerId(@Param("customerId")Integer customerId);
}
