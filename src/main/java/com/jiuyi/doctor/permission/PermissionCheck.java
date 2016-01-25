package com.jiuyi.doctor.permission;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;

import com.jiuyi.doctor.user.UserManager;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.doctor.user.model.DoctorStatus;
import com.jiuyi.frame.argsresolve.MethodInfo;
import com.jiuyi.frame.argsresolve.MethodParamService;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.permission.AbsPermissionCheck;
import com.jiuyi.frame.util.StringUtil;
import com.jiuyi.frame.util.WebUtil;

@Service("PermissionCheck")
public class PermissionCheck extends AbsPermissionCheck {

	protected @Autowired MethodParamService methodParamService;

	protected @Autowired UserManager userManager;

	@Override
	public ServerResult checkPermission(HttpServletRequest req, HttpServletResponse resp, Object handler) {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			MethodInfo methodInfo = methodParamService.getMethodInfo(handlerMethod);
			if (methodInfo.tokenUser == null) {
				return new ServerResult();
			}
			Permission permission = handlerMethod.getMethodAnnotation(Permission.class);
			DoctorStatus authStatus = permission == null ? DoctorStatus.NORMAL : permission.value();
			String token = (String) WebUtil.getParamInRequest(req, "token", String.class);
			if (StringUtil.isNullOrEmpty(token)) {
				return new ServerResult(ResultConst.NEED_PARAM_TOKEN);
			}
			Doctor doctor = userManager.getUserByToken(token);
			if (doctor == null) {
				return new ServerResult(ResultConst.NOT_LOGIN);
			}
			if (!authStatus.hasPrivilege(doctor.getStatus())) {
				return new ServerResult(ResultConst.NO_PERMISSIONS);
			}
		}
		return new ServerResult();
	}

}
