package com.jiuyi.doctor.consult.model;

import java.util.Date;

import com.jiuyi.frame.annotations.ConfigPrefix;
import com.jiuyi.frame.constants.Constants;
import com.jiuyi.frame.front.ISerializableObj;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.util.DateUtil;

/**
 * @Author: xutaoyang @Date: 下午1:43:23
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public class ChatRecord implements ISerializableObj {

	private int patientId;
	private String patientName;
	@ConfigPrefix(Constants.KEY_PATIENT_HEAD)
	private String headPortrait;
	private Date date;

	public int getPatientId() {
		return patientId;
	}

	public String getPatientName() {
		return patientName;
	}

	public Date getDate() {
		return date;
	}

	public void setPatientId(int patientId) {
		this.patientId = patientId;
	}

	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getHeadPortrait() {
		return headPortrait;
	}

	public void setHeadPortrait(String headPortrait) {
		this.headPortrait = headPortrait;
	}

	@Override
	public MapObject serializeToMapObject() {
		MapObject res = new MapObject();
		res.put("patientId", this.patientId);
		res.put("name", this.patientName);
		res.put("patientHead", this.headPortrait);
		res.put("date", DateUtil.date2Str(this.date));
		return res;
	}

}
