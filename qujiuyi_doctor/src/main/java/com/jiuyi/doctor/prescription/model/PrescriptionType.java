/**
 * 
 */
package com.jiuyi.doctor.prescription.model;

/**
 * 
 * @author xutaoyang
 *
 */
public enum PrescriptionType {

	COMMON("普通药品处方");

	public final String name;

	/**
	 * 
	 */
	private PrescriptionType(String name) {
		this.name = name;
	}

	public static String getNameById(int type) {
		if (type < 0 || type > values().length - 1) {
			return null;
		}
		return values()[type].name;
	}
}
