package com.jiuyi.doctor.prescribe;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.MayEmpty;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 下午2:20:23
 *
 * @Description 配药相关
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@RestController
public class PrescribeController extends ControllerBase {

	private static final String CMD = "prescribe_";
	private static final String CMD_LOAD_INFO = CMD + "info";// 配药服务状态
	private static final String CMD_OPEN = CMD + "open";// 开启配药服务
	private static final String CMD_CLOSE = CMD + "close";// 关闭配药服务

	private static final String CMD_LOAD_ALL = CMD + "all";// 医生所有配药记录
	private static final String CMD_LOAD_UNHANDLE = CMD + "unhandled";// 未处理的记录
	private static final String CMD_LOAD_PRESCRIBE_HANDLED = CMD + "handled";// 已经处理的
	private static final String CMD_LOAD_PRESCRIBE_ACCEPTED = CMD + "accepted";// 已经接受的
	private static final String CMD_LOAD_PRESCRIBE_MEDCINES = CMD + "medicines";// 详细药品
	private static final String CMD_PRESCRIBE = CMD + "accept";// 开处方单
	private static final String CMD_REFUSE = CMD + "refuse";// 拒绝配药
	private static final String CMD_DELETE = CMD + "delete";
	private static final String CMD_LOAD_BY_ID = CMD + "by_id";

	@Autowired
	private PrescribeManager manager;

	@RequestMapping(CMD_LOAD_INFO)
	public ServerResult handleLoadInfo(@TokenUser Doctor doctor) {
		return manager.loadInfo(doctor);
	}

	@RequestMapping(CMD_OPEN)
	public ServerResult handleOpenService(@TokenUser Doctor doctor) { 
		return manager.openService(doctor);
	}

	@RequestMapping(CMD_CLOSE)
	public ServerResult handleCloseService(@TokenUser Doctor doctor) {
		return manager.closeService(doctor);
	}

	@RequestMapping(CMD_LOAD_ALL)
	public ServerResult handleLoadAll(@TokenUser Doctor doctor) {
		return manager.loadAllPrescribe(doctor);
	}

	@RequestMapping(CMD_LOAD_UNHANDLE)
	public ServerResult handleLoadUnhandlePrescribe(@TokenUser Doctor doctor, @Param("page") Integer page, @Param("pageSize") Integer pageSize) {
		return manager.loadUnhandlePrescribe(doctor, page, pageSize);
	}

	@RequestMapping(CMD_LOAD_PRESCRIBE_HANDLED)
	public ServerResult handleLoadHandledPrescribe(@TokenUser Doctor doctor, @Param("page") Integer page, @Param("pageSize") Integer pageSize) {
		return manager.loadHandledPrescribe(doctor, page, pageSize);
	}

	@RequestMapping(CMD_LOAD_PRESCRIBE_ACCEPTED)
	public ServerResult handleLoadAcceptedPrescribe(@TokenUser Doctor doctor) {
		return manager.loadAcceptedPrescribe(doctor);
	}

	@RequestMapping(CMD_LOAD_PRESCRIBE_MEDCINES)
	public ServerResult handleLoadPrescribeMedcines(@TokenUser Doctor doctor, @Param("id") Integer prescribeId) {
		return manager.loadPrescribeMedcines(doctor, prescribeId);
	}

	@RequestMapping(CMD_PRESCRIBE)
	public ServerResult handleAcceptPrescribe(@TokenUser Doctor doctor, @RequestParam("id") Integer prescribeId, @RequestParam("pres") MultipartFile prescriptionList) {
		return manager.acceptPrescribe(doctor, prescribeId, prescriptionList);
	}

	@RequestMapping(CMD_REFUSE)
	public ServerResult handleRefuse(@TokenUser Doctor doctor, @Param("id") Integer prescribeId, @MayEmpty @Param("reason") String reason) {
		return manager.refusePrescribe(doctor, prescribeId, reason);
	}

	@RequestMapping(CMD_LOAD_BY_ID)
	public ServerResult loadById(@TokenUser Doctor doctor, @Param("id") Integer id) {
		return manager.loadById(doctor, id);
	}

	@RequestMapping(CMD_DELETE)
	public ServerResult handleDelete(@TokenUser Doctor doctor, @Param("ids") Integer[] prescribeIds) {
		return manager.deletePrescribes(doctor, prescribeIds);
	}

}
