package com.infy.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.OrderDTO;
import com.infy.exception.EPharmacyException;
import com.infy.service.OrderService;
@RestController
@RequestMapping(value = "order-api")
public class OrderAPI {

	@Autowired
	private OrderService orderService;

	@GetMapping(value = "/order/view-orders/customer/{customerId}")
	ResponseEntity<List<OrderDTO>> viewOrders(@PathVariable Integer customerId) throws EPharmacyException {
		return new ResponseEntity<List<OrderDTO>>(orderService.viewOrders(customerId), HttpStatus.OK);
	}

	
	ResponseEntity<String> placeOrder(OrderDTO orderDTO) throws EPharmacyException {

		//Write your logic here
		return null;

	}

	
	ResponseEntity<String> cancelOrder(Integer orderId, @RequestBody String reasonToCancel)
			throws EPharmacyException {
		
		//Write your logic here
		return null;
	}
}
