package com.jiuyi.doctor.patients;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.patients.interfaces.IRecommendPatient;
import com.jiuyi.doctor.patients.model.PatientGroup;
import com.jiuyi.doctor.patients.model.PatientInfo;
import com.jiuyi.doctor.patients.model.ReturnVisit;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.util.CollectionUtil;

/**
 * @Author: xutaoyang @Date: 下午6:08:08
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class PatientManager {

	@Autowired
	PatientDAO dao;

	@Autowired
	PatientGroupManager groupManager;

	@Autowired
	@Qualifier("RecommendPatientImpl")
	IRecommendPatient recommendPatient;

	/** 获取所有患者信息 */
	public ServerResult loadAllPatients(Doctor doctor) {
		ServerResult res = new ServerResult();
		List<PatientInfo> patients = new ArrayList<>();
		List<PatientGroup> groups = groupManager.loadPatientGroup(doctor);// 医生的分组信息
		List<PatientInfo> commonPatients = dao.loadCommonPatients(doctor);// 普通患者
		List<PatientInfo> personalPatients = dao.loadPersonalPatients(doctor);// 私人患者
		removePersonalInCommon(commonPatients, personalPatients);
		Map<Integer, List<Integer>> patientId_groups = groupManager.loadPatientInGroup(doctor);
		Map<Integer, List<ReturnVisit>> patientId_returnVisit = dao.loadPatientReturnVisit(doctor);
		patients.addAll(commonPatients);
		patients.addAll(personalPatients);
		for (PatientInfo patient : patients) {
			patient.setGroupIds(patientId_groups.get(patient.getPatientId()));
			patient.setReturnVisits(patientId_returnVisit.get(patient.getPatientId()));
		}
		res.putObjects("patients", patients);
		res.put("grps", groups);
		return res;
	}

	public ServerResult loadPatient(Doctor doctor, int patientId) {
		PatientInfo patient = dao.loadPatient(patientId);
		boolean isPersonalPatient = dao.isPersonalPatient(doctor.getId(), patientId);
		List<Integer> groupList = groupManager.loadPatientGroupList(doctor, patientId);
		patient.setGroupIds(groupList);
		patient.setType(isPersonalPatient ? PatientGroupManager.TYPE_PRIVATE : PatientGroupManager.TYPE_COMMON);
		return new ServerResult("patient", patient);
	}

	/** 搜索患者信息 */
	protected ServerResult searchPatient(Doctor doctor, String key) {
		ServerResult res = new ServerResult();
		List<PatientInfo> searchResults = dao.searchPatient(key);
		List<MapObject> list = new ArrayList<MapObject>();
		if (CollectionUtil.isNullOrEmpty(searchResults)) {
			res.putResult(ResultConst.NOT_FOUND);
			return res;
		}
		for (PatientInfo patient : searchResults) {
			list.add(patient.toSimpleInfo());
		}
		res.put("list", list);
		return res;
	}

	/** 把患者加到患者群 */
	protected ServerResult addPatient(Doctor doctor, int patientId) {
		if (dao.hasPatient(doctor, patientId)) {
			return new ServerResult(ResultConst.HAVE_IN_PATIENTS);
		} else if (!dao.patientExist(patientId)) {
			return new ServerResult(ResultConst.PATIENT_NOT_EXIST);
		}
		dao.addPatient(doctor, patientId);
		ServerResult res = loadPatient(doctor, patientId);
		return res;
	}

	/** 获取分组信息 */
	public ServerResult loadGroup(Doctor doctor) {
		List<PatientGroup> groups = groupManager.loadPatientGroup(doctor);
		return new ServerResult("grps", groups);
	}

	/** 新增分组 */
	public ServerResult addGroup(Doctor doctor, String groupName) {
		int groupId = dao.addGroup(doctor, groupName);
		PatientGroup patientGroup = new PatientGroup(groupId, groupName);
		groupManager.addGroup(doctor, patientGroup);
		ServerResult res = new ServerResult();
		res.put("groupId", groupId);
		res.put("groupName", groupName);
		return res;
	}

	/** 删除分组 */
	public ServerResult deleteGroup(Doctor doctor, Integer[] groupIds) {
		groupManager.deleteGroup(doctor, groupIds);
		dao.deleteGroup(doctor, groupIds);
		return new ServerResult();
	}

	/** 更新分组 */
	public ServerResult updateGroup(Doctor doctor, int groupId, String newGroupName) {
		groupManager.updateGroup(doctor, groupId, newGroupName);
		dao.updateGroup(doctor, groupId, newGroupName);
		ServerResult res = new ServerResult();
		res.put("groupId", groupId);
		res.put("groupName", newGroupName);
		return res;
	}

	/** 把患者加到某个分组 */
	public ServerResult moveIn(Doctor doctor, int groupId, int patientId) {
		if (groupManager.patientExistInGroup(doctor, groupId, patientId)) {
			return new ServerResult(ResultConst.PATIENT_EXIST_IN_GROUP);
		} else if (!groupManager.ownGroup(doctor, groupId)) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		groupManager.moveInGroup(doctor, patientId, groupId);
		dao.moveIn(doctor, groupId, patientId);
		return new ServerResult();
	}

	/** 把患者从某个分组移除 */
	public ServerResult moveOut(Doctor doctor, int groupId, int patientId) {
		if (!groupManager.patientExistInGroup(doctor, groupId, patientId)) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		} else if (!groupManager.ownGroup(doctor, groupId)) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		groupManager.moveOutGroup(doctor, patientId, groupId);
		dao.moveOut(doctor, groupId, patientId);
		return new ServerResult();
	}

	/** 把患者加到某个分组 */
	public ServerResult moveInBatch(Doctor doctor, int groupId, Integer[] patientIds) {
		groupManager.moveInBatch(doctor, patientIds, groupId);
		dao.moveInBatch(doctor, groupId, patientIds);
		return new ServerResult();
	}

	/** 把患者从某个分组移除 */
	public ServerResult moveOutBatch(Doctor doctor, int groupId, Integer[] patientIds) {
		groupManager.moveOutBatch(doctor, patientIds, groupId);
		dao.moveOutBatch(doctor, groupId, patientIds);
		return new ServerResult();
	}

	/** 添加回访提醒 */
	public ServerResult addReturnVisit(Doctor doctor, int patientId, Date visitDate, String desc) {
		int newId = dao.addReturnVisit(doctor, patientId, visitDate, desc);
		return new ServerResult("id", newId);
	}

	/** 删除回访提醒 */
	public ServerResult delReturnVisit(Doctor doctor, int id) {
		dao.delReturnVisit(doctor, id);
		return new ServerResult();
	}

	/** 推荐用户 */
	protected ServerResult recommendPatient(Doctor doctor) {
		List<PatientInfo> patients = recommendPatient.recommendPatient(doctor);
		List<MapObject> rcmds = new ArrayList<>();
		for (PatientInfo patient : patients) {
			rcmds.add(patient.toSimpleInfo());
		}
		return new ServerResult("rcmds", rcmds);
	}

	// ===private function===
	/** 因为普通患者和私人患者会有重复的，这样前端展示的时候有两个相同的患者。所以如果是私人患者，则在普通患者中移除 */
	private void removePersonalInCommon(List<PatientInfo> commonPatients, List<PatientInfo> personalPatients) {
		for (PatientInfo pp : personalPatients) {
			for (Iterator<PatientInfo> iter = commonPatients.listIterator(); iter.hasNext();) {
				PatientInfo cp = iter.next();
				if (cp.getPatientId() == pp.getPatientId()) {
					iter.remove();
				}
			}
		}
	}
}
