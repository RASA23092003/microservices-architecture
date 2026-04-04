package com.infy.repository;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.infy.entity.Medicine;

public interface MedicineRepository extends PagingAndSortingRepository<Medicine,Integer>{
	//Write the appropriate methods if required
}
