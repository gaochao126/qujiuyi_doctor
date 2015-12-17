/**
 * 
 */
package com.jiuyi.doctor.order;

import org.springframework.stereotype.Repository;

import com.jiuyi.frame.base.DbBase;

/**
 * 
 * @author xutaoyang
 *
 */
@Repository
public class OrderDao extends DbBase {

	private final static String SELECT_ORDER = "SELECT * FROM `t_third_pay_order` WHERE `id`=?";
	private final static String SELECT_ORDER_BY_TYPE_AND_SERVICEID = "SELECT * FROM `t_third_pay_order` WHERE `serviceId`=? AND `orderType`=?";

	protected ThirdPayOrder loadOrder(int id) {
		return queryForObjectDefaultBuilder(SELECT_ORDER, new Object[] { id }, ThirdPayOrder.class);
	}

	/**
	 * @param orderType
	 * @param serviceId
	 * @return
	 */
	protected ThirdPayOrder loadOrderByTypeAndServiceId(OrderType orderType, String serviceId) {
		return queryForObjectDefaultBuilder(SELECT_ORDER_BY_TYPE_AND_SERVICEID, new Object[] { serviceId, orderType.ordinal() }, ThirdPayOrder.class);
	}

}
