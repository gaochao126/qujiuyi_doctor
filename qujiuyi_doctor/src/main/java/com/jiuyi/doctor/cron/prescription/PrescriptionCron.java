/**
 * 
 */
package com.jiuyi.doctor.cron.prescription;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jiuyi.doctor.cron.CronTime;
import com.jiuyi.frame.base.DbBase;

/**
 * 处方相关job
 * 
 * @author xutaoyang
 *
 */
@Component
public class PrescriptionCron extends DbBase {

	private static final String EXPIRE_PRESCRIPTION = "UPDATE `t_prescription` SET `status`=7 WHERE `status` IN (0,1,3) AND unix_timestamp(now())>unix_timestamp(createTime)+?";

	private static final int EXPIRE_TIME = 60 * 60 * 48;// 48小时，unix_timestamp的单位是秒

	@Scheduled(cron = CronTime.EXPIRE_PRESCRIPTION)
	public void expirePrescription() {
		jdbc.update(EXPIRE_PRESCRIPTION, EXPIRE_TIME);// 单位是秒
	}

}
