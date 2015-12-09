package com.jiuyi.doctor.patients.impls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.patients.PatientDAO;
import com.jiuyi.doctor.patients.interfaces.IRecommendPatient;
import com.jiuyi.doctor.patients.model.PatientInfo;
import com.jiuyi.doctor.user.model.Doctor;

/**
 * @Author: xutaoyang @Date: 下午6:13:37
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service("RecommendPatientImpl")
public class RecommendPatientImpl implements IRecommendPatient {

	@Autowired
	PatientDAO patientDao;

	private static final int ONCE_RECOMMEND = 10;

	@Override
	public List<PatientInfo> recommendPatient(Doctor doctor) {
		Integer patientCount = patientDao.loadPatientCount();
		if (patientCount == 0) {
			return null;
		}
		if (patientCount <= ONCE_RECOMMEND) {
			return patientDao.recommendPatient(doctor);
		}
		int randomStart = (int) (Math.random() * patientCount) - ONCE_RECOMMEND;
		randomStart = randomStart < 0 ? 0 : randomStart;
		return patientDao.recommendPatient(doctor, randomStart, ONCE_RECOMMEND);
	}
}
