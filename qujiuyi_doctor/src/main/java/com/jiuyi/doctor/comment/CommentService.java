package com.jiuyi.doctor.comment;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.user.model.Doctor;

@Service
public class CommentService {

	private @Autowired CommentManager manager;

	public Comment loadConsultComment(Doctor doctor, String serviceId) {
		return manager.loadCommentByTypeServiceId(doctor, CommentServiceType.CONSULT, serviceId);
	}

	public List<Comment> loadComment(Doctor doctor, int page, int pageSize) {
		return manager.loadComment(doctor, page, pageSize);
	}

	public Integer loadUnreadCmtCount(Doctor doctor, Date lastGetUnreadEvaTime) {
		return manager.loadUnreadCmtCount(doctor, lastGetUnreadEvaTime);
	}

	/**
	 * @param doctor
	 * @return
	 */
	public int countTotalComment(Doctor doctor) {
		return manager.countDoctorComment(doctor);
	}
}
