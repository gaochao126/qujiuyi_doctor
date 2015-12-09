package com.jiuyi.doctor.patients.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * @Author: xutaoyang @Date: 上午10:15:53
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class ReturnVisitBuilder implements ResultSetExtractor<Map<Integer, List<ReturnVisit>>> {

	@Override
	public Map<Integer, List<ReturnVisit>> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<Integer, List<ReturnVisit>> res = new HashMap<Integer, List<ReturnVisit>>();
		while (rs.next()) {
			int id = rs.getInt("id");
			int doctorId = rs.getInt("doctorId");
			int patientId = rs.getInt("patientId");
			Date visitDate = rs.getTimestamp("visitTime");
			String desc = rs.getString("description");
			ReturnVisit returnVisit = new ReturnVisit(id, doctorId, patientId, visitDate, desc);
			put2Map(res, returnVisit);
		}
		return res;
	}

	private void put2Map(Map<Integer, List<ReturnVisit>> map, ReturnVisit returnVisit) {
		int patientId = returnVisit.getPatientId();
		if (!map.containsKey(patientId)) {
			map.put(patientId, new ArrayList<ReturnVisit>());
		}
		map.get(patientId).add(returnVisit);
	}
}
