package com.jiuyi.doctor.cron.personaldoctor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.jiuyi.frame.base.DbBase;

/**
 * @Author: xutaoyang @Date: 上午9:39:02
 *
 * @Description
 *
 * @Copyright @ 2015 重庆玖壹健康管理有限公司
 */
@Repository
public class PersonalDoctorFinishDao extends DbBase {

	private static final String SELECT = "SELECT o.`orderNumber`, o.`doctorId`,o.`totalAmount` FROM t_personal_doctor personal,t_order o WHERE o.accountArrivalStatus=0 AND personal.id=o.serviceId AND unix_timestamp(personal.expirationTime)+?<=unix_timestamp(now());";
	private static final String UPDATE_ORDER_STATUS = "UPDATE `t_order` SET `accountArrivalStatus`=1 WHERE `orderNumber`=?";
	private static final String COMING_TO_BALANCE = "UPDATE `t_doctor_account` SET `balance`=`balance`+?, `totalIncome`=`totalIncome`+?, `coming`=`coming`-? WHERE `doctorId`=?";
	private static final String INSERT_ACCOUNT_DETAIL = "INSERT `t_doctor_account_detail`(`doctorId`,`src`,`srcType`,`type`,`money`) VALUE(?,?,?,?,?)";

	/** load出刚结束私人医生服务的且医生还未到账的订单信息，延迟delay，单位秒 */
	public List<OrderInfo> loadSatisfyOrder(int delay) {
		return jdbc.query(SELECT, new Object[] { delay }, OrderInfo.builder);
	}

	@Transactional(rollbackFor = Exception.class)
	public void handleOrder(List<OrderInfo> toHandle) {
		updateOrderStatus(toHandle);
		coming2balance(toHandle);
		insertAccoutDetail(toHandle);
	}

	/** 订单状态改为医生已经到账 */
	public void updateOrderStatus(List<OrderInfo> orderList) {
		List<Object[]> args = new ArrayList<>(orderList.size());
		for (OrderInfo order : orderList) {
			args.add(new Object[] { order.orderNumber });
		}
		jdbc.batchUpdate(UPDATE_ORDER_STATUS, args);
	}

	/** 医生入账 */
	public void coming2balance(List<OrderInfo> orderList) {
		List<Object[]> args = new ArrayList<>(orderList.size());
		for (OrderInfo order : orderList) {
			args.add(new Object[] { order.money, order.money, order.money, order.doctorId });
		}
		jdbc.batchUpdate(COMING_TO_BALANCE, args);
	}

	/** 生成医生账单 */
	public void insertAccoutDetail(List<OrderInfo> orderList) {
		List<Object[]> args = new ArrayList<>(orderList.size());
		for (OrderInfo order : orderList) {
			args.add(new Object[] { order.doctorId, order.orderNumber, 2, 1, order.money });
		}
		jdbc.batchUpdate(INSERT_ACCOUNT_DETAIL, args);
	}
}
