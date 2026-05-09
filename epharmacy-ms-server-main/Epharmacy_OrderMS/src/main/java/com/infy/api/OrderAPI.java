package com.infy.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.infy.dto.OrderDTO;
import com.infy.entity.Order;
import com.infy.entity.OrderedMedicine;
import com.infy.exception.EPharmacyException;
import com.infy.service.OrderService;
@RestController
@RequestMapping(value = "/order-api")
public class OrderAPI {

	@Autowired
	private OrderService orderService;
	@Autowired
	RestTemplate restTemplate;
	@Autowired
	Environment environment;

	@GetMapping(value = "/order/view-orders/customer/{customerId}")
	ResponseEntity<List<OrderDTO>> viewOrders(@PathVariable Integer customerId) throws EPharmacyException {
		return new ResponseEntity<List<OrderDTO>>(orderService.viewOrders(customerId), HttpStatus.OK);
	}

	@PostMapping("/order/place-order")
	ResponseEntity<String> placeOrder(@RequestBody OrderDTO orderDTO) throws EPharmacyException {
		if(orderDTO.getOrderValueBeforeDiscount()>=10000 && orderDTO.getOrderValueBeforeDiscount()<20000){
			orderDTO.setDiscountPercent(10.0);
		}else if(orderDTO.getOrderValueBeforeDiscount()>=20000 && orderDTO.getOrderValueBeforeDiscount()<30000){
			orderDTO.setDiscountPercent(20.0);
		}
		else if(orderDTO.getOrderValueBeforeDiscount()>=30000){
			orderDTO.setDiscountPercent(30.0);
		}else{
			orderDTO.setDiscountPercent(0.0);
		}
		Double price=orderDTO.getOrderValueBeforeDiscount()-((orderDTO.getOrderValueBeforeDiscount())*(orderDTO.getDiscountPercent()/100));
		orderDTO.setOrderValueAfterDiscount(price);
		String paymentResponse=restTemplate.postForObject("http://Epharmacy-PaymentMS/epharmacy/payment-api/payment/amount/"+orderDTO.getOrderValueAfterDiscount(), orderDTO.getCard(), String.class);
		if(paymentResponse.contains("success")){
			Order order=orderService.placeOrder(orderDTO);
			System.err.println("Order placed with order id: "+order.getOrderId());
			restTemplate.delete("http://Epharmacy-CartMS/epharmacy/cart-api/cart/delete-medicines/customer/"+orderDTO.getCustomer().getCustomerId());
			System.err.println("Cart cleared for customer id: "+orderDTO.getCustomer().getCustomerId());
			for(OrderedMedicine medicine:order.getOrderedMedicines()) {
				System.err.println("Updating stock for medicine id: "+medicine.getMedicineId());
				restTemplate.put("http://Epharmacy-MedicineMS/epharmacy/medicine-api/medicines/update-stock/"+medicine.getMedicineId()+"/"+ medicine.getOrderedQuantity(), null);
			}
			return new ResponseEntity<String>(environment.getProperty("OrderAPI.PLACE_ORDER_SUCCESS"),HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<String>(environment.getProperty("OrderAPI.PLACE_ORDER_FAILURE"),HttpStatus.BAD_REQUEST);
		}
		

	}

	@PutMapping("/order/cancel-order/{orderId}")
	ResponseEntity<String> cancelOrder(@PathVariable Integer orderId, @RequestBody String reasonToCancel)
			throws EPharmacyException {
		orderService.cancelOrder(orderId, reasonToCancel);
		return new ResponseEntity<String>(environment.getProperty("OrderAPI.CANCEL_ORDER_SUCCESS"),HttpStatus.OK);
	}
}
