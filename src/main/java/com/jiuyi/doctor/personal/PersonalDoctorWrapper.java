package com.jiuyi.doctor.personal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiuyi.doctor.services.ServiceStatus;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;

/**
 * @Author: xutaoyang @Date: 上午11:46:11
 *
 * @Description 对医生私人服务配置的封装
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */

public class PersonalDoctorWrapper implements ISerializableObj {

	private List<PersonalDoctor> personalDoctorList;
	private Map<Integer, PersonalDoctor> type_info = new HashMap<>();

	public PersonalDoctorWrapper(List<PersonalDoctor> personalDoctorList) {
		personalDoctorList = personalDoctorList != null ? personalDoctorList : new ArrayList<PersonalDoctor>();
		this.personalDoctorList = personalDoctorList;
		for (PersonalDoctor personalDoctor : this.personalDoctorList) {
			this.type_info.put(personalDoctor.getType(), personalDoctor);
		}

		/** 对于没有设置过的，也初始化一下，前端就不用判断了 */
		for (PersonalDocterType personalDocterType : PersonalDocterType.values()) {
			if (!this.type_info.containsKey(personalDocterType.id)) {
				PersonalDoctor personalDoctor = new PersonalDoctor(0, personalDocterType.id, ServiceStatus.CLOSED.ordinal(), personalDocterType.default_price);
				this.type_info.put(personalDoctor.getType(), personalDoctor);
				this.personalDoctorList.add(personalDoctor);
			}
		}
	}

	/** 更新 */
	protected void update(Doctor doctor, PersonalDoctor personalDoctor) {
		PersonalDoctor personalDoctorInfo = this.type_info.get(personalDoctor.getType());
		if (personalDoctorInfo != null) {
			personalDoctorInfo.setStatus(personalDoctor.getStatus());
			personalDoctorInfo.setPrice(personalDoctor.getPrice());
			return;
		}
		personalDoctorInfo = new PersonalDoctor(doctor.getId(), personalDoctor.getType(), personalDoctor.getStatus(), personalDoctor.getPrice());
		this.personalDoctorList.add(personalDoctorInfo);
		this.type_info.put(personalDoctorInfo.getType(), personalDoctorInfo);

	}

	/** change状态 */
	protected void updateStatus(Doctor doctor, PersonalDoctor personalDoctor) {
		PersonalDoctor personalDoctorInfo = this.type_info.get(personalDoctor.getType());
		if (personalDoctorInfo != null) {
			personalDoctorInfo.setStatus(personalDoctor.getStatus());
			return;
		}
		personalDoctorInfo = new PersonalDoctor(doctor.getId(), personalDoctor.getType(), personalDoctor.getStatus(), PersonalDocterType.getPriceByType(personalDoctor.getType()));
		this.personalDoctorList.add(personalDoctorInfo);
		this.type_info.put(personalDoctorInfo.getType(), personalDoctorInfo);
	}

	/** change价格 */
	protected void updatePrice(Doctor doctor, PersonalDoctor personalDoctor) {
		PersonalDoctor personalDoctorInfo = this.type_info.get(personalDoctor.getType());
		if (personalDoctorInfo != null) {
			personalDoctorInfo.setPrice(personalDoctor.getPrice());
			return;
		}
		personalDoctorInfo = new PersonalDoctor(doctor.getId(), personalDoctor.getType(), ServiceStatus.CLOSED.ordinal(), personalDoctor.getPrice());
		this.personalDoctorList.add(personalDoctorInfo);
		this.type_info.put(personalDoctorInfo.getType(), personalDoctorInfo);
	}

	/** 只要开启了一种类型则为开启状态 */
	public ServiceStatus getStatus() {
		for (PersonalDoctor personalDoctor : personalDoctorList) {
			if (personalDoctor.getStatus() == ServiceStatus.OPENED.ordinal()) {
				return ServiceStatus.OPENED;
			}
		}
		return ServiceStatus.CLOSED;
	}

	public List<PersonalDoctor> getPersonalDoctorList() {
		return personalDoctorList;
	}

	public void setPersonalDoctorList(List<PersonalDoctor> personalDoctorList) {
		this.personalDoctorList = personalDoctorList;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("personalDoctorList", this.personalDoctorList);
		return res;
	}

}
