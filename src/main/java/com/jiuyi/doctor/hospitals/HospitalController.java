package com.jiuyi.doctor.hospitals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 下午4:38:32
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@RestController
public class HospitalController extends ControllerBase {

	private static final String CMD = "hospital_";
	private static final String CMD_ALL = "hospital_all";
	private static final String CMD_HOSPITAL_LEVEL1 = "hospital_level1";
	private static final String CMD_HOSPITAL_LEVEL2 = "hospital_level2";
	private static final String CMD_DEPARTMENT = "hospital_department";
	private static final String CMD_TITLE = "hospital_title";

	@Autowired
	HospitalManager manager;

	public HospitalController() throws Exception {
		super(CMD);
	}

	@RequestMapping(CMD_ALL)
	public ServerResult loadHospitals(/* @TokenUser @Permission(DoctorStatus.NEED_AUTH) Doctor doctor */) {
		return manager.loadAllHospital();
	}

	@RequestMapping(CMD_DEPARTMENT)
	public ServerResult handleDocReg(/* @TokenUser @Permission(DoctorStatus.NEED_AUTH) Doctor doctor */) {
		return manager.loadAllDepartment();
	}

	@RequestMapping(CMD_TITLE)
	public ServerResult handleLoadTitles(/* @TokenUser @Permission(DoctorStatus.NEED_AUTH) Doctor doctor */) {
		return manager.loadAllTitles();
	}

	@RequestMapping(CMD_HOSPITAL_LEVEL1)
	public ServerResult loadHospitalLevel1(/* @TokenUser @Permission(DoctorStatus.NEED_AUTH) Doctor doctor */) {
		return manager.loadHospitalLevel1();
	}

	@RequestMapping(CMD_HOSPITAL_LEVEL2)
	public ServerResult loadHospitalLevel2(/* @TokenUser @Permission(DoctorStatus.NEED_AUTH) Doctor doctor, */ @Param("level1") Integer level1Id) {
		return manager.loadHospitalLevel2(level1Id);
	}

}
