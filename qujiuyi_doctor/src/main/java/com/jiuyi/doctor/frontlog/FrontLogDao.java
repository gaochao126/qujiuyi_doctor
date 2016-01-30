/**
 * 
 */
package com.jiuyi.doctor.frontlog;

import org.springframework.stereotype.Repository;

import com.jiuyi.frame.base.DbBase;

/**
 * 
 * @author xutaoyang
 *
 */
@Repository
public class FrontLogDao extends DbBase {

	@Override
	protected String getTableName() {
		return "t_front_log";
	}

	protected void insertLog(FrontLog log) {
		super.insertReturnLong(log);
	}
}
