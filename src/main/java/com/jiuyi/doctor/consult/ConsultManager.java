package com.jiuyi.doctor.consult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyi.doctor.account.AccountService;
import com.jiuyi.doctor.activity.yiyuanyizhen.YiyuanyizhenService;
import com.jiuyi.doctor.chatserver.ChatServerService;
import com.jiuyi.doctor.comment.Comment;
import com.jiuyi.doctor.comment.CommentService;
import com.jiuyi.doctor.consult.enums.ConsultType;
import com.jiuyi.doctor.consult.model.ChatAcceptRequest;
import com.jiuyi.doctor.consult.model.ChatRecord;
import com.jiuyi.doctor.consult.model.Consult;
import com.jiuyi.doctor.consult.model.DoctorChat;
import com.jiuyi.doctor.consult.model.UnreadMsg;
import com.jiuyi.doctor.consult.model.UnreadMsgWrapper;
import com.jiuyi.doctor.hospitals.model.DoctorTitle;
import com.jiuyi.doctor.order.OrderService;
import com.jiuyi.doctor.order.OrderType;
import com.jiuyi.doctor.order.ThirdPayOrder;
import com.jiuyi.doctor.patients.PatientService;
import com.jiuyi.doctor.patients.v2.PatientServiceV2;
import com.jiuyi.doctor.patients.v2.model.Patient;
import com.jiuyi.doctor.services.ServiceStatus;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.ManagerBase;
import com.jiuyi.frame.db.RowData;
import com.jiuyi.frame.front.FailResult;
import com.jiuyi.frame.front.MapObject;
import com.jiuyi.frame.front.ResultConst;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.util.CollectionUtil;
import com.jiuyi.frame.util.ObjectUtil;

