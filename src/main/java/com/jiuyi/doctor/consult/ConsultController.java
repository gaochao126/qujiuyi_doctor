package com.jiuyi.doctor.consult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jiuyi.doctor.consult.model.DoctorChat;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.annotations.MayEmpty;
import com.jiuyi.frame.annotations.Param;
import com.jiuyi.frame.annotations.TokenUser;
import com.jiuyi.frame.base.ControllerBase;
import com.jiuyi.frame.front.ServerResult;

/**
 * @Author: xutaoyang @Date: 上午10:48:17
 *
 * @Description 关于咨询的接口
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@RestController
public class ConsultController extends ControllerBase {

	public ConsultController() throws Exception {
		super("chat");
	}

	private static final String CMD = "chat_";
	/** 接收 */
	private static final String CMD_ACCEPT = CMD + "accept";
	/** 拒绝 */
	private static final String CMD_REFUSE = CMD + "refuse";
	/** 结束图文咨询服务 */
	private static final String CMD_STOP = CMD + "stop";
	/** 最近联系人 */
	private static final String CMD_HISTORY = CMD + "history";
	/** 还没处理的问答（付费申请） */
	private static final String CMD_NEW_CHAT = CMD + "new";
	/** 还没处理的问答（免费申请） */
	private static final String CMD_FREE_CHAT = CMD + "free";
	/** 未读信息 */
	private static final String CMD_UNREAD = CMD + "unread";
	/** 正在咨询 */
	private static final String CMD_CHATING = CMD + "chating";
	/** 未完成的咨询（未回馈的和正在进行的） */
	private static final String CMD_UNFINISH = CMD + "unfinish";
	/** 已经完成的咨询，（患者列表） */
	private static final String CMD_FINISHED = CMD + "finished";
	/** 搜索已经完成的咨询，（患者列表） */
	private static final String CMD_SEARCH_FINISHED = CMD + "search_finished";
	/** 已经完成的咨询，根据患者查询 */
	private static final String CMD_FINISHED_BY_PATIENT = CMD + "patient_finished";
	/** 查询患者的咨询 */
	private static final String CMD_LOAD_BY_PATIENT = CMD + "by_patient";
	/** 查询患者所有咨询 */
	private static final String CMD_LOAD_BY_PATIENT_ALL = CMD + "by_patient_all";
	/** 根据id查询咨询记录 */
	private static final String CMD_LOAD_BY_CONSULT_ID = CMD + "by_consult_id";
	/** 查询医生的所有咨询 */
	private static final String CMD_LOAD_ALL = CMD + "all";
	/** 导航栏上的数量信息 */
	private static final String CMD_COUNT_INFO = CMD + "count";
	/** 获取某次咨询的患者评价 */
	private static final String CMD_GET_COMMENT = CMD + "comment";

	/* 医生设置相关 */
	/** 开启图文咨询 */
	private static final String CMD_CHAT_OPEN = CMD + "open";
	/** 关闭图文咨询 */
	private static final String CMD_CHAT_CLOSE = CMD + "close";
	/** 获取图文咨询信息 */
	private static final String CMD_CHAT_INFO = CMD + "info";
	/** 设置图文咨询服务 */
	private static final String CMD_SET_CHAT = CMD + "set";

	@Autowired
	private ConsultManager manager;

	@RequestMapping(CMD_ACCEPT)
	public ServerResult handleAcceptChat(@TokenUser Doctor doctor, @Param("id") String consultId) {
		return manager.acceptChat(doctor, consultId);
	}

	@RequestMapping(CMD_REFUSE)
	public ServerResult handleRefuseChat(@TokenUser Doctor doctor, @Param("id") String consultId, @MayEmpty @Param("reason") String reason) {
		return manager.refuseChat(doctor, consultId, reason);
	}

	@RequestMapping(CMD_STOP)
	public ServerResult handleStopChat(@TokenUser Doctor doctor, @Param("id") String consultId) {
		return manager.stopChat(doctor, consultId);
	}

	@RequestMapping(CMD_HISTORY)
	public ServerResult loadChatHistory(@TokenUser Doctor doctor) {
		return manager.loadChatHistory(doctor);
	}

	@RequestMapping(CMD_LOAD_ALL)
	public ServerResult loadAllConsult(@TokenUser Doctor doctor) {
		return manager.loadAllConsult(doctor);
	}

	/**
	 * 导航栏上的数量信息
	 * 
	 * @param doctor
	 * @return
	 */
	@RequestMapping(CMD_COUNT_INFO)
	public ServerResult loadCountInfo(@TokenUser Doctor doctor) {
		return manager.loadCountInfo(doctor);
	}

	/**
	 * 获取咨询评价
	 * 
	 * @param doctor
	 * @return
	 */
	@RequestMapping(CMD_GET_COMMENT)
	public ServerResult loadComment(@TokenUser Doctor doctor, @Param("serviceId") String serviceId) {
		return manager.loadComment(doctor, serviceId);
	}

	/**
	 * 新咨询
	 * 
	 * @param doctor
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(CMD_NEW_CHAT)
	public ServerResult loadNewChat(@TokenUser Doctor doctor, @Param("page") Integer page, @Param("pageSize") Integer pageSize) {
		return manager.loadNewChat(doctor, page, pageSize);
	}

	/**
	 * 免费咨询列表
	 * 
	 * @param doctor
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(CMD_FREE_CHAT)
	public ServerResult loadFreeChat(@TokenUser Doctor doctor, @Param("page") Integer page, @Param("pageSize") Integer pageSize) {
		return manager.loadFreeConsult(doctor, page, pageSize);
	}

	/**
	 * 正在咨询列表
	 * 
	 * @param doctor
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(CMD_CHATING)
	public ServerResult loadChating(@TokenUser Doctor doctor, @Param("page") Integer page, @Param("pageSize") Integer pageSize) {
		return manager.loadChatingChat(doctor, page, pageSize);
	}

	/**
	 * 历史咨询，结果为患者列表
	 * 
	 * @param doctor
	 * @param type
	 *            0表示免费类型，非0表示付费类型
	 * @param page
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(CMD_FINISHED)
	public ServerResult loadFinishedChat(@TokenUser Doctor doctor, @Param("type") int type, @Param("page") Integer page, @Param("pageSize") Integer pageSize) {
		return manager.loadFinishedChat(doctor, type, page, pageSize);
	}

	/**
	 * 搜索历史咨询，结果为患者列表
	 * 
	 * @param doctor
	 * @param type
	 *            0表示免费类型，非0表示付费类型
	 * @param key
	 *            搜索关键字
	 * @return
	 */
	@RequestMapping(CMD_SEARCH_FINISHED)
	public ServerResult searchFinishedChat(@TokenUser Doctor doctor, @Param("type") int type, @Param("key") String key) {
		return manager.searchFinishedChat(doctor, type, key);
	}

	@RequestMapping(CMD_UNFINISH)
	public ServerResult loadUnfinishChat(@TokenUser Doctor doctor) {
		return manager.loadUnFinishedChat(doctor);
	}

	@RequestMapping(CMD_FINISHED_BY_PATIENT)
	public ServerResult loadFinishedChatByPatient(@TokenUser Doctor doctor, @Param("patientId") int patientId, @Param("type") int type) {
		return manager.loadFinishedChatByPatient(doctor, patientId, type);
	}

	@RequestMapping(CMD_UNREAD)
	public ServerResult loadUnreadMsgs(@TokenUser Doctor doctor) {
		return manager.loadUnreadMsgs(doctor);
	}

	@RequestMapping(CMD_LOAD_BY_PATIENT)
	public ServerResult loadConsultByPatientId(@TokenUser Doctor doctor, @Param("patientId") int patientId) {
		return manager.loadConsultingByPatientId(doctor, patientId);
	}

	@RequestMapping(CMD_LOAD_BY_PATIENT_ALL)
	public ServerResult loadAllConsultByPatientId(@TokenUser Doctor doctor, @Param("patientId") int patientId) {
		return manager.loadAllConsultByPatientId(doctor, patientId);
	}

	@RequestMapping(CMD_LOAD_BY_CONSULT_ID)
	public ServerResult loadConsultById(@TokenUser Doctor doctor, @Param("id") String consultId) {
		return manager.loadConsultById(doctor, consultId);
	}

	@RequestMapping(CMD_CHAT_OPEN)
	public ServerResult openChatService(@TokenUser Doctor doctor) {
		return manager.openChatService(doctor);
	}

	@RequestMapping(CMD_CHAT_CLOSE)
	public ServerResult closeChatService(@TokenUser Doctor doctor) {
		return manager.closeChatService(doctor);
	}

	@RequestMapping(CMD_CHAT_INFO)
	public ServerResult handleLoadChatInfo(@TokenUser Doctor doctor) {
		return manager.handleLoadChatInfo(doctor);
	}

	@RequestMapping(CMD_SET_CHAT)
	public ServerResult handleUpdateChatInfo(@TokenUser Doctor doctor, @Param("chat") DoctorChat chatService) {
		return manager.handleUpdateChatInfo(doctor, chatService);
	}
}
