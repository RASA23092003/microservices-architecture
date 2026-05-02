package com.infy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.dto.DeliveryStatus;
import com.infy.dto.OrderDTO;
import com.infy.dto.OrderStatus;
import com.infy.dto.OrderedMedicineDTO;
import com.infy.entity.Order;
import com.infy.entity.OrderedMedicine;
import com.infy.exception.EPharmacyException;
import com.infy.repository.OrderRepository;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	OrderRepository orderRepository;

	@Override
	public List<OrderDTO> viewOrders(Integer customerId) throws EPharmacyException {
		//code here
		return null;
	}


	@Override
	public String placeOrder(OrderDTO orderDTO) throws EPharmacyException {
		Order neworder=new Order();
		neworder.setOrderValueBeforeDiscount(orderDTO.getOrderValueAfterDiscount());
		neworder.setCustomerId(orderDTO.getCustomer().getCustomerId());
		neworder.setDeliveryAddressId(orderDTO.getDeliveryAddress().getAddressId());
		if(orderDTO.getOrderValueBeforeDiscount()>=10000 && orderDTO.getOrderValueBeforeDiscount()<20000){
			neworder.setDiscountPercent(10.0);
		}else if(orderDTO.getOrderValueBeforeDiscount()>=20000 && orderDTO.getOrderValueBeforeDiscount()<30000){
			neworder.setDiscountPercent(20.0);
		}
		else if(orderDTO.getOrderValueBeforeDiscount()>=30000){
			neworder.setDiscountPercent(30.0);
		}else{
			neworder.setDiscountPercent(0.0);
		}
		Double price=neworder.getOrderValueBeforeDiscount()-((neworder.getOrderValueBeforeDiscount())*(neworder.getDiscountPercent()/100));
		neworder.setOrderValueAfterDiscount(price);
		neworder.setDeliveryDate(LocalDateTime.now().plusDays(1));
		neworder.setDeliveryStatus(DeliveryStatus.AWAITING_CONFIRMATION);
		neworder.setOrderStatus(OrderStatus.PROCESSING);
		neworder.setOrderDate(LocalDateTime.now());
		List<OrderedMedicine> medicineList=new ArrayList<>();
		for(OrderedMedicineDTO dto:orderDTO.getOrderedMedicines()){
			OrderedMedicine medicine=objectMapper.convertValue(dto, OrderedMedicine.class);
			medicine.setMedicineId(dto.getMedicine().getMedicineId());
			medicineList.add(medicine);
		}
		neworder.setCardId(orderDTO.getCard().getCardId());

		
		
		return null;
	}

	@Override
	public void cancelOrder(Integer orderId, String reason) throws EPharmacyException {
		//code here
	}
}
