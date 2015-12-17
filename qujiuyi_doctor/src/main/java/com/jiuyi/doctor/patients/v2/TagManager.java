package com.jiuyi.doctor.patients.v2;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.patients.v2.model.DoctorTags;
import com.jiuyi.doctor.patients.v2.model.Patient;
import com.jiuyi.doctor.patients.v2.model.Tag;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.ManagerBase;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.util.CollectionUtil;
import com.jiuyi.frame.util.StringUtil;

/**
 * @Author: xutaoyang @Date: 下午2:28:40
 *
 * @Description 医生的标签管理
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class TagManager extends ManagerBase<Doctor, DoctorTags> {

	@Autowired
	private PatientDaoV2 dao;

	@Autowired
	private PatientManagerV2 manager;

	/** 所有标签 */
	protected ServerResult loadTags(Doctor doctor) {
		DoctorTags doctorTags = loadInfoBase(doctor);
		ServerResult res = new ServerResult();
		res.putObjects("tags", doctorTags.getTags());

		/* 系统标签 */
		List<MapObject> sysTags = new ArrayList<>();
		int personalPatientCount = manager.getPersonalPatientCount(doctor);
		MapObject personalPatientTag = new MapObject();
		personalPatientTag.put("id", SystemTag.PERSONAL_TAG.id);
		personalPatientTag.put("name", SystemTag.PERSONAL_TAG.name);
		personalPatientTag.put("patientCount", personalPatientCount);
		sysTags.add(personalPatientTag);

		res.put("sys_tags", sysTags);
		return res;
	}

	/** 新增标签 */
	protected ServerResult addTag(Doctor doctor, String name) {
		if (StringUtil.isNullOrEmpty(name)) {
			return new ServerResult(ResultConst.PARAM_NULL);
		}
		DoctorTags doctorTags = loadInfoBase(doctor);
		if (doctorTags.nameExsit(name)) {
			return new ServerResult(ResultConst.NAME_EXIST);
		}
		int tagId = dao.addTag(doctor, name);
		Tag tag = new Tag(tagId, name, new Date());
		doctorTags.addTag(tag);
		return new ServerResult(tag);
	}

	/** 保存标签页信息，名称，患者列表 */
	protected ServerResult saveTag(Doctor doctor, Integer tagId, String name, List<Integer> patientIds) {
		if (StringUtil.isNullOrEmpty(name)) {
			return new ServerResult(ResultConst.PARAM_NULL);
		}
		DoctorTags doctorTags = loadInfoBase(doctor);
		if (!doctorTags.tagExsit(tagId)) {
			return new ServerResult(ResultConst.TAG_NOT_EXIST);
		} else if (doctorTags.nameDuplicate(tagId, name)) {
			return new ServerResult(ResultConst.NAME_EXIST);
		}

		/* 判断是否需要改名 */
		Tag tag = doctorTags.getTagById(tagId);
		if (!tag.getName().equals(name)) {
			tag.setName(name);
			dao.updateTagName(doctor, tagId, name);
		}

		/* 判断是否需要添加或者移除患者 */
		List<Integer> existPatientIds = tag.getPatientIds();
		if (existPatientIds.equals(patientIds)) {
			return new ServerResult();
		}
		List<Integer> toAdd = CollectionUtil.diff(patientIds, existPatientIds);
		List<Integer> toRemove = CollectionUtil.diff(existPatientIds, patientIds);
		if (!CollectionUtil.isNullOrEmpty(toAdd)) {
			tag.addPatients(toAdd);
			dao.addTagPatients(doctor, tagId, toAdd);
		}
		if (!CollectionUtil.isNullOrEmpty(toRemove)) {
			tag.removePatients(toRemove);
			dao.removeTagPatients(doctor, tagId, toRemove);
		}
		return new ServerResult();
	}

	/** 删除标签 */
	protected ServerResult deleteTag(Doctor doctor, Integer tagId) {
		DoctorTags doctorTags = loadInfoBase(doctor);
		if (!doctorTags.tagExsit(tagId)) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		doctorTags.removeTag(tagId);
		dao.removeTag(doctor, tagId);
		return new ServerResult();
	}

	/** 设置患者的标签 */
	protected ServerResult setPatientTags(Doctor doctor, Integer patientId, List<Integer> tagIds) {
		DoctorTags doctorTags = loadInfoBase(doctor);
		if (!doctorTags.getTagIds().containsAll(tagIds)) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		List<Tag> patientTags = doctorTags.getTagsByPatientId(patientId);
		List<Integer> patientTagIds = new ArrayList<>();
		for (Tag tag : patientTags) {
			patientTagIds.add(tag.getId());
		}
		if (patientTagIds.equals(tagIds)) {// 没有修改
			return new ServerResult();
		}
		List<Integer> toRemove = CollectionUtil.diff(patientTagIds, tagIds);
		List<Integer> toAdd = CollectionUtil.diff(tagIds, patientTagIds);
		if (!toRemove.isEmpty()) {
			doctorTags.removePatientTags(doctor, patientId, toRemove);
			dao.removePatientTags(doctor, patientId, toRemove);
		}
		if (!toAdd.isEmpty()) {
			doctorTags.addPatientTags(doctor, patientId, toAdd);
			dao.addPatientTags(doctor, patientId, toAdd);
		}
		return new ServerResult();
	}

	/** 获取指定标签下的患者列表 */
	protected ServerResult loadPatientsByTag(Doctor doctor, Integer tagId) {
		if (tagId == SystemTag.PERSONAL_TAG.id) {
			return new ServerResult("list", dao.loadSimplePersonal(doctor), true);
		}
		DoctorTags doctorTags = loadInfoBase(doctor);
		Tag tag = doctorTags.getTagById(tagId);
		if (tag == null) {
			return new ServerResult(ResultConst.TAG_NOT_EXIST);
		}
		List<Integer> patientIds = tag.getPatientIds();
		List<Patient> patients = CollectionUtil.isNullOrEmpty(patientIds) ? new ArrayList<Patient>() : dao.loadSimplePatientByTag(doctor, tagId);
		return new ServerResult("list", patients, true);
	}

	protected List<Tag> loadPatientTags(Doctor doctor, Integer patientId) {
		DoctorTags doctorTags = loadInfoBase(doctor);
		return doctorTags.getTagsByPatientId(patientId);
	}

	@Override
	protected DoctorTags constructInfo(Doctor doctor) {
		List<Tag> tags = dao.loadTags(doctor);
		if (!CollectionUtil.isNullOrEmpty(tags)) {
			List<Integer> tagIds = new ArrayList<>(tags.size());
			for (Tag tag : tags) {
				tagIds.add(tag.getId());
			}
			Map<Integer, List<Integer>> tagId_patientIds = dao.loadTagPatients(tagIds);
			for (Tag tag : tags) {
				tag.setPatientIds(tagId_patientIds.get(tag.getId()));
			}
		}
		return new DoctorTags(tags);
	}

	/**
	 * @param doctor
	 * @param phone
	 * @return
	 */
	protected ServerResult loadPatientsByPhone(Doctor doctor, String phone) {
		ServerResult res = new ServerResult();
		Patient patient = dao.loadPatientByPhone(doctor, phone);
		res.putObject(patient);
		return res;
	}

}
