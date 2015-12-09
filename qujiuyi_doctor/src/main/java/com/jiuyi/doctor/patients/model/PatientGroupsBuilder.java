package com.jiuyi.doctor.patients.model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * @Author: xutaoyang @Date: 下午7:53:47
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class PatientGroupsBuilder implements ResultSetExtractor<Map<Integer, List<Integer>>> {

	@Override
	public Map<Integer, List<Integer>> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<Integer, List<Integer>> res = new HashMap<Integer, List<Integer>>();
		while (rs.next()) {
			int patientId = rs.getInt("patientId");
			int groupId = rs.getInt("groupId");
			putToMap(res, patientId, groupId);
		}
		return res;
	}

	private void putToMap(Map<Integer, List<Integer>> map, int patientId, int groupId) {
		if (!map.containsKey(patientId)) {
			map.put(patientId, new ArrayList<Integer>());
		}
		map.get(patientId).add(groupId);
	}
}
