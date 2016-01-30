/**
 * 
 */
package com.jiuyi.doctor.frontlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.frame.front.ServerResult;

/**
 * 
 * @author xutaoyang
 *
 */
@Service
public class FrontLogManager {

	private @Autowired FrontLogDao dao;

	protected ServerResult insertLog(FrontLog log) {
		dao.insertLog(log);
		return new ServerResult();
	}

}
