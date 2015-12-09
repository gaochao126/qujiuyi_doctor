package com.jiuyi.doctor.home;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.home.SystemMsgManager.SystemMsgStatus;
import com.jiuyi.doctor.home.model.SystemMsg;
import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 上午10:53:11
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class SystemMsgDao extends DbBase {

	private static final String SELECT_ALL = "SELECT * FROM `t_doctor_system_msg` WHERE `status`=1 ORDER BY `date` DESC";
	private static final String INSERT_MSG = "INSERT `t_doctor_system_msg`(`title`,`content`) VALUE('?','?');";
	private static final String UPDATE_STATUS = "UPDATE `t_doctor_system_msg` SET `status`=? WHERE `id`=?";

	protected int addSystemMsg(final SystemMsg msg) {
		KeyHolder holder = new GeneratedKeyHolder();
		PreparedStatementCreator psc = new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(INSERT_MSG, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, msg.getTitle());
				ps.setString(2, msg.getContent());
				return ps;
			}
		};
		jdbc.update(psc, holder);
		return holder.getKey().intValue();
	}

	protected void deleteSystemMsg(Integer id) {
		updateStatus(id, SystemMsgStatus.DELETED);
	}

	protected void updateSystemMsg(SystemMsg newMsg) {

	}

	protected List<SystemMsg> loadAll() {
		return jdbc.query(SELECT_ALL, SystemMsg.builder);
	}

	protected void updateStatus(int id, SystemMsgStatus status) {
		jdbc.update(UPDATE_STATUS, new Object[] { status.ordinal(), id });
	}

}
