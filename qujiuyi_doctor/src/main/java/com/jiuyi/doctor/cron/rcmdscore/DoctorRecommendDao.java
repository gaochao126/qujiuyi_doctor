package com.jiuyi.doctor.cron.rcmdscore;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 下午5:50:10
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class DoctorRecommendDao extends DbBase {
	private static final String SELECT_CONSULT_INFO = "SELECT `doctorId`,`satisfaction`,`acceptStatus` FROM `t_patient_consult`";
	private static final String UPATE_RECOMMEND_SCORE = "UPDATE `t_doctor` SET `recommendScore`=?,`score`=? WHERE `id`=?";

	protected Map<Integer, RecommendScoreInfo> loadScoreInfo() {
		return jdbc.query(SELECT_CONSULT_INFO, new ResultSetHandler());
	}

	protected void updateRecommendScore(List<Object[]> updateArgs) {
		jdbc.batchUpdate(UPATE_RECOMMEND_SCORE, updateArgs);
	}

	class ResultSetHandler implements ResultSetExtractor<Map<Integer, RecommendScoreInfo>> {

		@Override
		public Map<Integer, RecommendScoreInfo> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Integer, RecommendScoreInfo> res = new HashMap<>();
			while (rs.next()) {
				int doctorId = rs.getInt("doctorId");
				Integer satisfaction = rs.getInt("satisfaction");// null的情况下为0
				int acceptStatus = rs.getInt("acceptStatus");
				RecommendScoreInfo scoreInfo = res.get(doctorId);
				if (scoreInfo == null) {
					scoreInfo = new RecommendScoreInfo();
					res.put(doctorId, scoreInfo);
				}
				scoreInfo.addData(satisfaction, acceptStatus);
			}
			return res;
		}
	}

}
