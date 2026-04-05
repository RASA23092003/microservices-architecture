package com.infy.entity;

public class OrderedMedicine {
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
