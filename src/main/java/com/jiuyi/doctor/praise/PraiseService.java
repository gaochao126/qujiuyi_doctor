package com.jiuyi.doctor.praise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.user.model.Doctor;

@Service
public class PraiseService {

	@Autowired
	private PraiseManager manager;

	/** doctor是否赞过doctorId */
	public boolean praised(Doctor doctor, Integer doctorId) {
		return manager.praised(doctor, doctorId);
	}

}
