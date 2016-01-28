package com.jiuyi.doctor.hospitals;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.hospitals.model.Department;
import com.jiuyi.frame.util.CollectionUtil;

/**
 * @Author: xutaoyang @Date: 下午4:34:09
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class DepartmentService {

	@Autowired
	DepartmentDao dao;

	private AtomicInteger currentMaxId = new AtomicInteger(0);
	private ConcurrentHashMap<Integer, Department> id_info = new ConcurrentHashMap<Integer, Department>();
	private ConcurrentHashMap<String, Department> name_info = new ConcurrentHashMap<String, Department>();
	private CopyOnWriteArrayList<Department> departmentTree = new CopyOnWriteArrayList<>();

	@PostConstruct
	public void init() {
		this.currentMaxId = new AtomicInteger(dao.loadMaxId());
		List<Department> departments = dao.loadAll();
		CopyOnWriteArrayList<Department> temp = new CopyOnWriteArrayList<>();
		ConcurrentHashMap<Integer, Department> temp_id_info = new ConcurrentHashMap<Integer, Department>();
		ConcurrentHashMap<String, Department> temp_name_info = new ConcurrentHashMap<String, Department>();
		if (!CollectionUtil.isNullOrEmpty(departments)) {
			for (Department department : departments) {
				temp_id_info.put(department.getId(), department);
				temp_name_info.put(department.getName(), department);
			}
			for (Department department : departments) {
				if (department.isRoot()) {
					temp.add(department);
					Department all = new Department(department.getId(), department.getName(), department.getId());
					department.addChild(all);
				} else {
					temp_id_info.get(department.getParentId()).addChild(department);
				}
			}
		}
		this.departmentTree = temp;
		this.id_info = temp_id_info;
		this.name_info = temp_name_info;
	}

	public int getIdByName(String name) {
		if (!this.name_info.containsKey(name)) {
			int newId = genId();
			Department department = new Department(newId, name, 13);// 13表示其他科室
			department = this.id_info.putIfAbsent(newId, department);
			department = this.name_info.putIfAbsent(name, department);
			dao.newDepartment(department);
			return newId;
		}
		return this.name_info.get(name).getId();
	}

	public String getNameById(int id) {
		if (!this.id_info.containsKey(id)) {
			return "";
		}
		return this.id_info.get(id).getName();
	}

	public boolean checkName(String name) {
		Department department = this.name_info.get(name);
		return department == null ? false : !department.isRoot();
	}

	public boolean checkId(Integer departmentId) {
		return this.id_info.containsKey(departmentId);
	}

	public Integer genId() {
		return currentMaxId.incrementAndGet();
	}

	public List<Department> getDepartmentTree() {
		return this.departmentTree;
	}

	/***
	 * 线下科室对应的线上科室id
	 * 
	 * @param departmentId
	 * @return
	 */
	public Department getStandardDepartmentId(int hospitalId, int departmentId) {
		return dao.loadStandard(hospitalId, departmentId);
	}
}
