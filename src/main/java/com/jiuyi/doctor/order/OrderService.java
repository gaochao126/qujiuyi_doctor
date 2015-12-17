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
public class OrderService {

	private @Autowired OrderManager manager;

	public ThirdPayOrder loadOrderByTypeAndServiceId(OrderType orderType, String serviceId) {
		return manager.loadOrderByTypeAndServiceId(orderType, serviceId);
	}

}
