package com.jiuyi.doctor.feedback;

import java.sql.Timestamp;

import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 下午1:37:29
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class FeedbackDao extends DbBase {

	private static final String INSERT_FEEDBACK = "INSERT `t_suggestion_feedback`(`userId`,`userType`,`content`,`contactWay`,`createTime`) VALUE(?,?,?,?,?)";

	protected void addFeedback(Doctor doctor, Feedback feedback) {
		jdbc.update(INSERT_FEEDBACK, new Object[] { doctor.getId(), feedback.getUserType(), feedback.getContent(), feedback.getContactWay(), new Timestamp(System.currentTimeMillis()) });
	}

}
