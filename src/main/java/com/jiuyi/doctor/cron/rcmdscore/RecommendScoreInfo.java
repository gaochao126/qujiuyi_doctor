package com.jiuyi.doctor.cron.rcmdscore;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: xutaoyang @Date: 下午5:50:22
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class RecommendScoreInfo {
	public Map<Integer, Integer> score_count = new HashMap<>();
	private int total = 10;// 默认赠送10个好评~

	public void addData(Integer satisfaction, int count) {
		// 默认为满意（1:一般 2:满意3:非常满意）
		satisfaction = satisfaction == 0 ? 2 : satisfaction;
		score_count.put(satisfaction, count);
		total += count;
	}

	/** 最终推荐指数=患者评分+接单率分数 */
	public int recommendScore() {
		return total == 0 ? 100 : ((getScoreCount(3) + 10) * 100 + getScoreCount(2) * 90 + getScoreCount(1) * 20) / total;
	}

	private int getScoreCount(Integer satisfaction) {
		Integer count = score_count.get(satisfaction);
		return count == null ? 0 : count;
	}
}
