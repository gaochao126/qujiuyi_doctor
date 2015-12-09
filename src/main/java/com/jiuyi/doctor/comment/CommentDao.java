package com.jiuyi.doctor.comment;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.DbBase;

@Repository
public class CommentDao extends DbBase {

	private static final String SELECT_COMMENT_TYPE_SERVICEID = "SELECT * FROM `t_doctor_comment` WHERE `serviceType`=? AND `serviceId`=? AND `doctorId`=?";

	private static final String SELECT_EVALUATION_PAGE = "SELECT c.*,p.`nickname` as patientName,p.`gender` as patientGender, p.`headPortrait`as patientHead,p.`birthday` as patientAge FROM `t_doctor_comment` c,`t_patient` p WHERE p.`id`=c.`patientId` AND c.`doctorId`=? ORDER BY c.`commentTime` DESC LIMIT ?,?";
	private static final String SELECT_EVALUATION_COUNT = "SELECT COUNT(`id`) FROM `t_doctor_comment` WHERE `commentTime`>? AND `doctorId`=?";

	protected List<Comment> loadComment(Doctor doctor, int page, int pageSize) {
		return queryForList(SELECT_EVALUATION_PAGE, new Object[] { doctor.getId(), startIndex(page, pageSize), pageSize }, Comment.class);
	}

	protected Integer loadUnreadCmtCount(Doctor doctor, Date lastGetUnreadEvaTime) {
		return queryForInteger(SELECT_EVALUATION_COUNT, new Object[] { lastGetUnreadEvaTime, doctor.getId() });
	}

	protected Comment loadCommentByTypeServiceId(Doctor doctor, CommentServiceType type, String serviceId) {
		return queryForObjectDefaultBuilder(SELECT_COMMENT_TYPE_SERVICEID, new Object[] { type.id, serviceId, doctor.getId() }, Comment.class);
	}
}
