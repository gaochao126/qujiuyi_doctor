/**
 * 
 */
package com.jiuyi.doctor.advices;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jiuyi.doctor.user.UserService;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;

/**
 * 
 * 出于安全考虑，token具有一定的时效性，在一定时间后会过期，此时需要生成一个新的token返回给客户端，由于不是长连接，所以
 * 
 * 用切面的方式更新token，针对每一个提供的接口，如果到了规定的时间（UserManager里面跑的job），将重新生成一个token，并返回给客户端
 * 
 * @author xutaoyang
 *
 */
@Aspect
@Component
public class UpdateTokenAdvice {

	private @Autowired UserService userService;

	@Pointcut("execution(com.jiuyi.frame.front.ServerResult com.jiuyi.doctor.*.*Controller.*(@com.jiuyi.frame.annotations.TokenUser (com.jiuyi.doctor.user.model.Doctor),..))")
	private void handlerMethodWithServerResultAndUser() {
	}// 定义一个切入点

	@AfterReturning(pointcut = "handlerMethodWithServerResultAndUser() && args(doctor,..) && @annotation(requestMapping)", returning = "returnValue")
	public void updateToken(ServerResult returnValue, Doctor doctor, RequestMapping requestMapping) {
		String url = requestMapping.value()[0];
		url = url.startsWith("/") && !url.equals("/") ? url.substring(1) : url;
		returnValue.put("cmd", url);
		if (returnValue.isSuccess() && doctor != null && doctor.getNeedUpdateToken().get()) {
			userService.updateToken(doctor);
			returnValue.put("newToken", doctor.getAccess_token());
			returnValue.putResult(ResultConst.PLEASE_UPDATE_TOKEN);
		}
	}

}
