/**
 * 
 */
package com.jiuyi.doctor.prescription;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * @author xutaoyang
 *
 */
public class PrescriptionMedicine {

	private long id;
	private String prescriptionId;
	@NotEmpty
	private String medicineId;
	@NotEmpty
	private String formatId;
	@NotNull
	@Min(1)
	private Integer number;
	@NotEmpty
	private String instructions;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPrescriptionId() {
		return prescriptionId;
	}

	public String getMedicineId() {
		return medicineId;
	}

	public String getFormatId() {
		return formatId;
	}

	public Integer getNumber() {
		return number;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setPrescriptionId(String prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	public void setMedicineId(String medicineId) {
		this.medicineId = medicineId;
	}

	public void setFormatId(String formatId) {
		this.formatId = formatId;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

}
