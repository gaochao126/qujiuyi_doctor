package com.jiuyi.doctor.yaofang;

import java.util.List;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class YaofangDao extends YaofangDB {

	private static final String SEARCH_MEDICINE = "SELECT * FROM `Products` WHERE LOWER(`prod_name`) LIKE :key;";

	public List<Medicine> searchMedicine(String key) {
		return namedJdbc.query(SEARCH_MEDICINE, new MapSqlParameterSource("key", "%" + key.toLowerCase() + "%"), mapperService.getRowMapper(Medicine.class));
	}

	
}
