/**
 * 
 */
package com.jiuyi.doctor.order;

import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author xutaoyang
 *
 */
@Service
public class OrderManager {

	private @Autowired OrderDao dao;

	protected ThirdPayOrder loadOrder(int id) {
		return dao.loadOrder(id);
	}

	/**
	 * @param orderType
	 * @param serviceId
	 * @return
	 */
	protected ThirdPayOrder loadOrderByTypeAndServiceId(OrderType orderType, String serviceId) {
		return dao.loadOrderByTypeAndServiceId(orderType, serviceId);
	}

}
