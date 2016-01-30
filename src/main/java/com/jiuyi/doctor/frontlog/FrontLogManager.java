/**
 * 
 */
package com.jiuyi.doctor.frontlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.user.UserManager;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.util.StringUtil;

/**
 * 
 * @author xutaoyang
 *
 */
@Service
public class FrontLogManager {

	private @Autowired FrontLogDao dao;

	private @Autowired UserManager userManager;

	protected ServerResult insertLog(FrontLog log, String token) {
		Doctor doctor = StringUtil.isNullOrEmpty(token) ? null : userManager.getUserByToken(token);
		int doctorId = doctor != null ? doctor.getId() : 0;
		log.setUserId(doctorId);
		dao.insertLog(log);
		return new ServerResult();
	}

}
