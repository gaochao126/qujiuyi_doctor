package com.jiuyi.doctor.hospitals;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.hospitals.model.Department;
import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 上午11:34:18
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class DepartmentDao extends DbBase {

	private static final String INSERT_DEPARTMENT = "INSERT `t_department`(`id`,`name`) VALUE(?,?)";
	private static final String SELECT = "SELECT * FROM `t_department`";
	private static final String SELECT_MAX_ID = "SELECT MAX(id) FROM `t_department`";
	private static final String SELECT_BY_HOSPITAL_DEP = "SELECT * FROM `t_department` WHERE `id`=(SELECT `standardNameId` FROM `t_hospital_department` WHERE `id`=?)";

	protected void newDepartment(final Department department) {
		jdbc.update(INSERT_DEPARTMENT, new Object[] { department.getId(), department.getName() });
	}

	protected List<Department> loadAll() {
		return jdbc.query(SELECT, Department.builder);
	}

	protected int loadMaxId() {
		return queryForInteger(SELECT_MAX_ID);
	}

	protected Department loadStandard(int hospitalId, int departmentId) {
		return queryForObject(SELECT_BY_HOSPITAL_DEP, new Object[] { departmentId }, Department.builder);
	}
}
