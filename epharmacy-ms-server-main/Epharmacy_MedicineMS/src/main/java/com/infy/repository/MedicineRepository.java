package com.infy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.infy.entity.Medicine;

public interface MedicineRepository extends JpaRepository<Medicine,Integer>{
	//Write the appropriate methods if required
	@Query("Select m from Medicine m where m.category= :category")
	List<Medicine> findByCategory(@Param("category")String category);
	@Query("Select m from Medicine m where m.medicineName=:medicineName")
	Optional<Medicine> fingByName(@Param("medicineName")String medicineName);
}
