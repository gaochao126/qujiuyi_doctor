package com.jiuyi.doctor.praise;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.doctor.user.model.SimpleDoctor;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;

@Service
public class PraiseManager {

	@Autowired
	private PraiseDao dao;

	/** 如果赞了就取消，否则点赞 */
	@Transactional(rollbackFor = Exception.class)
	protected ServerResult togglePraise(Doctor doctor, Integer doctorId) {
		if (doctor.getId() == doctorId) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		boolean praised = praised(doctor, doctorId);
		if (praised) {
			dao.cancelPraise(doctor, doctorId);
			decPraisedNum(doctorId);
		} else {
			dao.praise(doctor, doctorId);
			incPraisedNum(doctorId);
		}
		return new ServerResult();
	}

	protected boolean praised(Doctor doctor, Integer doctorId) {
		return dao.praised(doctor, doctorId);
	}

	protected ServerResult recommendDoctor(Doctor doctor, int num) {
		if (num > 5) {
			return new ServerResult();
		}
		return new ServerResult("list", recommendDoctorList(doctor, num), true);
	}

	/**
	 * 推荐医生 --- A.首先推荐同一个医院的，并且没有点过赞的 ；B.如果没有，则随机推荐没有点过赞的；C.如果再没有，则随机推荐
	 * 
	 * @param doctor
	 * @param recommendNum
	 * @return
	 */
	protected List<SimpleDoctor> recommendDoctorList(Doctor doctor, int recommendNum) {
		List<SimpleDoctor> res = dao.unPraisedDoctor(doctor, recommendNum);// 得到的是同一个医院的医生
		if (res.size() < recommendNum) {
			List<SimpleDoctor> rand = dao.unPraisedDoctorRand(doctor, recommendNum - res.size());
			res.addAll(rand);
		}
		return res;
	}

	private void incPraisedNum(Integer doctorId) {
		dao.incPraisedNum(doctorId);
	}

	private void decPraisedNum(Integer doctorId) {
		dao.decPraisedNum(doctorId);
	}

}
