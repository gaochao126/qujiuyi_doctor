/**
 * 
 */
package com.jiuyi.doctor.prescription;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 处方配药信息
 * 
 * @author xutaoyang
 *
 */
public class PrescriptionMedicine {

	private long id;

	private String prescriptionId;// 处方id
	@NotEmpty
	private String medicineId;// 药品id
	@NotEmpty
	private String formatId;// 规格id
	@NotNull
	@Min(1)
	private Integer number;// 数量
	@NotEmpty
	private String usage; // 用法

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

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

}
