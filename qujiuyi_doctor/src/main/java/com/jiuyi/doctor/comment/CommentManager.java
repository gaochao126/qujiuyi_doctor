package com.jiuyi.doctor.comment;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.user.model.Doctor;

@Service
public class CommentManager {

	private @Autowired CommentDao dao;

	protected Comment loadCommentByTypeServiceId(Doctor doctor, CommentServiceType type, String serviceId) {
		return dao.loadCommentByTypeServiceId(doctor, type, serviceId);
	}

	protected List<Comment> loadComment(Doctor doctor, int page, int pageSize) {
		return dao.loadComment(doctor, page, pageSize);
	}

	protected Integer loadUnreadCmtCount(Doctor doctor, Date lastGetUnreadEvaTime) {
		return dao.loadUnreadCmtCount(doctor, lastGetUnreadEvaTime);
	}
}
