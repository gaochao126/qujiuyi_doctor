package com.jiuyi.doctor.cron.personaldoctor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jiuyi.doctor.cron.CronTime;
import com.jiuyi.frame.util.CollectionUtil;

/**
 * @Author: xutaoyang @Date: 下午7:59:40
 *
 * @Description 私人医生服务结束后一段时间，医生收款
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Component
public class PersonalDoctorFinish {

	private volatile boolean isRunning = false;
	private static final int DELAY = 3600;// 一个小时，单位为秒

	@Autowired
	PersonalDoctorFinishDao dao;

	@Scheduled(cron = CronTime.PERSONAL_DOCTOR_INCOMING_TO_BALANCE)
	public void incoming2Balance() {
		if (isRunning) { // 为了防止上一次还没有运行完毕
			return;
		}
		isRunning = true;
		List<OrderInfo> toHandle = dao.loadSatisfyOrder(DELAY);
		if (CollectionUtil.isNullOrEmpty(toHandle)) {
			return;
		}
		dao.handleOrder(toHandle);
		isRunning = false;
	}

}
