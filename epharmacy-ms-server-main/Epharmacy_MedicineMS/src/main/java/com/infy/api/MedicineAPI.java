package com.infy.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.MedicineDTO;
import com.infy.exception.EPharmacyException;
import com.infy.service.MedicineService;

@RestController
@RequestMapping(value= "medicine-api")
@Validated 
@CrossOrigin
public class MedicineAPI {
	
	@Autowired
	private MedicineService medicineService;
	
	@GetMapping(value = "/medicines/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<List<MedicineDTO>> getAllMedicines(@PathVariable Integer pageNumber,@PathVariable Integer pageSize) throws EPharmacyException {
		List<MedicineDTO> allmedicines= medicineService.getAllMedicines(pageNumber, pageSize);
		return new ResponseEntity<>(allmedicines, HttpStatus.OK);
	}
	
	
	public ResponseEntity<MedicineDTO> getMedicineById(Integer medicineId) throws EPharmacyException {
		// Write your logic here
		return null;
	}

	
	public ResponseEntity<List<MedicineDTO>> category(String categoryName ) throws EPharmacyException {
		// Write your logic here
		return null;
	}
	
	
	public ResponseEntity<String> modifyQuantityOfMedicineInStock(Integer medicineId, Integer orderedQuantity) throws EPharmacyException {
		// Write your logic here
		return null;
	}
}
