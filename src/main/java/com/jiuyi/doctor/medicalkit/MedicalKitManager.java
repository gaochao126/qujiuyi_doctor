package com.jiuyi.doctor.medicalkit;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.doctor.yaofang.YaofangService;
import com.jiuyi.doctor.yaofang.model.FormatMedicine;
import com.jiuyi.frame.front.ServerResult;

@Service
public class MedicalKitManager {

	private @Autowired MedicalKitDao dao;

	private @Autowired YaofangService yaofangService;

	/**
	 * 收藏规格
	 * 
	 * @param formatId
	 * @return
	 */
	protected ServerResult collectFormat(Doctor doctor, String formatId) {
		boolean isInKit = isInKit(doctor, formatId);
		if (isInKit) {
			dao.deleteCollect(doctor, formatId);
		} else {
			dao.collect(doctor, formatId);
		}
		return new ServerResult("isInKit", !isInKit);
	}

	/**
	 * 药箱
	 * 
	 * @param doctor
	 * @return
	 */
	protected ServerResult myList(Doctor doctor, int page, int pageSize) {
		ServerResult res = new ServerResult();
		List<FormatMedicine> fm = myMedicalKit(doctor, page, pageSize);
		res.putObjects("list", fm);
		return res;
	}

	/**
	 * 该规格是否在医生的药箱中
	 * 
	 * @param doctor
	 * @param formatId
	 * @return
	 */
	protected boolean isInKit(Doctor doctor, String formatId) {
		return dao.checkCollectExist(doctor, formatId) > 0;
	}

	/**
	 * @param doctor
	 * @param key
	 * @return
	 */
	protected ServerResult searchMyList(Doctor doctor, String key) {
		List<FormatMedicine> all = myMedicalKit(doctor, 1, Integer.MAX_VALUE);
		List<FormatMedicine> fms = new ArrayList<>();

		for (FormatMedicine fm : all) {
			if (fm.like(key)) {
				fms.add(fm);
			}
		}
		ServerResult res = new ServerResult();
		res.putObjects("list", fms);
		return res;
	}

	private List<FormatMedicine> myMedicalKit(Doctor doctor, int page, int pageSize) {
		List<String> formatIds = dao.loadMyList(doctor, 1, Integer.MAX_VALUE);
		List<FormatMedicine> fm = yaofangService.loadFormatMeds(formatIds);
		return fm;
	}
}
