package com.jiuyi.doctor.pages;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.helper.ConstKeys;
import com.jiuyi.frame.util.StringUtil;

@Controller
public class PageController {

	@RequestMapping(value = "/html/{page}", method = RequestMethod.GET)
	public String getPage(@PathVariable String page) {
		return page;
	}

	@RequestMapping(value = "/html/salt", method = RequestMethod.POST)
	@ResponseBody
	public ServerResult getSalt(HttpServletRequest request) {
		String salt = StringUtil.getRandomStr(5);
		WebUtils.setSessionAttribute(request, ConstKeys.SALT, salt);
		ServerResult res = new ServerResult();
		res.put("salt", salt);
		return res;
	}
}
