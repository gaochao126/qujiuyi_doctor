package com.jiuyi.doctor.patients.interfaces;

import java.util.List;

import com.jiuyi.doctor.patients.model.PatientInfo;
import com.jiuyi.doctor.user.model.Doctor;

/**
 * @Author: xutaoyang @Date: 下午6:11:55
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
public interface IRecommendPatient {

	public List<PatientInfo> recommendPatient(Doctor doctor);

}
