/**
 * 
 */
package com.jiuyi.doctor.prescription;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.front.ServerResult;

/**
 * 处方相关
 * 
 * @author xutaoyang
 *
 */
@RestController
public class PrescriptionController {

	private @Autowired PrescriptionManager manager;

	private static final String CMD = "prescription_";
	private static final String CMD_CREATE = CMD + "create";
	private static final String CMD_PRESCRIBE = CMD + "prescribe";
	private static final String CMD_LOAD_DETAIL = CMD + "load_detail";
	private static final String CMD_LOAD_LIST = CMD + "load_list";
	private static final String CMD_SEARCH_HISTORY = CMD + "search";

	/**
	 * 创建处方，等待患者同意
	 * 
	 * @param doctor
	 * @param patientId
	 * @return
	 */
	@RequestMapping(CMD_CREATE)
	public ServerResult createPrescription(@TokenUser Doctor doctor, @Param("patientId") Integer patientId) {
		return manager.createPrescription(doctor, patientId);
	}

	/**
	 * 开处方
	 * 
	 * @param doctor
	 * @param patientId
	 * @return
	 */
	@RequestMapping(CMD_PRESCRIBE)
	public ServerResult prescribe(@TokenUser Doctor doctor, @Param("prescription") Prescription prescription, @Param("medicines") List<PrescriptionDetail> medicines) {
		return manager.prescribe(doctor, prescription, medicines);
	}

	/**
	 * 获取处方列表
	 * 
	 * @param doctor
	 * @param page
	 * @param pageSize
	 * @param type
	 *            0待用户确认，1处理中，2历史记录
	 * @return
	 */
	@RequestMapping(CMD_LOAD_LIST)
	public ServerResult loadList(@TokenUser Doctor doctor, @Param("page") int page, @Param("pageSize") int pageSize, @Param("type") int type) {
		return manager.loadList(doctor, page, pageSize, type);
	}

	/**
	 * 获取处方详情
	 * 
	 * @param doctor
	 * @param id
	 *            处方id
	 * @return
	 */
	@RequestMapping(CMD_LOAD_DETAIL)
	public ServerResult loadDetail(@TokenUser Doctor doctor, @Param("id") String id) {
		return manager.loadDetail(doctor, id);
	}

	/**
	 * 搜索历史记录
	 * 
	 * @param doctor
	 * @param key
	 * @return
	 */
	@RequestMapping(CMD_SEARCH_HISTORY)
	public ServerResult searchHistory(@TokenUser Doctor doctor, @Param("key") String key) {
		return manager.searchHistory(doctor, key);
	}
}
