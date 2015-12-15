/**
 * 
 */
package com.jiuyi.doctor.prescription;

/**
 * 
 * @author xutaoyang
 *
 */
public enum PrescriptionStatus {
	
	/**医生发起/等待患者回应*/
	CREATED,
	
	/**等待开方/患者已确认*/
	PATIENT_CONFIRMED,
	
	/**医生完成/患者取消*/
	PATIENT_CANCEL,
	
	/**医生开方完成/患者待支付*/
	PRESCRIBED,
	
	/**医生取消开方/患者完成*/
	CANCEL_PRESCRIBE,
	
	/**医生完成/患者取消支付*/
	CANCEL_PAY,
	
	/**医生完成/患者完成支付*/
	PAYEDM,
	
	/**过期*/
	EXPIRED
	
}
