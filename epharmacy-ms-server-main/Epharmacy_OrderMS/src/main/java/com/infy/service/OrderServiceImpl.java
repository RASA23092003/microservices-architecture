package com.infy.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.infy.dto.OrderDTO;
import com.infy.exception.EPharmacyException;

@Service
public class OrderServiceImpl implements OrderService {

	@Override
	public List<OrderDTO> viewOrders(Integer customerId) throws EPharmacyException {
		//code here
		return null;
	}


	@Override
	public String placeOrder(OrderDTO orderDTO) throws EPharmacyException {
		//code here
		return null;
	}

	@Override
	public void cancelOrder(Integer orderId, String reason) throws EPharmacyException {
		//code here
	}
}
