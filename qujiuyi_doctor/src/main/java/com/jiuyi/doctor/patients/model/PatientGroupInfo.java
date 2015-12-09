package com.jiuyi.doctor.patients.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jiuyi.doctor.user.model.Doctor;

/**
 * @Author: xutaoyang @Date: 下午6:34:04
 *
 * @Description 医生关于患者分组的信息
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class PatientGroupInfo {

	public Doctor doctor;
	public List<PatientGroup> groups;
	public Map<Integer, List<Integer>> patientId_groups;

	public PatientGroupInfo(Doctor doctor, List<PatientGroup> groups, Map<Integer, List<Integer>> patientId_groups) {
		this.doctor = doctor;
		this.groups = groups;
		this.patientId_groups = patientId_groups;
	}

	public boolean ownGroup(int groupId) {
		for (PatientGroup patientGroup : groups) {
			if (patientGroup.getGroupId() == groupId) {
				return true;
			}
		}
		return false;
	}

	public void addGroup(PatientGroup patientGroup) {
		this.groups.add(patientGroup);
	}

	public void deleteGroups(Integer[] groupIds) {
		for (Integer groupId : groupIds) {
			for (Iterator<PatientGroup> iter = groups.listIterator(); iter.hasNext();) {
				if (iter.next().getGroupId() == groupId) {
					iter.remove();
					break;
				}
			}
		}
		for (Iterator<List<Integer>> iter = patientId_groups.values().iterator(); iter.hasNext();) {
			iter.next().removeAll(Arrays.asList(groupIds));
		}
	}

	public void moveInGroup(int patientId, int groupId) {
		getPatientInGroup(patientId).add((Integer) groupId);
	}

	public void moveOutGroup(int patientId, int groupId) {
		getPatientInGroup(patientId).remove((Integer) groupId);
	}

	public List<Integer> getPatientInGroup(int patientId) {
		List<Integer> patientInGroup = this.patientId_groups.get(patientId);
		if (patientInGroup == null) {
			patientInGroup = new ArrayList<Integer>();
			this.patientId_groups.put(patientId, patientInGroup);
		}
		return patientInGroup;
	}

	public void updateGroup(int groupId, String newGroupName) {
		for (PatientGroup patientGroup : groups) {
			if (patientGroup.getGroupId() == groupId) {
				patientGroup.setGroupName(newGroupName);
				return;
			}
		}
	}
}