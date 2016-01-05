package com.jiuyi.doctor.consult;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.jiuyi.doctor.consult.enums.ConsultAcceptStatus;
import com.jiuyi.doctor.consult.enums.ConsultStatus;
import com.jiuyi.doctor.consult.model.ChatRecord;
import com.jiuyi.doctor.consult.model.Consult;
import com.jiuyi.doctor.consult.model.DoctorChat;
import com.jiuyi.doctor.consult.model.UnreadMsg;
import com.jiuyi.doctor.patients.v2.model.Patient;
import com.jiuyi.doctor.services.ServiceStatus;
import com.jiuyi.doctor.user.model.Doctor;
import com.jiuyi.frame.base.DbBase;
import com.jiuyi.frame.db.RowData;

/**
 * @Author: xutaoyang @Date: 上午11:50:52
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class ConsultDao extends DbBase {

	private static final String SELECT_CHAT_SERVICE = "SELECT * FROM `t_doctor_chat` WHERE `doctorId`=?";

	private static final String SELECT_ALL_CONSULT_BY_PATIENTID = "SELECT * FROM `t_patient_consult` WHERE `patientId`=? AND `doctorId`=?";
	private static final String SELECT_CONSULT_BY_PATIENTID = "SELECT * FROM `t_patient_consult` WHERE `consultStatus`=1 AND `patientId`=? AND `doctorId`=?";
	private static final String SELECT_CONSULT = "SELECT * FROM `t_patient_consult` WHERE `id`=?";
	private static final String SELECT_CHAT_LIST = "SELECT list.*,patient.`name`,patient.`headPortrait` "//
			+ "FROM `t_doctor_chat_list` list, `t_patient` patient "//
			+ "WHERE patient.`id`=list.`patientId` AND `doctorId`=?";//

	private static final String SELECT_CONSULT_BY_CONSULTID = "SELECT consult.*,"// 咨询信息
			+ "patient.`name` as patientName,patient.`gender` AS patientGender,patient.`headPortrait`,patient.`phone`,(year(now())-year(patient.birthday)-1) + ( DATE_FORMAT(patient.birthday, '%m%d') <= DATE_FORMAT(NOW(), '%m%d') ) as patientAge,relative.name " // 患者信息
			+ "FROM `t_patient_consult` consult " // consult
			+ "JOIN `t_patient` patient ON patient.id=consult.patientId "// 患者信息
			+ "LEFT JOIN `t_patient_relative` relative ON relative.id=consult.patientRelativeId "// 就诊人信息
			+ "WHERE consult.`id`=? AND (consult.`doctorId`=? OR consult.`doctorId` IS NULL)";

	/** 包括咨询详情，患者详情，订单金额 */
	private static final String CONSULT_FULL_INFO = ""
			/* 咨询记录信息 */
			+ "SELECT consult.*,"
			/* 患者信息 */
			+ "patient.`name` as patientName,patient.`gender` AS patientGender,patient.`headPortrait`,patient.`phone`,(year(now())-year(patient.birthday)-1) + ( DATE_FORMAT(patient.birthday, '%m%d') <= DATE_FORMAT(NOW(), '%m%d') ) as patientAge,"
			+ "orders.`totalAmount` as money "/* 金额 */
			+ "FROM `t_patient_consult` consult " /* 咨询详情 */
			+ "JOIN `t_patient` patient ON patient.id=consult.patientId "/* 患者信息 */
			+ "LEFT JOIN `t_third_pay_order` orders ON consult.id=orders.serviceId AND orders.orderType=2 ";/*
																											 * 可能无订单信息，
																											 * 用left
																											 * join
																											 */

	private static final String SELECT_ALL_CHAT = CONSULT_FULL_INFO /* 所有咨询 */
			+ "WHERE consult.`doctorId`=? ORDER BY `createTime` DESC";

	private static final String SELECT_UNHANDLED_CHAT = CONSULT_FULL_INFO /* 未接收的咨询 */
			+ "WHERE consult.`acceptStatus`=0 AND `consultStatus`=0 AND consult.`payStatus`=1 AND consult.`doctorId`=? ORDER BY `createTime` DESC LIMIT ?,?";

	private static final String SELECT_CHATING = CONSULT_FULL_INFO /* 正在咨询 */
			+ "WHERE consult.`acceptStatus`=1 AND consult.`consultStatus`=1 AND consult.`doctorId`=? ORDER BY `createTime` DESC LIMIT ?,?";

	private static final String SELECT_UNFINISHED_CHAT = CONSULT_FULL_INFO /* 未完成的咨询 */
			+ "WHERE (consult.`acceptStatus`=0 OR (consult.`acceptStatus`=1 AND consult.`consultStatus`=1)) AND consult.`payStatus`=1 AND consult.`doctorId`=?  ORDER BY `createTime` DESC";

	private static final String SELECT_FREE_CHAT = CONSULT_FULL_INFO
			/* 免费咨询 ,不需要订单信息 */
			+ "WHERE consult.`acceptStatus`=0 AND consult.`consultStatus`=0 AND `type`=0 LIMIT ?,?";

	// 历史记录begin
	private static final String FINISED_PATIENT_LIST = "SELECT distinct(c.patientId),p.name,p.gender,p.headPortrait,p.birthday AS age,r.remark,r.note "//
			+ "FROM `t_patient_consult` c " /* 咨询信息 */
			+ "JOIN `t_patient` p ON c.patientId =p.id " /* 患者信息 */
			+ "LEFT JOIN `t_doctor_remark_patient` r ON r.patientId=c.patientId AND r.doctorId=c.doctorId "/* 备注信息 */
			+ "WHERE c.`acceptStatus`=1 AND c.`consultStatus`=2 AND c.`doctorId`=? ";

	private static final String FINISHED_CONSULT_BY_PATIENTID = "SELECT consult.`id`,consult.`patientId`,consult.`createTime`,consult.`type`,consult.`acceptStatus`,consult.`consultStatus`,consult.`symptoms`,orders.`totalAmount` as money, " //
			+ "consult.`age`,consult.`gender`,IF(cmt.`commentTime`,TRUE,FALSE) AS hasComment "//
			+ "FROM `t_patient_consult` consult "//
			+ "LEFT JOIN `t_third_pay_order` orders ON consult.id=orders.serviceId AND orders.orderType=2 " // 订单
			+ "LEFT JOIN `t_doctor_comment` cmt ON consult.id=cmt.serviceId " // 评论
			+ "WHERE consult.`acceptStatus`=1 AND consult.`consultStatus`=2 AND consult.`patientId`=? AND consult.`doctorId`=? ";//

	private static final String SELECT_FINISED_PATIENT_LIST_FREE = FINISED_PATIENT_LIST + " AND c.type=0  ORDER BY `createTime` DESC LIMIT ?,?";
	private static final String SELECT_FINISH_CONSULT_BY_PATIENTID_FREE = FINISHED_CONSULT_BY_PATIENTID + " AND consult.type=0 ORDER BY `createTime` DESC";
	private static final String SELECT_FINISED_PATIENT_LIST = FINISED_PATIENT_LIST + " ORDER BY `createTime` DESC LIMIT ?,?";
	private static final String SELECT_FINISED_PATIENT_LIST_PAYED = FINISED_PATIENT_LIST + " AND c.type IN(1,2) ORDER BY `createTime` DESC LIMIT ?,?";
	private static final String SELECT_FINISH_CONSULT_BY_PATIENTID_PAYED = FINISHED_CONSULT_BY_PATIENTID + " AND consult.type IN(1,2) ORDER BY `createTime` DESC";
	private static final String SELECT_FINISH_CONSULT_BY_PATIENTID = FINISHED_CONSULT_BY_PATIENTID + " ORDER BY `createTime` DESC";

	private static final String SELECT_FINISH_CONSULT_COUNT = "SELECT patientId, COUNT(*) as count FROM `t_patient_consult` WHERE `acceptStatus`=1 AND `consultStatus`=2 AND `doctorId`=? GROUP BY `patientId`";
	private static final String SELECT_FINISH_CONSULT_COUNT_FREE = "SELECT patientId, COUNT(*) as count FROM `t_patient_consult` WHERE `acceptStatus`=1 AND `consultStatus`=2 AND `doctorId`=? AND `type`=0 GROUP BY `patientId`";
	private static final String SELECT_FINISH_CONSULT_COUNT_PAYED = "SELECT patientId, COUNT(*) as count FROM `t_patient_consult` WHERE `acceptStatus`=1 AND `consultStatus`=2 AND `doctorId`=? AND `type`=1 GROUP BY `patientId`";
	// 历史记录end

	// 历史记录搜索begin
	private static final String SEARCH_FINISED_PATIENT_LIST_FREE = FINISED_PATIENT_LIST + " AND c.type=0 AND (p.`name` LIKE #key# OR r.`remark` LIKE #key#)";
	private static final String SEARCH_FINISED_PATIENT_LIST_PAYED = FINISED_PATIENT_LIST + " AND c.type IN(1,2) AND (p.`name` LIKE #key# OR r.`remark` LIKE #key#)";
	// 历史记录搜索end

	// 咨询中，付费新申请，免费申请count
	private static final String SELECT_CHATING_COUNT = "SELECT COUNT(*) FROM `t_patient_consult` consult,`t_patient` patient  WHERE consult.patientId = patient.id AND  consult.`consultStatus`=1 AND consult.`doctorId`=?";
	private static final String SELECT_NEW_PAYED_COUNT = "SELECT COUNT(*) FROM `t_patient_consult` consult,`t_patient` patient  WHERE consult.patientId = patient.id AND  consult.`acceptStatus`=0 AND  consult.`consultStatus`=0 AND  consult.`payStatus`=1 AND  consult.`type` IN(1,2) AND consult.`doctorId`=? ";
	private static final String SELECT_NEW_FREE_COUNT = "SELECT COUNT(*) FROM `t_patient_consult` consult,`t_patient` patient WHERE consult.patientId = patient.id AND consult.`acceptStatus`=0 AND consult.`consultStatus`=0 AND consult.`type`=0";
	// 咨询中，付费新申请count end

	private static final String SELECT_UNREAD_MSG = "SELECT `sender`,`chatType`,`chatContent`,`chatTime`,`serviceId` FROM `t_chat_his` WHERE `receiver`=? AND `receiverType`=1 AND `readStatus`=0";
	private static final String INSERT_CHAT_LIST = "REPLACE INTO `t_doctor_chat_list`(`doctorId`,`patientId`) VALUE(?,?)";

	private static final String ON_START_CONSULT = "UPDATE `t_patient_consult` SET `doctorId`=?, `acceptStatus`=?,`consultStatus`=?,`startTime`=? WHERE `id`=?";
	private static final String ON_REFUSE_CONSULT = "UPDATE `t_patient_consult` SET `acceptStatus`=?,`consultStatus`=?,`endTime`=?,`refuseReason`=? WHERE `id`=?";
	private static final String ON_STOP_CONSULT = "UPDATE `t_patient_consult` SET `acceptStatus`=?,`consultStatus`=?,`endTime`=? WHERE `id`=?";
	private static final String UPDATE_CONSULT_ACCEPT_STATUS = "UPDATE `t_patient_consult` SET `acceptStatus`=? WHERE `id`=?";
	private static final String UPDATE_CONSULT_STATUS = "UPDATE `t_patient_consult` SET `consultStatus`=? WHERE `id`=?";
	private static final String UPDATE_COUPON_STATUS = "UPDATE `t_coupon` SET `status`=? WHERE `id`=?";
	private static final String UPDATE_CHAT_STATUS = "INSERT `t_doctor_chat`(`doctorId`,`status`,`price`) VALUES(?,?,?) ON DUPLICATE KEY UPDATE `status`=?";
	private static final String UPDATE_CHAT_PRICE = "INSERT `t_doctor_chat`(`doctorId`,`status`,`price`) VALUES(?,?,?) ON DUPLICATE KEY UPDATE `price`=?";
	private static final String INSERT_CHAT_PRICE = "INSERT `t_doctor_chat`(`doctorId`,`status`,`price`) VALUES(?,?,?) ON DUPLICATE KEY UPDATE `status`=?,`price`=?";
	private static final String UPDATE_CHAT_HIS_READ_STATUS = "UPDATE `t_chat_his` SET `readStatus`=1 WHERE `serviceType`=1 AND `serviceId`=?";

	protected DoctorChat loadDoctorChat(Doctor doctor) {
		return queryForObjectDefaultBuilder(SELECT_CHAT_SERVICE, new Object[] { doctor.getId() }, DoctorChat.class);
	}

	protected void insertDoctorConsult(Doctor doctor, int price, int status) {
		jdbc.update(INSERT_CHAT_PRICE, doctor.getId(), status, price, status, price);
	}

	protected void updateChatStatus(Doctor doctor, ServiceStatus serviceStatus) {
		jdbc.update(UPDATE_CHAT_STATUS, new Object[] { doctor.getId(), serviceStatus.ordinal(), 10, serviceStatus.ordinal() });
	}

	protected void updateChatPrice(Doctor doctor, Integer price) {
		jdbc.update(UPDATE_CHAT_PRICE, doctor.getId(), ServiceStatus.CLOSED.ordinal(), price, price);
	}

	protected List<UnreadMsg> loadUnreadMsgs(Doctor doctor) {
		return jdbc.query(SELECT_UNREAD_MSG, new Object[] { doctor.getId() }, UnreadMsg.builder);
	}

	protected void addChatList(Doctor doctor, int patientId) {
		jdbc.update(INSERT_CHAT_LIST, new Object[] { doctor.getId(), patientId });
	}

	protected List<ChatRecord> loadChatRecords(Doctor doctor) {
		return queryForList(SELECT_CHAT_LIST, new Object[] { doctor.getId() }, ChatRecord.class);
	}

	protected void updateCouponStatus(int couponId, int status) {
		jdbc.update(UPDATE_COUPON_STATUS, new Object[] { couponId, status });
	}

	protected void updateConsultAcceptStatus(String consultId, ConsultAcceptStatus status) {
		jdbc.update(UPDATE_CONSULT_ACCEPT_STATUS, new Object[] { status.ordinal(), consultId });
	}

	protected void updateConsultStatus(String consultId, ConsultStatus status) {
		jdbc.update(UPDATE_CONSULT_STATUS, new Object[] { status.ordinal(), consultId });
	}

	protected Consult loadConsult(String consultId) {
		return queryForObjectDefaultBuilder(SELECT_CONSULT, new Object[] { consultId }, Consult.class);
	}

	protected List<Consult> loadAllConsult(Doctor doctor) {
		return queryForList(SELECT_ALL_CHAT, new Object[] { doctor.getId() }, Consult.class);
	}

	protected List<Consult> loadUnacceptConsult(Doctor doctor, int page, int pageSize) {
		return queryForList(SELECT_UNHANDLED_CHAT, new Object[] { doctor.getId(), startIndex(page, pageSize), pageSize }, Consult.class);
	}

	protected List<Consult> loadChatingConsult(Doctor doctor, Integer page, Integer pageSize) {
		return queryForList(SELECT_CHATING, new Object[] { doctor.getId(), startIndex(page, pageSize), pageSize }, Consult.class);
	}

	protected List<Consult> loadUnFinishedConsult(Doctor doctor) {
		return queryForList(SELECT_UNFINISHED_CHAT, new Object[] { doctor.getId() }, Consult.class);
	}

	protected List<Consult> loadFreeConsult(Doctor doctor, int page, int pageSize) {
		return queryForList(SELECT_FREE_CHAT, new Object[] { startIndex(page, pageSize), pageSize }, Consult.class);
	}

	protected List<Patient> loadFinishedConsultPatientsFree(Doctor doctor, Integer page, Integer pageSize) {
		return queryForList(SELECT_FINISED_PATIENT_LIST_FREE, new Object[] { doctor.getId(), startIndex(page, pageSize), pageSize }, Patient.class);
	}

	protected List<Patient> loadFinishedConsultPatientsPayed(Doctor doctor, Integer page, Integer pageSize) {
		return queryForList(SELECT_FINISED_PATIENT_LIST_PAYED, new Object[] { doctor.getId(), startIndex(page, pageSize), pageSize }, Patient.class);
	}

	protected List<Patient> loadFinishedConsultPatients(Doctor doctor, Integer page, Integer pageSize) {
		return queryForList(SELECT_FINISED_PATIENT_LIST, new Object[] { doctor.getId(), startIndex(page, pageSize), pageSize }, Patient.class);
	}

	protected List<Consult> loadFinishedConsultByPatientFree(Doctor doctor, int patientId) {
		return queryForList(SELECT_FINISH_CONSULT_BY_PATIENTID_FREE, new Object[] { patientId, doctor.getId() }, Consult.class);
	}

	protected List<Consult> loadFinishedConsultByPatientPayed(Doctor doctor, int patientId) {
		return queryForList(SELECT_FINISH_CONSULT_BY_PATIENTID_PAYED, new Object[] { patientId, doctor.getId() }, Consult.class);
	}

	protected List<Consult> loadFinishedConsultByPatientAll(Doctor doctor, int patientId) {
		return queryForList(SELECT_FINISH_CONSULT_BY_PATIENTID, new Object[] { patientId, doctor.getId() }, Consult.class);
	}

	protected List<RowData> loadFinishedConsultCountFree(Doctor doctor) {
		return queryForRowDataList(SELECT_FINISH_CONSULT_COUNT_FREE, doctor.getId());
	}

	protected List<RowData> loadFinishedConsultCountPayed(Doctor doctor) {
		return queryForRowDataList(SELECT_FINISH_CONSULT_COUNT_PAYED, doctor.getId());
	}

	protected List<RowData> loadFinishedConsultCount(Doctor doctor) {
		return queryForRowDataList(SELECT_FINISH_CONSULT_COUNT, doctor.getId());
	}

	protected void onRefuseConsult(String consultId, String reason) {
		jdbc.update(ON_REFUSE_CONSULT, new Object[] { ConsultAcceptStatus.REFUSED.ordinal(), ConsultStatus.FINISHED.ordinal(), new Date(), reason, consultId });
	}

	protected void onAcceptConsult(Doctor doctor, Consult consult) {
		jdbc.update(ON_START_CONSULT, new Object[] { doctor.getId(), ConsultAcceptStatus.ACCEPTED.ordinal(), ConsultStatus.STARTED.ordinal(), new Date(), consult.getId() });
	}

	protected void onStopConsult(Consult consult) {
		jdbc.update(ON_STOP_CONSULT, new Object[] { ConsultAcceptStatus.ACCEPTED.ordinal(), ConsultStatus.FINISHED.ordinal(), new Date(), consult.getId() });
	}

	protected void setChatHisReadStatus(Consult consult) {
		jdbc.update(UPDATE_CHAT_HIS_READ_STATUS, consult.getId());
	}

	protected Consult loadConsultingByPatientId(Doctor doctor, int patientId) {
		return queryForObjectDefaultBuilder(SELECT_CONSULT_BY_PATIENTID, new Object[] { patientId, doctor.getId() }, Consult.class);
	}

	protected Consult loadConsultById(Doctor doctor, String consultId) {
		return queryForObjectDefaultBuilder(SELECT_CONSULT_BY_CONSULTID, new Object[] { consultId, doctor.getId() }, Consult.class);
	}

	protected List<Consult> loadAllConsultByPatientId(Doctor doctor, int patientId) {
		return queryForList(SELECT_ALL_CONSULT_BY_PATIENTID, new Object[] { patientId, doctor.getId() }, Consult.class);
	}

	protected List<Patient> searchFinishedPatientsFree(Doctor doctor, String key) {
		String sql = SEARCH_FINISED_PATIENT_LIST_FREE.replaceAll("#key#", "'%" + key + "%'");
		return queryForList(sql, new Object[] { doctor.getId(), }, Patient.class);
	}

	protected List<Patient> searchFinishedPatientsPayed(Doctor doctor, String key) {
		String sql = SEARCH_FINISED_PATIENT_LIST_PAYED.replaceAll("#key#", "'%" + key + "%'");
		return queryForList(sql, new Object[] { doctor.getId(), }, Patient.class);
	}

	protected int countChating(Doctor doctor) {
		return queryForInteger(SELECT_CHATING_COUNT, doctor.getId());
	}

	protected int countNewPayed(Doctor doctor) {
		return queryForInteger(SELECT_NEW_PAYED_COUNT, doctor.getId());
	}

	protected int countNewFree(Doctor doctor) {
		return queryForInteger(SELECT_NEW_FREE_COUNT);
	}
}
