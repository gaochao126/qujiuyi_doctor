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
	public Integer acceptCount = 0;
	public Integer totalCount = 0;

	public void addData(Integer satisfaction, int acceptStatus) {
		totalCount++;
		acceptCount = acceptStatus == 1 ? acceptCount + 1 : acceptCount;

		// 默认为满意（1:一般 2:满意3:非常满意）
		satisfaction = satisfaction == 0 ? 2 : satisfaction;
		Integer scoreCount = score_count.get(satisfaction);
		scoreCount = scoreCount == null ? 0 : scoreCount;
		score_count.put(satisfaction, scoreCount + 1);
	}

	/** 患者对医生的评分 */
	public int score() {
		return totalCount == 0 ? 0 : (int) ((float) (getScoreCount(3) * 10 + getScoreCount(2) * 7 + getScoreCount(1) * 3) / totalCount);
	}

	/** 最终推荐指数=患者评分+接单率分数 */
	public int recommendScore() {
		return totalCount == 0 ? 80 : score() + (int) (((float) acceptCount / totalCount) * 10) + 80;
	}

	private int getScoreCount(Integer satisfaction) {
		Integer count = score_count.get(satisfaction);
		return count == null ? 0 : count;
	}
}
