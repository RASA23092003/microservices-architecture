package com.infy.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.dto.CardDTO;
import com.infy.dto.CustomerAddressDTO;
import com.infy.dto.CustomerDTO;
import com.infy.dto.DeliveryStatus;
import com.infy.dto.MedicineDTO;
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

	@Autowired
	RestTemplate restTemplate;

	@Override
	public List<OrderDTO> viewOrders(Integer customerId) throws EPharmacyException {
		List<Order> orders=orderRepository.findByCustomerId(customerId);
		 List<OrderDTO> orderDTOs=new ArrayList<>();
		 for(Order order:orders){
			OrderDTO dto=new OrderDTO();
			dto.setOrderId(order.getOrderId());
			dto.setOrderDate(order.getOrderDate());
			dto.setOrderValueBeforeDiscount(order.getOrderValueBeforeDiscount());
			dto.setOrderValueAfterDiscount(order.getOrderValueAfterDiscount());
			dto.setDiscountPercent(order.getDiscountPercent());
			dto.setDeliveryDate(order.getDeliveryDate());
			dto.setCancelReason(order.getCancelReason());
			for(OrderedMedicine medicine:order.getOrderedMedicines()){
				OrderedMedicineDTO medicineDTO=new OrderedMedicineDTO();
				medicineDTO.setOrderedMedicineId(medicine.getOrderedMedicineId());
				medicineDTO.setOrderedQuantity(medicine.getOrderedQuantity());
				medicineDTO.setOrderSubtotal(medicine.getOrderSubtotal());
				medicineDTO.setMedicine(restTemplate.getForObject("http://Epharmacy-MedicineMS/epharmacy/medicine-api/medicines/"+medicine.getMedicineId(), MedicineDTO.class));
				dto.getOrderedMedicines().add(medicineDTO);
			}
			CardDTO carddto=restTemplate.getForObject("http://Epharmacy-PaymentMS/epharmacy/payment-api/payment/card/"+order.getCardId(), CardDTO.class);
			dto.setCard(carddto);
			CustomerDTO customerDTO=restTemplate.getForObject("http://Epharmacy-CustomerMS/epharmacy/customer-api/customer/"+order.getCustomerId(), CustomerDTO.class);
			dto.setCustomer(customerDTO);
			for(CustomerAddressDTO address:customerDTO.getAddressList()){
				if(address.getAddressId()==order.getDeliveryAddressId()){
					dto.setDeliveryAddress(address);
				}
			}
			if(LocalDateTime.now().isAfter(order.getOrderDate().plusMinutes(30))) {
				dto.setDeliveryStatus(DeliveryStatus.IN_TRANSIT);
				dto.setOrderStatus(OrderStatus.CONFIRMED);
			}else if(LocalDateTime.now().isAfter(order.getOrderDate().plusDays(1))) {
				dto.setDeliveryStatus(DeliveryStatus.OUT_FOR_DELIVERY);
			}else if(LocalDateTime.now().isAfter(order.getOrderDate().plusDays(1).plusHours(5))) {
				dto.setDeliveryStatus(DeliveryStatus.DELIVERED);
				dto.setOrderStatus(OrderStatus.COMPLETED);
			}
			orderDTOs.add(dto);
		}
		return orderDTOs;
	}


	@Override
	public Order placeOrder(OrderDTO orderDTO) throws EPharmacyException {
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
		return orderRepository.save(neworder);
	}

	@Override
	public void cancelOrder(Integer orderId, String reason) throws EPharmacyException {
		Order order=orderRepository.findById(orderId).orElseThrow(()->new EPharmacyException("OrderService.NO_ORDERED_PRODUCTS_FOUND"));
		if(order.getOrderStatus()==OrderStatus.CANCELLED) {
			throw new EPharmacyException("OrderService.ORDER_ALREADY_CANCELLED");
		}
		if(order.getOrderStatus()==OrderStatus.PROCESSING) {
			order.setOrderStatus(OrderStatus.CANCELLED);
			order.setCancelReason(reason);
			orderRepository.save(order);
		}else {
			throw new EPharmacyException("OrderService.ORDER_CANNOT_CANCEL");
		}
	}
}
