package com.jiuyi.doctor.hospitals;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.hospitals.model.DoctorTitle;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.reload.IReloader;
import com.jiuyi.frame.reload.ReloadManager;

/**
 * @Author: xutaoyang @Date: 下午5:15:45
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class HospitalManager implements IReloader {

	@Autowired
	HospitalService hospitalService;

	@Autowired
	DepartmentService departmentService;

	@Autowired
	ReloadManager reloadManager;

	private List<MapObject> titleObj = new ArrayList<>();

	@PostConstruct
	public void init() {
		reloadManager.registerReloader("hospital", this);
		initTitles();
	}

	@Override
	public void reload() {
		initTitles();
		hospitalService.init();
		departmentService.init();
	}

	private void initTitles() {
		List<MapObject> temp = new ArrayList<>();
		for (DoctorTitle doctorTitle : DoctorTitle.values()) {
			MapObject id_name = new MapObject();
			id_name.put("id", doctorTitle.id);
			id_name.put("name", doctorTitle.name);
			temp.add(id_name);
		}
		this.titleObj = temp;
	}

	public ServerResult loadAllHospital() {
		return new ServerResult("hospitals", hospitalService.getAllHospitals());
	}

	public ServerResult loadAllDepartment() {
		return new ServerResult("list", departmentService.getDepartmentTree());
	}

	protected ServerResult loadHospitalLevel1() {
		return new ServerResult("list", hospitalService.loadHospitalLevel1());
	}

	protected ServerResult loadHospitalLevel2(Integer level1Id) {
		return new ServerResult("list", hospitalService.loadHospitalLevel2(level1Id));
	}

	protected ServerResult loadAllTitles() {
		return new ServerResult("titles", titleObj, true);
	}
}