/**
 * @Author: xutaoyang @Date: 上午11:00:42
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class ConsultManager extends ManagerBase<Doctor, DoctorChat> {

	private @Autowired ConsultDao dao;

	private @Autowired AccountService accountService;

	private @Autowired ChatServerService chatServer;

	private @Autowired PatientService patientService;

	private @Autowired YiyuanyizhenService yiyuanyizhenService;

	private @Autowired PatientServiceV2 patientServiceV2;

	private @Autowired CommentService commentService;

	private @Autowired OrderService orderService;

	private static final String RESPONSE_CONSULT = "consultResponse";
	private static final String STOP_CONSULT = "endConsultRequest";

	/** 免费咨询 */
	private static final int CONSULT_FREE = 0;

	/** 付费咨询 */
	private static final int CONSULT_PAYED = 1;

	// =====医生服务信息相关======
	protected ServerResult openChatService(Doctor doctor) {
		updateDoctorChatStatus(doctor, ServiceStatus.OPENED);
		return new ServerResult();
	}

	/** 关闭服务 */
	protected ServerResult closeChatService(Doctor doctor) {
		updateDoctorChatStatus(doctor, ServiceStatus.CLOSED);
		return new ServerResult();
	}

	/** 获取医生的图文咨询设置 */
	protected ServerResult handleLoadChatInfo(Doctor doctor) {
		DoctorChat doctorChat = loadInfoBase(doctor);
		return new ServerResult(doctorChat);
	}

	/** 更新医生的图文咨询设置 */
	protected ServerResult handleUpdateChatInfo(Doctor doctor, DoctorChat chatService) {
		int price = chatService.getPrice();
		if (price < 1) {
			return new ServerResult(ResultConst.FAIL.id, "价格必须大于等于1元~");
		}
		updateDoctorChatPrice(doctor, price);
		if (price == 0 && yiyuanyizhenService.isOpen(doctor)) {// 如果设为0元则关闭一元义诊服务
			yiyuanyizhenService.close(doctor);
			return new ServerResult(ResultConst.YIYUAN_CLOSED);
		}
		return new ServerResult();
	}

	/** 修改开启关闭状态 */
	protected void updateDoctorChatStatus(Doctor doctor, ServiceStatus serviceStatus) {
		DoctorChat doctorChat = loadInfoBase(doctor);
		doctorChat.setStatus(serviceStatus.ordinal());
		dao.updateChatStatus(doctor, serviceStatus);
	}

	/** 修改价格 */
	protected void updateDoctorChatPrice(Doctor doctor, int price) {
		DoctorChat doctorChat = loadInfoBase(doctor);
		doctorChat.setPrice(price);
		dao.updateChatPrice(doctor, price);
	}

	// ==========与患者图文咨询互动相关==========

	/** 接收 */
	@Transactional
	protected ServerResult acceptChat(Doctor doctor, String consultId) {
		Consult consult = loadConsult(consultId);
		if (consult == null || consult.getAcceptStatus() != 0) {// 0是未接收状态
			return new FailResult("已经被其他医生接受了！");
		}
		if (consult.getDoctorId() != doctor.getId() && consult.getType() != CONSULT_FREE) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		if (consult.getPhone().equals(doctor.getPhone())) {
			return new FailResult("对不起，不能接受自己的提问哦~");
		}
		Consult consulting = dao.loadConsultingByPatientId(doctor, consult.getPatientId());
		if (consulting != null) {
			return new FailResult("您与该患者有正在进行的咨询，请先结束该次咨询");
		}
		/*
		 * ****************如果是接受：医生即将到账，更新consult状态，添加最近联系人，添加患者列表。*******
		 * ****************如果是拒绝：退款，更新consult状态*******************
		 */
		dao.onAcceptConsult(doctor, consult);
		dao.addChatList(doctor, consult.getPatientId());// 添加最近联系人
		patientService.addPatient(doctor, consult.getPatientId());// 添加到患者群

		/* 付费图文咨询 */
		if (consult.getType() == ConsultType.COMMON.ordinal()) {
			ThirdPayOrder order = orderService.loadOrderByTypeAndServiceId(OrderType.CONSULT, consultId);
			if (order != null) {
				BigDecimal money = order.getTotalAmount();// 存入 医生即将到账
				accountService.incComing(doctor, money);
			}
		}
		ChatAcceptRequest request = new ChatAcceptRequest(RESPONSE_CONSULT, doctor, consult, true);
		chatServer.postMsg(request);
		patientServiceV2.addToUnfamiliar(doctor, consult.getPatientId());
		return new ServerResult();
	}

	/** 拒绝图文咨询 */
	@Transactional
	protected ServerResult refuseChat(Doctor doctor, String consultId, String reason) {
		Consult consult = loadConsult(consultId);
		if (consult == null || consult.getAcceptStatus() != 0) {// 0是未接收状态
			return new ServerResult(ResultConst.NOT_SATISFY);
		} else if (consult.getPayStatus() != 1) {// pay status 1表示已经付费
			return new ServerResult(ResultConst.HAS_NOT_PAY);
		} else if (doctor.getId() != consult.getDoctorId()) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		/*
		 * ****************如果是接受：医生即将到账，更新consult状态，添加最近联系人，添加患者列表。*******
		 * ****************如果是拒绝：退款，更新consult状态*******************
		 */

		dao.onRefuseConsult(consultId, reason);
		ChatAcceptRequest request = new ChatAcceptRequest(RESPONSE_CONSULT, doctor, consult, false);
		chatServer.postMsg(request);
		return new ServerResult();
	}

	/** 结束图文咨询 */
	protected ServerResult stopChat(Doctor doctor, String consultId) {
		Consult consult = loadConsult(consultId);
		if (consult == null || consult.getAcceptStatus() != 1 || consult.getConsultStatus() != 1 || consult.getDoctorId() != doctor.getId()) {
			return new ServerResult(ResultConst.NOT_SATISFY);
		}
		handleStopConsult(doctor, consult);
		ChatAcceptRequest request = new ChatAcceptRequest(STOP_CONSULT, doctor, consult);
		chatServer.postMsg(request);
		return new ServerResult();
	}

	/** 最近联系人 */
	protected ServerResult loadChatHistory(Doctor doctor) {
		List<ChatRecord> chatRecords = dao.loadChatRecords(doctor);
		return new ServerResult("list", chatRecords);
	}

	/** 所有咨询 */
	protected ServerResult loadAllConsult(Doctor doctor) {
		List<Consult> all = dao.loadAllConsult(doctor);
		return new ServerResult("list", all);
	}

	/** 免费咨询 */
	protected ServerResult loadFreeConsult(Doctor doctor, int page, int pageSize) {
		List<Consult> list = dao.loadFreeConsult(doctor, page, pageSize);
		// 不显示和医生有相同电话的患者的提问，这种情况比较少，所以不在sql里面做了
		for (Iterator<Consult> iter = list.listIterator(); iter.hasNext();) {
			if (iter.next().getPhone().equals(doctor.getPhone())) {
				iter.remove();
			}
		}
		return new ServerResult("list", list, true);
	}

	/** 已经完成的，患者列表 ,type=0表示免费类型，非0表示付费类型 */
	protected ServerResult loadFinishedChat(Doctor doctor, Integer page, Integer pageSize) {
		List<Patient> patients = new ArrayList<>();
		List<RowData> patient_count = new ArrayList<>();

		patient_count = dao.loadFinishedConsultCount(doctor);
		patients = dao.loadFinishedConsultPatients(doctor, page, pageSize);
		List<MapObject> resList = new ArrayList<>(patients.size());
		for (Patient sp : patients) {
			MapObject mo = sp.serializeToMapObject();
			/* 每个患者总共咨询次数 */
			mo.put("count", getPatientConsultCount(sp.getPatientId(), patient_count));
			resList.add(mo);
		}
		return new ServerResult("list", resList, true);
	}

	private Integer getPatientConsultCount(int patientId, List<RowData> patient_count) {
		for (RowData rd : patient_count) {
			if ((Integer) rd.get("patientId") == patientId) {
				return ((Long) rd.get("count")).intValue();
			}
		}
		return 0;
	}

	protected ServerResult searchFinishedChat(Doctor doctor, int type, String key) {
		if (type < 0 || type > 1) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		List<Patient> res = null;
		if (type == CONSULT_FREE) {
			res = dao.searchFinishedPatientsFree(doctor, key);
		} else {
			res = dao.searchFinishedPatientsPayed(doctor, key);
		}
		res = CollectionUtil.emptyIfNull(res);
		return new ServerResult("list", res, true);
	}

	/** 未处理的咨询请求 */
	protected ServerResult loadNewChat(Doctor doctor, Integer page, Integer pageSize) {
		int count = dao.countNewPayed(doctor);
		ServerResult res = new ServerResult();
		res.putObjects("list", loadUnhandledChatList(doctor, page, pageSize));
		res.put("total", count);
		return res;
	}

	/** 正在咨询状态的 */
	protected ServerResult loadChatingChat(Doctor doctor, Integer page, Integer pageSize) {
		List<Consult> chating = dao.loadChatingConsult(doctor, page, pageSize);
		int count = dao.countChating(doctor);
		ServerResult res = new ServerResult();
		res.putObjects("list", chating);
		res.put("total", count);
		return res;
	}

	/** 未完成的（包括未处理的和正在进行的） */
	protected ServerResult loadUnFinishedChat(Doctor doctor) {
		List<Consult> unfinish = loadUnFinishedChatList(doctor);
		return new ServerResult("list", unfinish);
	}

	/** 已经完成的，根据患者查询 ,type=0表示免费类型，1表示付费类型 ，2表示全部 */
	protected ServerResult loadFinishedChatByPatient(Doctor doctor, int patientId, int type) {
		if (type < 0) {
			return new ServerResult(ResultConst.PARAM_ERROR);
		}
		List<Consult> consults = null;
		if (type == CONSULT_FREE) {
			consults = dao.loadFinishedConsultByPatientFree(doctor, patientId);
		} else if (type == CONSULT_PAYED) {
			consults = dao.loadFinishedConsultByPatientPayed(doctor, patientId);
		} else {
			consults = dao.loadFinishedConsultByPatientAll(doctor, patientId);
		}
		ServerResult res = new ServerResult();
		res.putObjects("list", consults);
		return res;
	}

	protected List<Consult> loadUnFinishedChatList(Doctor doctor) {
		List<Consult> unfinish = dao.loadUnFinishedConsult(doctor);
		return unfinish;
	}

	/** 未处理的咨询请求 */
	protected List<Consult> loadUnhandledChatList(Doctor doctor, Integer page, Integer pageSize) {
		return dao.loadUnacceptConsult(doctor, page, pageSize);
	}

	/** 未读信息 */
	protected ServerResult loadUnreadMsgs(Doctor doctor) {
		List<UnreadMsg> unreadMsgs = dao.loadUnreadMsgs(doctor);
		if (CollectionUtil.isNullOrEmpty(unreadMsgs)) {
			return new ServerResult("list", new ArrayList<>());
		}
		UnreadMsgWrapper wrapper = new UnreadMsgWrapper(unreadMsgs);
		return new ServerResult(wrapper);
	}

	/** 根据id获取consult */
	protected Consult loadConsult(String consultId) {
		return dao.loadConsult(consultId);
	}

	/** 查询患者正在进行的咨询 */
	protected ServerResult loadConsultingByPatientId(Doctor doctor, int patientId) {
		Consult consult = dao.loadConsultingByPatientId(doctor, patientId);
		return consult == null ? new ServerResult(ResultConst.DATA_NOT_FOUND) : new ServerResult(consult);
	}

	/** 获取指定患者的咨询列表 */
	protected ServerResult loadAllConsultByPatientId(Doctor doctor, int patientId) {
		List<Consult> consult = dao.loadAllConsultByPatientId(doctor, patientId);
		return CollectionUtil.isNullOrEmpty(consult) ? new ServerResult(ResultConst.DATA_NOT_FOUND) : new ServerResult("list", consult);
	}

	protected ServerResult loadConsultById(Doctor doctor, String consultId) {
		Consult consult = dao.loadConsultById(doctor, consultId);
		return consult == null ? new ServerResult(ResultConst.DATA_NOT_FOUND) : new ServerResult(consult);
	}

	protected ServerResult loadComment(Doctor doctor, String serviceId) {
		ServerResult res = new ServerResult();
		Comment comment = commentService.loadConsultComment(doctor, serviceId);
		res.putAll(ObjectUtil.introspect(comment));
		return res;
	}

	protected ServerResult loadCountInfo(Doctor doctor) {
		int newPayedCount = dao.countNewPayed(doctor);
		int chatingCount = dao.countChating(doctor);
		int newFreeCount = dao.countNewFree(doctor);
		ServerResult res = new ServerResult();
		res.put("newPayedCount", newPayedCount);
		res.put("newFreeCount", newFreeCount);
		res.put("chatingCount", chatingCount);
		return res;
	}

	private void handleStopConsult(Doctor doctor, Consult consult) {
		if (consult == null) {
			return;
		}
		/* 更新consult状态 */
		dao.onStopConsult(consult);

		/* 把未读消息设为已读 */

		/** 原本医生在这个时候入账，现在改为一小时后再入账（跑job），留给患者投诉时间，为了不让医生转账 */
		// Order order = dao.loadOrderByConsultId(consult.getId());
		// if (!patientService.isPersonalPatient(doctor, consult.getPatientId())
		// && order == null) {
		// return;
		// }
		// BigDecimal money = order.getTotalAmount();
		// accountService.coming2Balance(doctor, money);
	}

	@Override
	protected DoctorChat constructInfo(Doctor doctor) {
		DoctorChat res = dao.loadDoctorChat(doctor);
		return res != null ? res : new DoctorChat(doctor.getId(), ServiceStatus.CLOSED.ordinal(), 20);// 默认价格为20元;;
	}

	protected void openDefaulConsult(Doctor doctor) {
		/* 主任医师和副主任医师默认40块，其余20块 */
		int price = (DoctorTitle.Professor.id == doctor.getTitleId() || DoctorTitle.DeputyChief.id == doctor.getTitleId()) ? 40 : 20;
		dao.insertDoctorConsult(doctor, price, ServiceStatus.OPENED.ordinal());
	}

}
