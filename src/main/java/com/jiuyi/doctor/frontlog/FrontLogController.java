/**
 * 
 */
package com.jiuyi.doctor.frontlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.frame.annotations.MayEmpty;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.front.ServerResult;

/**
 * 收集前端日志
 * 
 * @author xutaoyang
 *
 */
@RestController
public class FrontLogController {

	private static final String CMD = "log_";
	private static final String CMD_LOG = CMD + "add";

	private @Autowired FrontLogManager manager;

	@RequestMapping(CMD_LOG)
	public ServerResult log(@Param("log") FrontLog log, @MayEmpty @Param("token") String token) {
		return manager.insertLog(log, token);
	}

}
