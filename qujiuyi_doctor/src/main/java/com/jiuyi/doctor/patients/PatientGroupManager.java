package com.jiuyi.doctor.patients;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.patients.model.PatientGroup;
import com.jiuyi.doctor.patients.model.PatientGroupInfo;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.ManagerBase;

/**
 * @Author: xutaoyang @Date: 下午6:38:53
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class PatientGroupManager extends ManagerBase<Doctor, PatientGroupInfo> {

	@Autowired
	PatientDAO dao;

	public static final int COMMON_GROUP = 1;// 医生自己建的组
	public static final int SYSTEM_GROUP = 2;// 系统分组

	public static final int TYPE_COMMON = 1;// 普通患者
	public static final int TYPE_PRIVATE = 2;// 私人患者
	private List<PatientGroup> systemGroups;

	@PostConstruct
	public void init() {
		this.systemGroups = new ArrayList<PatientGroup>(SystemGroup.values().length);
		for (SystemGroup sys_grp : SystemGroup.values()) {
			this.systemGroups.add(new PatientGroup(sys_grp.id, sys_grp.name, SYSTEM_GROUP));
		}
	}

	/** 分组是否属于该医生 */
	protected boolean ownGroup(Doctor doctor, int groupId) {
		PatientGroupInfo groupInfo = loadInfoBase(doctor);
		return groupInfo.ownGroup(groupId);
	}

	/** 患者是否在某个分组中 */
	protected boolean patientExistInGroup(Doctor doctor, int groupId, int patientId) {
		List<Integer> patientGroupList = loadPatientGroupList(doctor, patientId);
		return patientGroupList.contains((Integer) groupId);
	}

	/** 某个患者所在的分组 */
	protected List<Integer> loadPatientGroupList(Doctor doctor, int patientId) {
		PatientGroupInfo patientGroupInfo = loadInfoBase(doctor);
		return patientGroupInfo.getPatientInGroup(patientId);
	}

	/** 该医生所有的患者分组 */
	protected List<PatientGroup> loadPatientGroup(Doctor doctor) {
		PatientGroupInfo patientGroupInfo = loadInfoBase(doctor);
		return patientGroupInfo.groups;
	}

	/** 获取患者所在的分组 */
	protected Map<Integer, List<Integer>> loadPatientInGroup(Doctor doctor) {
		PatientGroupInfo patientGroupInfo = loadInfoBase(doctor);
		return patientGroupInfo.patientId_groups;
	}

	/** 添加患者到分组 */
	protected void moveInGroup(Doctor doctor, int patientId, int groupId) {
		PatientGroupInfo patientGroupInfo = loadInfoBase(doctor);
		patientGroupInfo.moveInGroup(patientId, groupId);
	}

	/** 把患者从分组中移除 */
	protected void moveOutGroup(Doctor doctor, int patientId, int groupId) {
		PatientGroupInfo patientGroupInfo = loadInfoBase(doctor);
		patientGroupInfo.moveOutGroup(patientId, groupId);
	}

	/** 批量移入分组 */
	protected void moveInBatch(Doctor doctor, Integer[] patientIds, int groupId) {
		for (Integer patientId : patientIds) {
			this.moveInGroup(doctor, patientId, groupId);
		}
	}

	/** 批量移除分组 */
	protected void moveOutBatch(Doctor doctor, Integer[] patientIds, int groupId) {
		for (Integer patientId : patientIds) {
			this.moveOutGroup(doctor, patientId, groupId);
		}
	}

	/** 增加一个分组 */
	protected void addGroup(Doctor doctor, PatientGroup patientGroup) {
		PatientGroupInfo patientGroupInfo = loadInfoBase(doctor);
		patientGroupInfo.addGroup(patientGroup);
	}

	/** 删除分组 */
	protected void deleteGroup(Doctor doctor, Integer[] groupIds) {
		PatientGroupInfo patientGroupInfo = loadInfoBase(doctor);
		patientGroupInfo.deleteGroups(groupIds);
	}

	/** 获取系统分组 */
	protected List<PatientGroup> getSystemGroup() {
		return this.systemGroups;
	}

	protected void updateGroup(Doctor doctor, int groupId, String newGroupName) {
		PatientGroupInfo patientGroupInfo = loadInfoBase(doctor);
		patientGroupInfo.updateGroup(groupId, newGroupName);
	}

	public enum SystemGroup {
		PRIVATE(1, "私人患者"), RETURN_VISIT(2, "需要随访患者");

		public final int id;
		public final String name;

		private SystemGroup(int id, String name) {
			this.id = id;
			this.name = name;
		}
	}

	@Override
	protected PatientGroupInfo constructInfo(Doctor doctor) {
		List<PatientGroup> groups = dao.loadGroup(doctor);
		groups.addAll(getSystemGroup());
		Map<Integer, List<Integer>> patientId_groups = dao.loadPatientGroup(doctor);
		PatientGroupInfo doctorGroupInfo = new PatientGroupInfo(doctor, groups, patientId_groups);
		return doctorGroupInfo;
	}
}
