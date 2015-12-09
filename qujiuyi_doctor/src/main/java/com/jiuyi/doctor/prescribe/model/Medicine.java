package com.jiuyi.doctor.prescribe.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 上午11:03:41
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class Medicine implements ISerializableObj {

	public final String code;
	public final String name;
	public final String image;
	public final BigDecimal price;
	public final int quantity;
	public final String unit;

	public static final RowMapper<Medicine> builder = new RowMapper<Medicine>() {
		@Override
		public Medicine mapRow(ResultSet rs, int rowNum) throws SQLException {
			String code = rs.getString("code");
			String name = rs.getString("name");
			String image = rs.getString("image");
			BigDecimal price = rs.getBigDecimal("price");
			int quantity = rs.getInt("quantity");
			String unit = rs.getString("unit");
			return new Medicine(code, name, image, price, quantity, unit);
		}
	};
	public static final ResultSetExtractor<Map<Integer, List<Medicine>>> map_builder = new ResultSetExtractor<Map<Integer, List<Medicine>>>() {
		@Override
		public Map<Integer, List<Medicine>> extractData(ResultSet rs) throws SQLException, DataAccessException {
			Map<Integer, List<Medicine>> res = new HashMap<Integer, List<Medicine>>();
			int rowNum = 0;
			while (rs.next()) {
				Medicine medcine = builder.mapRow(rs, rowNum++);
				Integer prescribeId = rs.getInt("prescribeId");
				put2Map(res, prescribeId, medcine);
			}
			return res;
		}

		private void put2Map(Map<Integer, List<Medicine>> res, Integer prescribeId, Medicine medcine) {
			List<Medicine> medcines = res.get(prescribeId);
			if (medcines == null) {
				medcines = new ArrayList<Medicine>();
				res.put(prescribeId, medcines);
			}
			medcines.add(medcine);
		}

	};

	public Medicine(String code, String name, String image, BigDecimal price, int quantity, String unit) {
		super();
		this.code = code;
		this.name = name;
		this.image = image;
		this.price = price;
		this.quantity = quantity;
		this.unit = unit;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("code", this.code);
		res.put("name", this.name);
		res.put("image", this.image);
		res.put("price", this.price);
		res.put("quantity", this.quantity);
		res.put("unit", this.unit);
		return res;

	}

}
