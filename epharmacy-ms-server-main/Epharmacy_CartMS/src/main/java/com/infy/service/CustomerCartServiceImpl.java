package com.infy.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.infy.dto.CustomerCartDTO;
import com.infy.dto.MedicineDTO;
import com.infy.entity.CustomerCart;
import com.infy.exception.EPharmacyException;
import com.infy.repository.CustomerCartRepository;

@Service(value = "customerCartService")
@Transactional
public class CustomerCartServiceImpl implements CustomerCartService {

	@Autowired
	private CustomerCartRepository customerCartRepository;
	
	@Autowired
	private RestTemplate template;
	

	@Override
	public void addMedicinesToCart(CustomerCartDTO customerCartDTO, Integer medicineId, Integer customerId)
			throws EPharmacyException {
		CustomerCart cartfromDb=customerCartRepository.findByCustomerIdAndMedicineId(medicineId, customerId);
		if(cartfromDb!=null) throw new EPharmacyException("CustomerCartService.MEDICINE_ALREADY_IN_CART");
		CustomerCart cart=new CustomerCart();
		cart.setCustomerId(customerId);
		cart.setMedicineId(medicineId);
		cart.setQuantity(customerCartDTO.getQuantity());
		customerCartRepository.save(cart);
				
	}

	@Override
	public List<CustomerCartDTO> getMedicinesFromCart(Integer customerId) throws EPharmacyException {
		List<CustomerCart> customerCartList = customerCartRepository.findByCustomerId(customerId);
		if (customerCartList.isEmpty()) {
			throw new EPharmacyException("CustomerCartService.EMPTY_CART");
		}
		List<CustomerCartDTO> ccdList = new ArrayList<>();
		for (CustomerCart cc : customerCartList) {
			CustomerCartDTO ccDTO = new CustomerCartDTO();
			ccDTO.setCartId(cc.getCartId());
			ccDTO.setCustomerId(customerId);
			MedicineDTO m = template.getForObject("http://EPharmacy-MedicineMS/epharmacy/medicine-api/medicines/"+cc.getMedicineId(), MedicineDTO.class);
			ccDTO.setMedicine(m);;
			ccDTO.setQuantity(cc.getQuantity());
			ccdList.add(ccDTO);
		}
		return ccdList;
	}

	@Override
	public void modifyQuantityOfMedicinesInCart(Integer customerId, Integer medicineId, Integer quantity)
			throws EPharmacyException {
		CustomerCart cart=customerCartRepository.findByCustomerIdAndMedicineId(medicineId, customerId);
		if(cart==null) throw new EPharmacyException("CustomerCartService.NO_MEDICINE_FOUND");
		int quantitycart=cart.getQuantity()+quantity;
		MedicineDTO medicine=template.getForObject("http://EPharmacy-MedicineMS/epharmacy/medicine-api/medicines/"+medicineId, MedicineDTO.class);
		if(medicine.getQuantity()<quantitycart) throw new EPharmacyException("CustomerCartService.STOCK_NOT_AVAILABLE");
		cart.setQuantity(quantitycart);
	}

	@Override
	public void deleteMedicineFromCart(Integer customerId, Integer medicineId) throws EPharmacyException {
		CustomerCart cart=customerCartRepository.findByCustomerIdAndMedicineId(medicineId, customerId);
		if(cart==null) throw new EPharmacyException("CustomerCartService.NO_MEDICINE_FOUND");
		customerCartRepository.delete(cart);
	}

	@Override
	public void deleteAllMedicinesFromCart(Integer customerId) throws EPharmacyException {
		List<CustomerCart> cartList=customerCartRepository.findByCustomerId(customerId);
		if(cartList.isEmpty()) throw new EPharmacyException("CustomerCartService.EMPTY_CART");
		customerCartRepository.deleteAll(cartList);
	}

}