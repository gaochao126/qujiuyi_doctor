package com.jiuyi.doctor.base;

import java.util.HashSet;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 上午10:00:07
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class FileUploadCheckService {

	private HashSet<String> whiteList;

	@PostConstruct
	public void init() {
		whiteList = new HashSet<String>();
		whiteList.add("jpg");
		whiteList.add("jpeg");
		whiteList.add("jpe");
		whiteList.add("png");
		whiteList.add("bmp");
	}

	public ServerResult securityCheck(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return new ServerResult();
		}
		file.getContentType();
		file.getOriginalFilename();
		String post = getPost(file.getOriginalFilename());
		if (!whiteList.contains(post)) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		return new ServerResult();
	}

	private String getPost(String filename) {
		String[] info = filename.split("\\.");
		return info[info.length - 1];
	}

	public ServerResult securityCheck(MultipartFile... files) {
		for (MultipartFile file : files) {
			ServerResult checkRes = securityCheck(file);
			if (!checkRes.isSuccess()) {
				return checkRes;
			}
		}
		return new ServerResult();
	}
}
