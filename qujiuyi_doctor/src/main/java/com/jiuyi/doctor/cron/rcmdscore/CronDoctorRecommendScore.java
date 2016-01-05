package com.jiuyi.doctor.cron.rcmdscore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jiuyi.doctor.cron.CronTime;

/**
 * @Author: xutaoyang @Date: 下午4:50:27
 *
 * @Description 每天晚上计算医生的推荐指数
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Component
public class CronDoctorRecommendScore {

	@Autowired
	DoctorRecommendDao dao;

	@Scheduled(cron = CronTime.DOCTOR_RECOMMEND_SCORE)
	public void runJob() {
		Map<Integer, RecommendScoreInfo> doctorId_score = dao.loadScoreInfo();
		List<Object[]> updateArgs = new ArrayList<>(doctorId_score.size());
		for (Entry<Integer, RecommendScoreInfo> entry : doctorId_score.entrySet()) {
			updateArgs.add(new Object[] { entry.getValue().recommendScore(), entry.getKey() });
			dao.updateRecommendScore(updateArgs);
		}
	}
}
