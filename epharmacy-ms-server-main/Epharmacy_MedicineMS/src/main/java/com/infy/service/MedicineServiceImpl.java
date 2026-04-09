package com.infy.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.infy.dto.MedicineDTO;
import com.infy.entity.Medicine;
import com.infy.exception.EPharmacyException;
import com.infy.repository.MedicineRepository;

@Service(value = "medicineService")
@Transactional
public class MedicineServiceImpl implements MedicineService {

	@Autowired
	private MedicineRepository medicineRepository;
	@Autowired
	ObjectMapper objectMapper;

	@Override
	public List<MedicineDTO> getAllMedicines(Integer pageNumber, Integer pageSize) throws EPharmacyException {

		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<Medicine> pageMedicine = medicineRepository.findAll(pageable);

		if (pageMedicine.isEmpty()) {
			throw new EPharmacyException("MedicineService.NO_MEDICINE_FOUND");
		}
		List<MedicineDTO> listOfMedicineDTO = new ArrayList<>();

		List<Medicine> medicine = pageMedicine.getContent();
		for (Medicine m : medicine) {
			MedicineDTO mDTO = new MedicineDTO();
			mDTO.setMedicineId(m.getMedicineId());
			mDTO.setMedicineName(m.getMedicineName());
			mDTO.setCategory(m.getCategory());
			mDTO.setDiscountPercent(m.getDiscountPercent());
			mDTO.setManufacturingDate(m.getManufacturingDate());
			mDTO.setExpiryDate(m.getExpiryDate());
			mDTO.setManufacturer(m.getManufacturer());
			mDTO.setPrice(m.getPrice());
			mDTO.setQuantity(m.getQuantity());

			listOfMedicineDTO.add(mDTO);
		}

		return listOfMedicineDTO;
	}

	@Override
	public List<MedicineDTO> getMedicinesByCategory(String category) throws EPharmacyException {
	    List<Medicine> medicineList=medicineRepository.findByCategory(category);
		if(medicineList.isEmpty()) throw new EPharmacyException("MedicineService.NO_MEDICINE_FOUND_CATEGORY");
		List<MedicineDTO> medicineDTOs=new ArrayList<>();
		for(Medicine medicine:medicineList){
			medicineDTOs.add(objectMapper.convertValue(medicine, MedicineDTO.class));
		}
		return medicineDTOs;
	}

	@Override
	public MedicineDTO getMedicineById(Integer medicineId) throws EPharmacyException {
		Medicine medicine=medicineRepository.findById(medicineId).orElseThrow(()->new EPharmacyException("MedicineService.NO_MEDICINE_FOUND"));
		MedicineDTO medicinrDto=objectMapper.convertValue(medicine, MedicineDTO.class);
		return medicinrDto;
	}

	@Override
	public void updateMedicineQuantityAfterOrder(Integer medicineId, Integer orderedQuantity) throws EPharmacyException {
		Medicine medicine=medicineRepository.findById(medicineId).orElseThrow(()->new EPharmacyException("MedicineService.NO_MEDICINE_FOUND"));
		if(medicine.getQuantity()<orderedQuantity) throw new EPharmacyException("MedicineService.MEDICINE_OUT_OF_STOCK");
		medicine.setQuantity(medicine.getQuantity()-orderedQuantity);

	}

	@Override
	public Integer addMedicine(MedicineDTO medicineDTO) throws EPharmacyException {
		Optional<Medicine> medicine=medicineRepository.fingByName(medicineDTO.getMedicineName());
		if(medicine.isPresent()) throw new EPharmacyException("MedicineService.MEDICINE_FOUND");
        Medicine newMedicine=objectMapper.convertValue(medicineDTO, Medicine.class);
		
		return medicineRepository.save(newMedicine).getMedicineId();
	}

}
