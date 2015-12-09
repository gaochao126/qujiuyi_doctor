package com.jiuyi.doctor.permission;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.permission.AbsPermissionCheck;

@Service("PermissionCheck")
public class PermissionCheck extends AbsPermissionCheck {

	@Override
	public ServerResult checkPermission(HttpServletRequest req, HttpServletResponse resp, Object handler) {
		
		return new ServerResult();
	}

}
