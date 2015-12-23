/**
 * 
 */
package com.jiuyi.doctor.medicalkit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.front.ServerResult;

/**
 * 医生药箱功能
 * 
 * @author xutaoyang
 *
 */
@RestController
public class MedicalKitController {

	private static final String CMD = "medicalkit_";
	private static final String CMD_COLLECT_FORMAT = CMD + "collect";
	private static final String CMD_MY_LIST = CMD + "my_list";
	private static final String CMD_SEARCH_MY_LIST = CMD + "search";

	private @Autowired MedicalKitManager manager;

	/**
	 * 收藏/取消收藏
	 * 
	 * @param doctor
	 * @param formatId
	 * @return
	 */
	@RequestMapping(CMD_COLLECT_FORMAT)
	public ServerResult collectFormat(@TokenUser Doctor doctor, @Param("formatId") String formatId) {
		return manager.collectFormat(doctor, formatId);
	}

	@RequestMapping(CMD_MY_LIST)
	public ServerResult myList(@TokenUser Doctor doctor, @Param("page") int page, @Param("pageSize") int pageSize) {
		return manager.myList(doctor, page, pageSize);
	}

	@RequestMapping(CMD_SEARCH_MY_LIST)
	public ServerResult searchMyList(@TokenUser Doctor doctor, @Param("key") String key) {
		return manager.searchMyList(doctor, key);
	}
}
