/**
 * 
 */
package com.jiuyi.doctor.test;

import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.patients.model.Patient;
import com.jiuyi.frame.base.DbBase;

/**
 * 
 * @author xutaoyang
 *
 */
@Repository
public class TestDao extends DbBase {

	private static final String SELECT = "SELECT * FROM `t_patient` WHERE `id`=";

	protected Patient loadPatient(String id) {
		String sql = SELECT + id + "'";
		System.err.println(sql);
		return queryForObjectDefaultBuilder(SELECT + id + "", Patient.class);
	}

}
