package com.jiuyi.doctor.home.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/**
 * @Author: xutaoyang @Date: 下午7:58:07
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class AdBanner {

	public String adTitle;
	public String adImage;
	public String adUrl;
	public int sort;

	public AdBanner(String adTitle, String adImage, String adUrl, int sort) {
		this.adTitle = adTitle;
		this.adImage = adImage;
		this.adUrl = adUrl;
		this.sort = sort;
	}

	public static final RowMapper<AdBanner> builder = new RowMapper<AdBanner>() {
		@Override
		public AdBanner mapRow(ResultSet rs, int rowNum) throws SQLException {
			String adTitle = rs.getString("adTitle");
			String adImage = rs.getString("adImage");
			String adUrl = rs.getString("adUrl");
			int sort = rs.getInt("sort");
			return new AdBanner(adTitle, adImage, adUrl, sort);
		}
	};
}
