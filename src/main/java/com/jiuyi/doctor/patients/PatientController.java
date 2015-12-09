package com.jiuyi.doctor.patients;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 下午6:01:19
 *
 * @Description 医生端，各种关于患者群的维护
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Controller
public class PatientController extends ControllerBase {

	@Autowired
	PatientManager manager;

	public PatientController() throws Exception {
		super(CMD_PRE);
	}

	private static final String CMD_PRE = "/patient_";
	private static final String CMD_LOAD_PATIENT = CMD_PRE + "info";// 获取患者信息
	private static final String CMD_LOAD_ALL_PATIENT = CMD_PRE + "all";// 获取医生的患者群
	private static final String CMD_SEARCH_PATIENT = CMD_PRE + "search"; // 搜索
	private static final String CMD_ADD_PATIENT = CMD_PRE + "add";// 添加患者到患者群
	private static final String CMD_LOAD_GROUP = CMD_PRE + "load_group";// 获取所有的分组
	private static final String CMD_ADD_GROUP = CMD_PRE + "add_group";// 新增一个分组
	private static final String CMD_DELETE_GROUP = CMD_PRE + "del_group";// 删除分组
	private static final String CMD_UPDATE_GROUP = CMD_PRE + "update_group";// 更新分组名称
	private static final String CMD_MOVE_IN_BATCH = CMD_PRE + "move_in_batch";// 批量把用户加到某个组
	private static final String CMD_MOVE_OUT_BATCH = CMD_PRE + "move_out_batch"; // 批量把用户从某个组中移除;
	private static final String CMD_MOVE_IN = CMD_PRE + "move_in";// 把用户加到某个组
	private static final String CMD_MOVE_OUT = CMD_PRE + "move_out"; // 把用户从某个组中移除;

	private static final String CMD_ADD_RETURN_VISIT = CMD_PRE + "add_rtn_vst";// 添加回访提醒
	private static final String CMD_DEL_RETURN_VISIT = CMD_PRE + "del_rtn_vst";// 移除回访提醒

	private static final String CMD_RECOMMEND_PATIENT = CMD_PRE + "recommend";

	@RequestMapping(CMD_LOAD_ALL_PATIENT)
	@ResponseBody
	public ServerResult loadAllPatients(@TokenUser Doctor doctor) {
		return manager.loadAllPatients(doctor);
	}

	@RequestMapping(CMD_LOAD_PATIENT)
	@ResponseBody
	public ServerResult loadPatient(@TokenUser Doctor doctor, @Param("patientId") int patientId) {
		return manager.loadPatient(doctor, patientId);
	}

	@RequestMapping(CMD_SEARCH_PATIENT)
	@ResponseBody
	public ServerResult searchPatient(@TokenUser Doctor doctor, @Param("key") String key) {
		return manager.searchPatient(doctor, key);
	}

	@RequestMapping(CMD_ADD_PATIENT)
	@ResponseBody
	public ServerResult addPatient(@TokenUser Doctor doctor, @Param("patientId") int patientId) {
		return manager.addPatient(doctor, patientId);
	}

	@RequestMapping(CMD_LOAD_GROUP)
	@ResponseBody
	public ServerResult loadGroup(@TokenUser Doctor doctor) {
		return manager.loadGroup(doctor);
	}

	@RequestMapping(CMD_ADD_GROUP)
	@ResponseBody
	public ServerResult addGroup(@TokenUser Doctor doctor, @Param("groupName") String groupName) {
		return manager.addGroup(doctor, groupName);
	}

	@RequestMapping(CMD_DELETE_GROUP)
	@ResponseBody
	public ServerResult deleteGroup(@TokenUser Doctor doctor, @Param("groupId") Integer[] groupId) {
		return manager.deleteGroup(doctor, groupId);
	}

	@RequestMapping(CMD_UPDATE_GROUP)
	@ResponseBody
	public ServerResult updateGroup(@TokenUser Doctor doctor, @Param("groupId") int groupId, @Param("newGroupName") String newGroupName) {
		return manager.updateGroup(doctor, groupId, newGroupName);
	}

	@RequestMapping(CMD_MOVE_IN)
	@ResponseBody
	public ServerResult moveIn(@TokenUser Doctor doctor, @Param("groupId") int groupId, @Param("patientId") int patientId) {
		return manager.moveIn(doctor, groupId, patientId);
	}

	@RequestMapping(CMD_MOVE_OUT)
	@ResponseBody
	public ServerResult moveOut(@TokenUser Doctor doctor, @Param("groupId") int groupId, @Param("patientId") int patientId) {
		return manager.moveOut(doctor, groupId, patientId);
	}

	@RequestMapping(CMD_MOVE_IN_BATCH)
	@ResponseBody
	public ServerResult moveInBatch(@TokenUser Doctor doctor, @Param("groupId") int groupId, @Param("patientId") Integer[] patientId) {
		return manager.moveInBatch(doctor, groupId, patientId);
	}

	@RequestMapping(CMD_MOVE_OUT_BATCH)
	@ResponseBody
	public ServerResult moveOutBatch(@TokenUser Doctor doctor, int groupId, @Param("patientId") Integer[] patientId) {
		return manager.moveOutBatch(doctor, groupId, patientId);
	}

	@RequestMapping(CMD_ADD_RETURN_VISIT)
	@ResponseBody
	public ServerResult addReturnVisit(@TokenUser Doctor doctor, @Param("patientId") int patientId, @Param("visitDate") Date visitDate, @Param("desc") String desc) {
		return manager.addReturnVisit(doctor, patientId, visitDate, desc);
	}

	@RequestMapping(CMD_DEL_RETURN_VISIT)
	@ResponseBody
	public ServerResult delReturnVisit(@TokenUser Doctor doctor, @Param("id") int id) {
		return manager.delReturnVisit(doctor, id);
	}

	@RequestMapping(CMD_RECOMMEND_PATIENT)
	@ResponseBody
	public ServerResult recommendPatient(@TokenUser Doctor doctor) {
		return manager.recommendPatient(doctor);
	}

}
