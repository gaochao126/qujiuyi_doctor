package com.jiuyi.doctor.hospitals;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.hospitals.model.Hospital;
import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 上午11:34:23
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class HospitalDao extends DbBase {
	private static final String INSERT_HOSPITAL = "INSERT `t_hospital`(`id`,`name`) VALUE(?,?)";
	private static final String SELECT = "SELECT h.id,h.name,h.city, c.cityName FROM `t_hospital` h,`t_city` c WHERE h.`city`=c.`cityId`";
	private static final String SELECT_MAX_ID = "SELECT MAX(id) FROM `t_hospital`";

	protected void newHospital(final Hospital hospital) {
		jdbc.update(INSERT_HOSPITAL, new Object[] { hospital.getId(), hospital.getName() });
	}

	protected List<Hospital> loadAll() {
		return jdbc.query(SELECT, Hospital.builder);
	}

	protected int loadMaxId() {
		return queryForInteger(SELECT_MAX_ID);
	}

}
