package com.jiuyi.doctor.cron.consult;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 下午1:33:48
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class ConsultFinishDao extends DbBase {

	//@formatter:off
	private static final String SELECT = "SELECT o.totalAmount,o.id AS orderId,consult.doctorId "
			+ "FROM t_third_pay_order o,t_patient_consult consult "
			+ "WHERE o.accountArrivalStatus=0 AND unix_timestamp(consult.endTime)+?<=unix_timestamp(now()) AND consult.acceptStatus=1 AND consult.consultStatus=2 AND o.serviceId=consult.id AND o.orderType=2; ";
	
	private static final String SELECT_UNHANDLED_CHAT = "SELECT consult.id, consult.doctorId,consult.patientId,doctor.name,o.id as orderId,o.couponId,o.totalAmount " 
			+ "FROM t_patient_consult consult " 
			+ "LEFT JOIN t_third_pay_order o ON consult.id = o.serviceId " 
			+ "LEFT JOIN t_doctor doctor ON consult.doctorId = doctor.id "
			+ "WHERE consult.acceptStatus=0 AND `type`=1 AND unix_timestamp(consult.createTime)+?<unix_timestamp(now());";

	private static final String UPDATE_ORDER_STATUS = "UPDATE `t_third_pay_order` SET `accountArrivalStatus`=1 WHERE `id`=?";
	private static final String COMING_TO_BALANCE = "UPDATE `t_doctor_account` SET `balance`=`balance`+?,`totalIncome`=`totalIncome`+?, `coming`=`coming`-? WHERE `doctorId`=?";
	private static final String INSERT_ACCOUNT_DETAIL = "INSERT `t_doctor_account_detail`(`doctorId`,`src`,`srcType`,`type`,`money`) VALUE(?,?,?,?,?)";

	private static final String UPDATE_CONSULT_ACCEPT_STATUS = "UPDATE `t_patient_consult` SET `acceptStatus`=?,`consultStatus`=? WHERE `id`=?";
	//@formatter:on

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

		for (ConsultOrder consultOrder : consultOrders) {
			updateAcceptStatusArgs.add(new Object[] { 3, 2, consultOrder.consultId });// 接收状态改为超时，咨询状态改为结束
		}
		jdbc.batchUpdate(UPDATE_CONSULT_ACCEPT_STATUS, updateAcceptStatusArgs);
	}

	/** 订单状态改为医生已经到账 */
	public void updateOrderStatus(List<ConsultInfo> consultList) {
		List<Object[]> args = new ArrayList<>(consultList.size());
		for (ConsultInfo consult : consultList) {
			args.add(new Object[] { consult.getOrderId() });
		}
		jdbc.batchUpdate(UPDATE_ORDER_STATUS, args);
	}

	/** 医生入账 */
	public void coming2balance(List<ConsultInfo> consultList) {
		List<Object[]> args = new ArrayList<>(consultList.size());
		for (ConsultInfo consult : consultList) {
			args.add(new Object[] { consult.getMoney(), consult.getMoney(), consult.getMoney(), consult.getDoctorId() });
		}
		jdbc.batchUpdate(COMING_TO_BALANCE, args);
	}

	/** 生成医生账单 */
	public void insertAccoutDetail(List<ConsultInfo> consultList) {
		List<Object[]> args = new ArrayList<>(consultList.size());
		for (ConsultInfo consult : consultList) {
			args.add(new Object[] { consult.getDoctorId(), consult.getOrderId(), 1, 1, consult.getMoney() });
		}
		jdbc.batchUpdate(INSERT_ACCOUNT_DETAIL, args);
	}

}
