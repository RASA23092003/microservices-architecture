package com.infy.api;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infy.dto.MedicineDTO;
import com.infy.exception.EPharmacyException;
import com.infy.service.MedicineService;

@RestController
@RequestMapping(value="/epharmacy/medicine-api")
@CrossOrigin

public class MedicineAPI {
	
	@Autowired
	private MedicineService medicineService;
	@Autowired
	Environment environment;
	
	@GetMapping(value = "/medicines/pageNumber/{pageNumber}/pageSize/{pageSize}")
	public ResponseEntity<List<MedicineDTO>> getAllMedicines(@PathVariable Integer pageNumber,@PathVariable Integer pageSize) throws EPharmacyException {
		List<MedicineDTO> allmedicines= medicineService.getAllMedicines(pageNumber, pageSize);
		return new ResponseEntity<>(allmedicines, HttpStatus.OK);
	}
	
	@GetMapping("/medicines/{medicineId}")
	public ResponseEntity<MedicineDTO> getMedicineById(@PathVariable Integer medicineId) throws EPharmacyException {
		MedicineDTO medicine=medicineService.getMedicineById(medicineId);
		return new ResponseEntity<>(medicine,HttpStatus.OK);
	}

	@GetMapping("/medicines/category/{categoryName}")
	public ResponseEntity<List<MedicineDTO>> category(@PathVariable String categoryName ) throws EPharmacyException {
		return new ResponseEntity<>(medicineService.getMedicinesByCategory(categoryName),HttpStatus.OK);
	}
	
	@PutMapping("/medicines/update-stock/{medicineId}/{orderedQuantity}")
	public ResponseEntity<String> modifyQuantityOfMedicineInStock(@PathVariable Integer medicineId,@PathVariable Integer orderedQuantity) throws EPharmacyException {
		medicineService.updateMedicineQuantityAfterOrder(medicineId, orderedQuantity);
		String successMsg=environment.getProperty("MedicineAPI.MEDICINE_QUANTITY_UPDATE_SUCCESS");
		return new ResponseEntity<>(successMsg,HttpStatus.OK);
	}

	@PostMapping("/medicines/addmedicine")
	public ResponseEntity<String> addMedicine(@RequestBody @Valid MedicineDTO medicineDto) throws EPharmacyException{
        int medicineId=medicineService.addMedicine(medicineDto);
		String successMsg=environment.getProperty("MedicineAPI.MEDICINE_ADDED_SUCCESS")+" "+medicineId;
		return new ResponseEntity<>(successMsg,HttpStatus.CREATED);
	}
}
