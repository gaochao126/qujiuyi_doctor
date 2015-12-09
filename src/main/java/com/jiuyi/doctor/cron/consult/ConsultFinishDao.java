package com.jiuyi.doctor.cron.consult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyi.frame.base.DbBase;
import com.jiuyi.frame.util.StringUtil;

/**
 * @Author: xutaoyang @Date: 下午1:33:48
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class ConsultFinishDao extends DbBase {

	private static final String SELECT = "SELECT o.totalAmount,o.orderNumber,consult.doctorId FROM t_order o,t_patient_consult consult WHERE o.accountArrivalStatus=0 AND unix_timestamp(consult.endTime)+?<=unix_timestamp(now()) AND consult.acceptStatus=1 AND consult.consultStatus=2 AND o.serviceId=consult.id; ";
	private static final String SELECT_UNHANDLED_CHAT = "SELECT consult.id, consult.doctorId,consult.patientId,doctor.name,o.orderNumber,o.couponId,o.payAmount,o.balance "
			+ "FROM t_patient_consult consult "
			+ "LEFT JOIN t_order o ON consult.id = o.serviceId "
			+ "LEFT JOIN t_doctor doctor ON consult.doctorId = doctor.id "
			+ "WHERE consult.acceptStatus=0 AND `type`=1 AND unix_timestamp(consult.createTime)+?<unix_timestamp(now());";

	private static final String UPDATE_ORDER_STATUS = "UPDATE `t_order` SET `accountArrivalStatus`=1 WHERE `orderNumber`=?";
	private static final String COMING_TO_BALANCE = "UPDATE `t_doctor_account` SET `balance`=`balance`+?,`totalIncome`=`totalIncome`+?, `coming`=`coming`-? WHERE `doctorId`=?";
	private static final String INSERT_ACCOUNT_DETAIL = "INSERT `t_doctor_account_detail`(`doctorId`,`src`,`srcType`,`type`,`money`) VALUE(?,?,?,?,?)";

	private static final String UPDATE_CONSULT_ACCEPT_STATUS = "UPDATE `t_patient_consult` SET `acceptStatus`=?,`consultStatus`=? WHERE `id`=?";
	private static final String UPDATE_COUPON_STATUS = "UPDATE `t_coupon` SET `status`=? WHERE `id`=?";
	private static final String INC_PATIENT_BALANCE = "UPDATE `t_patient` SET `balance`=`balance`+ ? WHERE `id`=?";
	private static final String INSERT_PATIENT_ACCOUNT_DETAIL = "INSERT `t_patient_account_detail`(`patientId`,`type`,`transactionNum`,`amount`,`createTime`) VALUE(?,?,?,?,?)";

	protected List<ConsultInfo> loadSatisfiedConsult(int delay) {
		return jdbc.query(SELECT, new Object[] { delay }, ConsultInfo.builder);
	}

	protected List<ConsultOrder> loadExpireConsult(int expireTime) {
		return jdbc.query(SELECT_UNHANDLED_CHAT, new Object[] { expireTime }, ConsultOrder.builder);
	}

	@Transactional(rollbackFor = Exception.class)
	public void handleConsult(List<ConsultInfo> consultList) {
		updateOrderStatus(consultList);
		coming2balance(consultList);
		insertAccoutDetail(consultList);
	}

	/** 理论上业务不应该在这里实现的，但是为了事务，懒得再去加一个类了 */
	@Transactional(rollbackFor = Exception.class)
	public void handleExpireConsult(List<ConsultOrder> consultOrders) {
		// 修改咨询接受状态，退款（礼券，金额），患者收支详情
		List<Object[]> updateAcceptStatusArgs = new ArrayList<>(consultOrders.size());
		List<Object[]> updateCoupon = new ArrayList<>(consultOrders.size());
		List<Object[]> updatePatientBalance = new ArrayList<>(consultOrders.size());
		List<Object[]> patientAccountDetail = new ArrayList<>(consultOrders.size());

		for (ConsultOrder consultOrder : consultOrders) {
			updateAcceptStatusArgs.add(new Object[] { 3, 2, consultOrder.consultId });// 接收状态改为超时，咨询状态改为结束
			String orderNumber = consultOrder.orderNumber;
			if (StringUtil.isNullOrEmpty(orderNumber)) {// 有可能没有订单，比如私人医生
				continue;
			}
			int couponId = consultOrder.couponId;
			if (couponId > 0) {
				updateCoupon.add(new Object[] { 0, couponId });
			}
			int patientId = consultOrder.patientId;
			BigDecimal totalMoney = consultOrder.balance.add(consultOrder.payAmount);
			updatePatientBalance.add(new Object[] { totalMoney, patientId });
			patientAccountDetail.add(new Object[] { patientId, 5, orderNumber, totalMoney, new Date() });
		}
		jdbc.batchUpdate(UPDATE_CONSULT_ACCEPT_STATUS, updateAcceptStatusArgs);
		jdbc.batchUpdate(UPDATE_COUPON_STATUS, updateCoupon);
		jdbc.batchUpdate(INC_PATIENT_BALANCE, updatePatientBalance);
		jdbc.batchUpdate(INSERT_PATIENT_ACCOUNT_DETAIL, patientAccountDetail);
	}

	/** 订单状态改为医生已经到账 */
	public void updateOrderStatus(List<ConsultInfo> consultList) {
		List<Object[]> args = new ArrayList<>(consultList.size());
		for (ConsultInfo consult : consultList) {
			args.add(new Object[] { consult.orderNumber });
		}
		jdbc.batchUpdate(UPDATE_ORDER_STATUS, args);
	}

	/** 医生入账 */
	public void coming2balance(List<ConsultInfo> consultList) {
		List<Object[]> args = new ArrayList<>(consultList.size());
		for (ConsultInfo consult : consultList) {
			args.add(new Object[] { consult.money, consult.money, consult.money, consult.doctorId });
		}
		jdbc.batchUpdate(COMING_TO_BALANCE, args);
	}

	/** 生成医生账单 */
	public void insertAccoutDetail(List<ConsultInfo> consultList) {
		List<Object[]> args = new ArrayList<>(consultList.size());
		for (ConsultInfo consult : consultList) {
			args.add(new Object[] { consult.doctorId, consult.orderNumber, 1, 1, consult.money });
		}
		jdbc.batchUpdate(INSERT_ACCOUNT_DETAIL, args);
	}

}
