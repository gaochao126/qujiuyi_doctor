/**
 * 
 */
package com.jiuyi.doctor.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
