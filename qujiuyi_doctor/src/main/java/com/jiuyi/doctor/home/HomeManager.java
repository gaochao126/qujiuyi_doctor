package com.jiuyi.doctor.home;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jiuyi.doctor.account.AccountService;
import com.jiuyi.doctor.appointment.AppointmentService;
import com.jiuyi.doctor.comment.Comment;
import com.jiuyi.doctor.comment.CommentService;
import com.jiuyi.doctor.consult.ConsultService;
import com.jiuyi.doctor.home.model.AdBanner;
import com.jiuyi.doctor.home.model.HomeInfo;
import com.jiuyi.doctor.home.model.SystemMsg;
import com.jiuyi.doctor.personal.PersonalService;
import com.jiuyi.doctor.prescribe.PrescribeService;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.ManagerBase;
import com.jiuyi.frame.front.ServerResult;
import com.jiuyi.frame.keyvalue.KeyValueService;
import com.jiuyi.frame.reload.IReloader;
import com.jiuyi.frame.reload.ReloadManager;

/**
 * @Author: xutaoyang @Date: 下午2:47:21
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Service
public class HomeManager extends ManagerBase<Doctor, HomeInfo>implements IReloader {
	private static final int PAGE_SIZE = 10;

	private static final String TARGET_GET_UNREAD_TIME = "home_get_unread_time";

	private @Autowired HomeDao dao;

	private @Autowired AccountService accountService;

	private @Autowired SystemMsgManager systemMsgManager;

	private @Autowired ConsultService chatService;

	private @Autowired PrescribeService prescribeService;

	private @Autowired AppointmentService appointmentService;

	private @Autowired KeyValueService keyValueService;

	private @Autowired PersonalService personalService;

	private @Autowired ReloadManager reloadManager;

	private @Autowired CommentService commentService;

	private List<AdBanner> adBannerList;

	@PostConstruct
	public void init() {
		reloadManager.registerReloader("home", this);
		reload();
	}

	/** 获取首页展示的信息 */
	protected ServerResult loadHomeInfo(Doctor doctor) {
		ServerResult res = new ServerResult();
		BigDecimal totalIncome = accountService.getTotalIncome(doctor);
		HomeInfo homeInfo = loadInfoBase(doctor);
		Date lastGetUnreadEvaTime = homeInfo.lastGetUnreadEvaTime;
		res.put("ad", adBannerList);
		res.put("income", totalIncome);
		res.put("score", doctor.getScore());/* 患者对医生的总评价分数 */
		res.put("personal", personalService.loadUnreadNewPersnalCount(doctor));
		res.putObjects("chat", chatService.loadUnhandledChat(doctor));
		res.putObjects("prescribe", prescribeService.loadUnhandlePresribe(doctor));
		res.putObjects("appointment", appointmentService.loadUnhandleAppointment(doctor));
		res.put("unreadEva", commentService.loadUnreadCmtCount(doctor, lastGetUnreadEvaTime));/* 未读评价数量 */
		return res;
	}

	/** 对医生的评价 */
	protected ServerResult loadEvaluation(Doctor doctor, int page) {
		List<Comment> evaluations = commentService.loadComment(doctor, page, PAGE_SIZE);
		int count = commentService.countTotalComment(doctor);
		Date now = new Date();
		keyValueService.keyValueForever.setValue(doctor.getId(), TARGET_GET_UNREAD_TIME, now);
		loadInfoBase(doctor).lastGetUnreadEvaTime = now;
		ServerResult res = new ServerResult();
		res.put("list", evaluations);
		res.put("count", count);
		return res;
	}

	/** 系统消息 */
	protected ServerResult loadSystemMsg(Doctor doctor, Integer page, Integer pageSize) {
		List<SystemMsg> sysMsgs = systemMsgManager.getSystemMsg(page - 1, pageSize);
		return new ServerResult("list", sysMsgs);
	}

	@Override
	public void reload() {
		this.adBannerList = dao.loadAdBanners();
	}

	@Override
	protected HomeInfo constructInfo(Doctor doctor) {
		Date lastGetTime = keyValueService.keyValueForever.getValueDate(doctor.getId(), TARGET_GET_UNREAD_TIME);
		lastGetTime = lastGetTime == null ? new Date(0) : lastGetTime;
		return new HomeInfo(lastGetTime);
	}
}