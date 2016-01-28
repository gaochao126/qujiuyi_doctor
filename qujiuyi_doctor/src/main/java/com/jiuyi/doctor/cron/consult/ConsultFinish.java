package com.jiuyi.doctor.cron.consult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jiuyi.doctor.chatserver.ChatServerRequestEntity;
import com.jiuyi.doctor.chatserver.ChatServerService;
import com.jiuyi.doctor.cron.CronTime;
import com.jiuyi.frame.conf.DBConfig;
import com.jiuyi.frame.helper.Loggers;
import com.jiuyi.frame.util.CollectionUtil;

/**
 * @Author: xutaoyang @Date: 下午8:00:15
 *
 * @Description 图文咨询结束后一段时间，医生收款
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Component
public class ConsultFinish {

	private volatile boolean running = false;
	private static final int DELAY = 3600;// 图文咨询结束多久后打款给医生
	private static final int EXPIRED_TIME = 3600;// 应该是3600秒，这里为了测试，把时间改长，单位秒哦

	private @Autowired ConsultFinishDao dao;

	private @Autowired ChatServerService chatServerService;

	private @Autowired DBConfig dbConfig;

	@Scheduled(cron = CronTime.CONSULT_INCOMING_TO_BALANCE)
	public void incoming2Balance() {
		if (running) { // 防止上一次的job还没跑完
			return;
		}
		running = true;
		try {
			// 干活
			List<ConsultInfo> consultList = dao.loadSatisfiedConsult(DELAY);
			if (CollectionUtil.isNullOrEmpty(consultList)) {
				return;
			}
			for (ConsultInfo consultInfo : consultList) {
				// 平台将会收取一定费用
				BigDecimal rate = new BigDecimal(dbConfig.getConfig("doctor.order.rates"));
				BigDecimal shouldIncome = consultInfo.getMoney().multiply(rate);
				consultInfo.setMoney(shouldIncome);
			}
			dao.handleConsult(consultList);
		} catch (Exception e) {
			Loggers.err("ConsultFinish incoming2Balance err", e);
		} finally {
			running = false;
		}

	}

	@Scheduled(cron = CronTime.CHECK_CONSULT)
	public void checkConsult() {
		// 干活
		List<ConsultOrder> consultOrders = dao.loadExpireConsult(EXPIRED_TIME);
		if (CollectionUtil.isNullOrEmpty(consultOrders)) {
			return;
		}
		dao.handleExpireConsult(consultOrders);
		// 发送通知
		List<Map<String, Object>> params = new ArrayList<>(consultOrders.size());
		for (ConsultOrder consult : consultOrders) {
			Map<String, Object> consultMap = new HashMap<>();
			consultMap.put("consultId", consult.consultId);
			consultMap.put("patientId", consult.patientId);
			consultMap.put("doctorId", consult.doctorId);
			consultMap.put("doctorName", consult.doctorName);
			params.add(consultMap);
		}
		ChatServerRequestEntity req = new ChatServerRequestEntity("expireConsult");
		req.putParam("params", params);
		chatServerService.postMsg(req);
	}

}
