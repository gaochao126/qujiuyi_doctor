package com.jiuyi.doctor.patients.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jiuyi.doctor.user.model.Doctor;

/**
 * @Author: xutaoyang @Date: 下午2:29:45
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class DoctorTags {

	private List<Tag> tags = new ArrayList<>();

	public DoctorTags(List<Tag> tags) {
		this.tags = tags;
	}

	public List<Tag> getTags() {
		return tags;
	}

	public List<Integer> getTagIds() {
		List<Integer> res = new ArrayList<>(tags.size());
		for (Tag tag : tags) {
			res.add(tag.getId());
		}
		return res;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public boolean tagExsit(Integer tagId) {
		for (Tag tag : tags) {
			if (tag.getId() == tagId) {
				return true;
			}
		}
		return false;
	}

	public boolean nameExsit(String name) {
		for (Tag tag : tags) {
			if (tag.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	public void addTag(Tag tag) {
		this.tags.add(tag);
	}

	public void removeTag(Integer tagId) {
		for (Iterator<Tag> iter = tags.listIterator(); iter.hasNext();) {
			Tag tag = iter.next();
			if (tag.getId() == tagId) {
				iter.remove();
			}
		}
	}

	public Tag getTagById(Integer tagId) {
		for (Tag tag : tags) {
			if (tag.getId() == tagId) {
				return tag;
			}
		}
		return null;
	}

	public boolean nameDuplicate(Integer tagId, String name) {
		for (Tag tag : tags) {
			if (tag.getName().equals(name) && tag.getId() != tagId) {
				return true;
			}
		}
		return false;
	}

	public List<Tag> getTagsByPatientId(Integer patientId) {
		List<Tag> res = new ArrayList<>();
		for (Tag tag : tags) {
			if (tag.hasPatient(patientId)) {
				res.add(tag);
			}
		}
		return res;
	}

	/** 移除患者的指定tag */
	public void removePatientTags(Doctor doctor, Integer patientId, List<Integer> tagIds) {
		for (Integer tagId : tagIds) {
			Tag tag = getTagById(tagId);
			tag.removePatient(patientId);
		}
	}

	/** 给患者添加指定tag */
	public void addPatientTags(Doctor doctor, Integer patientId, List<Integer> tagIds) {
		for (Integer tagId : tagIds) {
			Tag tag = getTagById(tagId);
			tag.addPatient(patientId);
		}
	}
}
