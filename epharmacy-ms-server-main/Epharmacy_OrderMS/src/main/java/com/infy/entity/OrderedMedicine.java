package com.infy.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ORDERED_MEDICINE")
public class OrderedMedicine {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer orderedMedicineId;
	private Integer orderedQuantity;
	private Double orderSubtotal;
	private Integer medicineId;

	
	public Integer getOrderedMedicineId() {
		return orderedMedicineId;
	}
	public void setOrderedMedicineId(Integer orderedMedicineId) {
		this.orderedMedicineId = orderedMedicineId;
	}
	public Integer getOrderedQuantity() {
		return orderedQuantity;
	}
	public void setOrderedQuantity(Integer orderedQuantity) {
		this.orderedQuantity = orderedQuantity;
	}
	public Double getOrderSubtotal() {
		return orderSubtotal;
	}
	public void setOrderSubtotal(Double orderSubtotal) {
		this.orderSubtotal = orderSubtotal;
	}
	public Integer getMedicineId() {
		return medicineId;
	}
	public void setMedicineId(Integer medicineId) {
		this.medicineId = medicineId;
	}

	

}
