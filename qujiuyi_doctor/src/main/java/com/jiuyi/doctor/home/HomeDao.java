package com.jiuyi.doctor.home;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.home.model.AdBanner;
import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 下午2:47:29
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class HomeDao extends DbBase {

	private static final String SELECT_AD_BANNER = "SELECT * FROM `t_ad` WHERE `type`=1 order by `sort`";

	protected List<AdBanner> loadAdBanners() {
		return jdbc.query(SELECT_AD_BANNER, AdBanner.builder);
	}
}
